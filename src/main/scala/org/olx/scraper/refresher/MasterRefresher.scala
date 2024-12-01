package org.olx.scraper.refresher

import discord4j.discordjson.json.MessageData
import reactor.core.publisher.Flux

class MasterRefresher(
    refreshers: Refresher*
) extends Refresher {
  override def refresh: Flux[MessageData] =
    refreshers
      .map(_.refresh)
      .foldLeft(super.refresh)(_ thenMany _)
}
