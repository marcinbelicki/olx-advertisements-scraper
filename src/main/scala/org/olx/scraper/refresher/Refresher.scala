package org.olx.scraper.refresher

import com.typesafe.scalalogging.LazyLogging
import discord4j.discordjson.json.MessageData
import reactor.core.publisher.Flux

trait Refresher extends LazyLogging {

  protected lazy val name: String = getClass.getSimpleName

  def refresh: Flux[MessageData] =
    Flux.defer { () =>
      logger.info(s"Running.")
      Flux.empty
    }
}
