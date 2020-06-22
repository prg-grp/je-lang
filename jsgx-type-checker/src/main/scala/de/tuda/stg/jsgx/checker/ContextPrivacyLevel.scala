package de.tuda.stg.jsgx.checker

sealed  abstract class ContextPrivacyLevel {
  case class Secret() extends ContextPrivacyLevel
  case class NonSecret() extends ContextPrivacyLevel
}
