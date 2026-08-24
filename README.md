# Ktor HttpClient Reuse Companion

Warning icon on a Ktor `HttpClient(...)` constructor call built inside
a regular function body — Ktor's own documentation states plainly:
"creating HttpClient is not a cheap operation, and it's better to
reuse its instance in the case of multiple requests." Building one
inside a regular function means a brand new client (and its own
connection pool/engine) gets created on every call.

## Why it exists

`val client = HttpClient(CIO)` compiles fine and works — call it once
per request handler and every single call quietly spins up a new
engine and connection pool, instead of reusing the one client instance
the application already has.

## Why built this way

- **100% static text/PSI analysis** — matches the callee name by
  simple text, so it works whether the real Ktor client jar is on the
  classpath or not. Kotlin only (Ktor has no Java equivalent).
- **Confirmed gap**: JetBrains' own bundled Ktor plugin has only 2 real
  inspections, both on YAML config files (`KtorYamlConfig`,
  `KtorOpenApiUpdateInspection`) — confirmed by extracting and reading
  the plugin's own `plugin.xml` directly, not just its documentation.
  Neither covers this code-level construction pattern.

## v0.1 scope — stated honestly, not exhaustively

Only flags a direct `HttpClient(...)` call (with or without a
trailing config lambda). Never flags a call inside a constructor or a
class/top-level property initializer (legitimate "create once"
locations). Matches by simple callee name, not real type resolution —
an unrelated function also named `HttpClient` is a possible (rare)
false positive.

## Usage

Open any Kotlin file using Ktor's client. An `HttpClient(...)` built
inside a regular function shows a warning icon.

## Enterprise / Team Licensing

Need enterprise features, custom rules, or team licensing? Contact us at
**gaphunterlabs@gmail.com**.

## Development

```
./gradlew test           # unit tests
./gradlew buildPlugin    # generates build/distributions/*.zip
./gradlew verifyPlugin   # checks compatibility against real IDEs
```

## License

Apache-2.0. See `LICENSE`.
