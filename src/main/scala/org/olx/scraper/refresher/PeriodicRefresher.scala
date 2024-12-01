package org.olx.scraper.refresher

import discord4j.discordjson.json.MessageData
import reactor.core.publisher.Flux
import reactor.retry.Repeat

import java.time.Duration

class PeriodicRefresher(
    refresher: Refresher,
    period: Duration
) extends Refresher {

  private val repeat: Repeat[MessageData] = Repeat
    .onlyIf[MessageData](_ => true)
    .fixedBackoff(period)

  override val refresh: Flux[MessageData] =
    Flux
      .defer(() => refresher.refresh)
      .repeatWhen(repeat)

  def start(): Unit = refresh.blockLast()

}
