package org.olx.scraper

import org.scalatest.matchers.should.Matchers.empty
import org.scalatest.matchers.should.Matchers.not
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.scalatest.wordspec.AnyWordSpecLike

import java.net.URL
import scala.language.postfixOps

class LocalMemoryScraperTest extends AnyWordSpecLike {

  private val localMemoryScraper = new LocalMemoryScraper(Scraper(url = new URL("https://www.olx.pl/nieruchomosci/mieszkania/wynajem/wroclaw/?search%5Border%5D=created_at:desc&search%5Bfilter_float_price:from%5D=2500&search%5Bfilter_float_price:to%5D=3000&search%5Bfilter_float_m:from%5D=50&search%5Bfilter_enum_rooms%5D%5B0%5D=two&search%5Bfilter_enum_rooms%5D%5B1%5D=three")))

  "LocalMemoryScraper" must {
    "properly return new stuff when started" in {
      // when / then
      localMemoryScraper.refreshAndReturnNewOnes should not be empty

      // when / then
      localMemoryScraper.refreshAndReturnNewOnes shouldBe empty
    }
  }

}
