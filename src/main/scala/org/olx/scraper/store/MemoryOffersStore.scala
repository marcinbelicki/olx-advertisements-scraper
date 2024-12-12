package org.olx.scraper.store

import org.olx.scraper.models.TradeOffer

import scala.collection.mutable

class MemoryOffersStore extends OffersStore with Analyze {

  private val memorizedOffers = mutable.Set.empty[TradeOffer]

  override protected def onlyNewOnes(tradeOffers: Set[TradeOffer]): Set[TradeOffer] =
    tradeOffers -- memorizedOffers

  override protected def addNewOnes(newTradeOffers: Set[TradeOffer]): Unit =
    memorizedOffers ++= newTradeOffers
}
