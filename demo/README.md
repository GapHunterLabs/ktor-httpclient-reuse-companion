# Demo data — Ktor HttpClient Reuse Companion

For capturing the real Marketplace screenshot:

1. `./gradlew runIde`
2. Open `demo/ApiService.kt` **inside a real Kotlin project/module** in
   the sandbox IDE (a bare scratch/standalone file shows as
   highlighting "OFF" and the Kotlin PSI never resolves, so the marker
   won't appear — confirmed live, not a plugin bug). Easiest: create a
   throwaway Kotlin project via New Project, then paste this file's
   content into a new `.kt` file in it.
3. The `HttpClient(CIO)` call inside `fetchOrdersUnsafely` shows the
   warning — hover it for the tooltip. `fetchOrdersSafely`'s
   constructor-assigned instance stays clean, for contrast.
4. Enter Full Screen (`View > Appearance > Enter Full Screen`), capture
   with `Win+Shift+S`, save directly to `docs/screenshots/` in this
   repo.
