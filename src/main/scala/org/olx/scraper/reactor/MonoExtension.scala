package org.olx.scraper.reactor

import reactor.core.publisher.Mono

object MonoExtension {

  implicit class RichMono[A](mono: Mono[A]) {
    def onErrorResumePf(
        pf: PartialFunction[Throwable, _ <: Mono[_ <: A]]
    ): Mono[A] =
      mono.onErrorResume(
        pf isDefinedAt _,
        pf apply _
      )
  }
}
