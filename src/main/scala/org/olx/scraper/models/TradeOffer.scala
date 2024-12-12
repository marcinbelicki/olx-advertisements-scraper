package org.olx.scraper.models

import net.ruippeixotog.scalascraper.browser.Browser

import java.net.URL

case class TradeOffer(
    string: String
) {
  lazy val url: URL = new URL(string)
}

object TradeOffer {
  private def urlFromHref(href: String): String = {
    if (href.startsWith("/d/oferta/")) s"https://www.olx.pl$href"
    else href
  }

  def apply(element: Browser#DocumentType#ElementType): TradeOffer = {
    val Seq(href) = element.select("a").map(_.attr("href")).toSeq.distinct
    apply(
      string = urlFromHref(href)
    )
  }
}
