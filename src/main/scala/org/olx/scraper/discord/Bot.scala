package org.olx.scraper.discord

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import discord4j.common.util.Snowflake
import discord4j.core.DiscordClient
import discord4j.discordjson.json.MessageData
import org.olx.scraper.discord.Bot.message
import org.olx.scraper.olx.models.TradeOffer
import reactor.core.publisher.{Flux, Mono}

import java.net.URL
import scala.collection.mutable

class Bot(
    client: DiscordClient,
    channelId: Snowflake
) extends LazyLogging {
  private val channel = client.getChannelById(channelId)

  private def startBot(url: URL): Bot = {
    channel
      .createMessage(s"Bot został uruchomiony z linkiem wyszukiwania: $url")
      .block()

    this
  }

  def sendOffersMessage(tradeOffers: Set[TradeOffer]): Flux[MessageData] = {
    val finalTradeOffers = tradeOffers ++ unsentOffers

    if (finalTradeOffers.isEmpty) return noOffersFound

    val sendOffers = for {
      tradeOffer <- finalTradeOffers
      url = tradeOffer.url.toString
      createMessage = channel.createMessage(url)
    } yield createMessage
      .doOnSuccess(_ => unsentOffers -= tradeOffer)
      .onErrorResume { exception =>
        logger.error(
          s"There was an error with sending trade offer: $tradeOffer.",
          exception
        )
        logger.info("Will try to send it another time 🥹🥹🥹.")
        unsentOffers += tradeOffer
        Mono.empty
      }

    val initialFlux = Flux.defer { () =>
      logger.info(s"Found ${finalTradeOffers.size} offer(s).")
      messageCreation
    }

    sendOffers.foldLeft(initialFlux)(_ thenMany _)
  }

  private val unsentOffers = mutable.Set.empty[TradeOffer]

  private def messageCreation: Mono[MessageData] =
    channel
      .createMessage(message)
      .onErrorResume { exception =>
        logger.error(
          "There was an error with sending initial message :/.",
          exception
        )
        Mono.empty
      }

  private val noOffersFound = Flux.defer[MessageData] { () =>
    logger.info("No new offers were found.")
    Flux.empty
  }
}

object Bot {

  val defaultClient: DiscordClient = DiscordClient.create(
    ConfigFactory.load().getString("olx.scraper.client.token")
  )

  private val message = "Znaleziono nowe ogłoszenia:"

  def apply(channelId: Snowflake, url: URL): Bot =
    new Bot(defaultClient, channelId).startBot(url)

}
