package org.olx.scraper

import com.typesafe.scalalogging.LazyLogging
import org.olx.scraper.StoreScraper.exceptionPf
import org.olx.scraper.models.TradeOffer
import org.olx.scraper.reactorextension.MonoExtension.RichMono
import org.olx.scraper.store.OffersStore
import reactor.core.publisher.Mono

import java.io.IOException
import java.net.{SocketTimeoutException, UnknownHostException}

class StoreScraper(
                    scraper: Scraper,
                    offersStore: OffersStore
                  ) extends LazyLogging {


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
      .onErrorResumePf(exceptionPf.andThen(fallback _))

  def refreshAndReturnNewOnes: Set[TradeOffer] =
    offersStore.analyzeAndReturnNewOnes(scraper.elementsSet)
}

object StoreScraper {

  private val exceptionPf: PartialFunction[Throwable, Exception] = {
    case exception: SocketTimeoutException => exception
    case exception: UnknownHostException => exception
    case exception: IOException => exception
  }
}
