# Changelog

## [Released]
### Highlights
- Added Java-time helpers (`ticksToDuration`, `durationToTicks`, `timeUnitToTicks`, `sleepTicksAsync`, `sleepTicksOnServer`) so mods can bridge TickTok timings with `Duration`, `TimeUnit`, and async scheduling via the public API.  
- Introduced phase scheduling utilities (`scheduleAtPhase`, level-aware scheduling, and `phaseBarrier`) to run callbacks precisely when dawn/day/dusk/night begin.  
- Added repeating timer scheduling helpers and a default timer accessor (`TickTokHelper.timer`) for "every X ticks/time do this" gameplay loops.  
- Reworked the server's "Can't keep up" warning to report ticks, seconds, and milliseconds behind using TickTok's lag formatter for clearer diagnostics.  
- Optimized the HUD clock overlay by caching formatted game/local time once per tick/second and skipping redundant renders when the player isn't holding a clock.  
- Bundled a logo in the mod metadata so TickTokLib displays with branding in the NeoForge mod list.  
