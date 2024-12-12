package org.olx.scraper.store

import org.olx.scraper.models.TradeOffer

trait OffersStore {

  def analyzeAndReturnNewOnes(tradeOffers: Set[TradeOffer]): Set[TradeOffer]

}
