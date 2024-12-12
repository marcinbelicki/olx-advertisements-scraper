package org.olx.scraper.refresher

import discord4j.common.util.Snowflake
import discord4j.core.DiscordClient
import discord4j.discordjson.json.MessageData
import org.olx.scraper.{LocalMemoryScraper, RunAfterStart, Scraper}
import org.olx.scraper.discord.Bot
import reactor.core.publisher.Flux

import java.net.URL

class SingleChannelRefresher(
                              bot: Bot,
                              localMemoryScraper: LocalMemoryScraper
                            ) extends Refresher {
  override def refresh: Flux[MessageData] =
    localMemoryScraper.refreshAndReturnNewOnesMono
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
      localMemoryScraper = new LocalMemoryScraper(
        scraper = Scraper(
          url = url
        )
      ) with RunAfterStart
    )
}
