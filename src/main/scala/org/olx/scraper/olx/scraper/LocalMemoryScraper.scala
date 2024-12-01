package org.olx.scraper.olx.scraper

import com.typesafe.scalalogging.LazyLogging
import org.olx.scraper.olx.models.TradeOffer
import org.olx.scraper.reactor.MonoExtension.RichMono
import reactor.core.publisher.Mono

import java.io.IOException
import java.net.{SocketTimeoutException, UnknownHostException}
import scala.collection.mutable

class LocalMemoryScraper(
    scraper: Scraper
) extends LazyLogging {

  private val memorizedOffers = mutable.Set.empty[TradeOffer]

  private def fallback(exception: Exception): Mono[Set[TradeOffer]] = {
    logger.error(
      "There was a problem with trying to read the website.",
      exception
    )
    Mono.just(Set.empty)
  }

  def refreshAndReturnNewOnesMono: Mono[Set[TradeOffer]] =
    Mono
      .fromCallable[Set[TradeOffer]](() => refreshAndReturnNewOnes)
      .onErrorResumePf {
        case exception: SocketTimeoutException => fallback(exception)
        case exception: UnknownHostException   => fallback(exception)
        case exception: IOException            => fallback(exception)
      }

  def refreshAndReturnNewOnes: Set[TradeOffer] = {
    val newTradeOffers = scraper.elementsSet -- memorizedOffers
    memorizedOffers ++= newTradeOffers
    newTradeOffers
  }
}
