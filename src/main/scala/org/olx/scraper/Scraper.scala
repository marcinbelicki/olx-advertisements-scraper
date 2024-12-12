package org.olx.scraper

import net.ruippeixotog.scalascraper.browser.{Browser, JsoupBrowser}
import org.olx.scraper.models.TradeOffer

import java.net.URL

class Scraper(url: URL, browser: Browser) {

  private def foundDocument: browser.DocumentType = browser.get(url.toString)

  private def elements: Iterable[TradeOffer] = for {
    root <- foundDocument.body.select("[id=hydrate-root]")
    mainContent <- root.select("[id=mainContent]")
    divOne <- mainContent.children
    divTwo <- divOne.children
    divClass <- divTwo.select("[class=css-1nvt13t]")
    divAction <- divClass.select("[data-testid=listing-filters-form]")
    almostListingGrid <- divAction.select("[class=css-1d90tha]")
    listingGridContainer <- almostListingGrid.select(
      "[class=listing-grid-container css-d4ctjd]"
    )
    listingGrid <- listingGridContainer
      .select("[data-testid=listing-grid]")
      .headOption
      .toList
    tradeOfferElement <- listingGrid.select("[data-testid=l-card]")
  } yield TradeOffer(tradeOfferElement)

  def elementsSet: Set[TradeOffer] = elements.toSet
}

object Scraper {

  private val browser = JsoupBrowser()

  def apply(url: URL) = new Scraper(url, browser)

}
