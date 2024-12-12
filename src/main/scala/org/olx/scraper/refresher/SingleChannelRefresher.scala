package org.olx.scraper.refresher

import discord4j.common.util.Snowflake
import discord4j.core.DiscordClient
import discord4j.discordjson.json.MessageData
import org.olx.scraper.{RunAfterStart, Scraper, StoreScraper}
import org.olx.scraper.discord.Bot
import org.olx.scraper.store.MemoryOffersStore
import reactor.core.publisher.Flux

import java.net.URL

class SingleChannelRefresher(
                              bot: Bot,
                              storeScraper: StoreScraper
                            ) extends Refresher {
  override def refresh: Flux[MessageData] =
    storeScraper.refreshAndReturnNewOnesMono
      .flatMapMany(bot.sendOffersMessage)

}

object SingleChannelRefresher {
  def apply(discordClient: DiscordClient, channelId: Snowflake, url: URL): SingleChannelRefresher =
    new SingleChannelRefresher(
      bot = Bot(
        discordClient = discordClient,
        channelId = channelId,
        url = url
      ),
      storeScraper = new StoreScraper(
        scraper = Scraper(
          url = url
        ),
        offersStore = new MemoryOffersStore
      ) with RunAfterStart
    )
}
