package org.olx.scraper

import com.typesafe.scalalogging.LazyLogging

trait RunAfterStart extends LazyLogging {
  this: LocalMemoryScraper =>

  locally {
    logger.info(s"Starting $this.")
    for {
      tradeOffers <- refreshAndReturnNewOnesMono
    } yield {
      if (tradeOffers.isEmpty) logger.warn("No offers were found.")
      else logger.info(s"Found ${tradeOffers.size} offer(s).")
    }
  }
}
