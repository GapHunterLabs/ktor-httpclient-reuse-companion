<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Ktor HttpClient Reuse Companion Changelog

## [Unreleased]

## [0.1.0]

### Added

- Warning icon on a Ktor HttpClient(...) built inside a regular
  function instead of created once and reused -- matching Ktor's own
  documentation: "creating HttpClient is not a cheap operation".
- 100% static text/PSI analysis, Kotlin only, no network calls, no
  telemetry. Free.

[Unreleased]: https://github.com/GapHunterLabs/ktor-httpclient-reuse-companion/compare/0.1.0...HEAD
[0.1.0]: https://github.com/GapHunterLabs/ktor-httpclient-reuse-companion/commits/0.1.0
