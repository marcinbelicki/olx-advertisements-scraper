package org.olx.scraper.refresher

import discord4j.common.util.Snowflake
import discord4j.discordjson.json.MessageData
import org.olx.scraper.discord.Bot
import org.olx.scraper.olx.scraper.{LocalMemoryScraper, RunAfterStart, Scraper}
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
  def apply(channelId: Snowflake, url: URL): SingleChannelRefresher =
    new SingleChannelRefresher(
      bot = Bot(
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
