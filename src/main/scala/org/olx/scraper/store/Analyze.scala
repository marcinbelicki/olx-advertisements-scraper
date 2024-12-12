package org.olx.scraper.store

import org.olx.scraper.models.TradeOffer

trait Analyze {
  this: OffersStore =>

  protected def onlyNewOnes(tradeOffers: Set[TradeOffer]): Set[TradeOffer]

  protected def addNewOnes(newTradeOffers: Set[TradeOffer]): Unit

  final def analyzeAndReturnNewOnes(tradeOffers: Set[TradeOffer]): Set[TradeOffer] = {
    val newTradeOffers = onlyNewOnes(tradeOffers)
    addNewOnes(newTradeOffers)
    newTradeOffers
  }

}
