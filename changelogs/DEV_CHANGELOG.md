# Developer Changelog

## [Unreleased]
### API additions
- `TickTokAPI` exposes long/double overloads for tick conversion, locale-aware `formatClock`/`formatLocalized`, phase helpers (`currentPhase`, `ticksUntilPhase`, `ticksSincePhaseStart`), and countdown factories that accept `Runnable` or `Consumer` callbacks.
- `TickTokTimeBuilder.durationLong` returns long durations for large composite times; `TickTokFormatter` now supports 12h/24h patterns and localized clocks.

### Events and lifecycle
- `TickTokPhaseTracker` dispatches `TickTokPhaseEvent.Start`/`End` transitions each level tick via `TickTok.onLevelTick`, keyed per-dimension.
- New `/ticktok` command (permission level 2) offers `now` phase reporting and `convert <ticks>` formatting for in-game diagnostics.
- Tick-phase tracking respects a new `enable_tick_optimization` flag to sample world time once per second instead of every tick for lighter performance.

### Configuration and HUD
- Debugging options are split into targeted trace toggles: conversions, formatting, and events alongside the existing debug toggle.

### Migration notes
- Listeners expecting phase lifecycle should subscribe to the new start/end events instead of computing phases manually.
