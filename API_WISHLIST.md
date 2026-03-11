# TickTok Library / API Wishlist

For NeoForge mod development (Java + Gradle), these additions would make TickTok
more production-friendly and easier to integrate across mods.

## 1) Unified `ITickClock` abstraction
- Add an interface for reading time from game tick, sub-tick, and wall-clock sources.
- Include server/client-aware implementations.
- Helps unit testing by allowing fake clocks.

## 2) `TickTokTaskHandle` for cancellation and status
- Return a lightweight handle from schedulers (`cancel()`, `isDone()`, `remainingTicks()`).
- Include optional repeating tasks and fixed-rate/fixed-delay variants.

## 3) Lifecycle-safe scheduler integration
- Auto-cancel scheduled work on server stop, world unload, or player logout.
- Optional owner-bound tasks (e.g., bound to a `UUID`, `Level`, or `ServerPlayer`).

## 4) Event-driven timer callbacks
- Add NeoForge event bridge helpers (e.g., once-after-N-ticks, each N ticks).
- Expose a small DSL for common patterns:
  - `every(20).ticks(...)`
  - `after(5).seconds(...)`

## 5) Serialization support
- Built-in codecs/NBT serializers for cooldowns, countdowns, and phase trackers.
- This makes persistence in capabilities/data attachments straightforward.

## 6) Threading safety helpers
- Dedicated APIs that guarantee execution on the main server thread.
- Add guard methods to fail fast when called from the wrong thread.

## 7) Metrics + debug introspection
- Optional debug endpoint/log output listing active timers and queue size.
- Useful for profiling lag spikes or memory leaks from forgotten tasks.

## 8) Data-driven timing presets
- Load common timer definitions from JSON resources.
- Makes balancing easier without recompiling (great for pack/mod tuning).

## 9) Kotlin-friendly API surface (optional)
- Keep Java-first API, but provide null-safe builder/factory patterns that are pleasant in Kotlin mods.

## 10) Testkit module
- A tiny test utility package with fake clocks, deterministic scheduler stepping,
  and assertion helpers for timer-driven code.

---

## Suggested rollout order
1. `TaskHandle` + cancellation semantics
2. Lifecycle-safe ownership and auto-cleanup
3. Serialization codecs
4. Debug/metrics
5. Data-driven presets + testkit

That order provides immediate safety/ergonomic wins while keeping migration risk low.
