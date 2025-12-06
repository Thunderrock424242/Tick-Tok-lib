# Changelog

## [Unreleased]
### Highlights
- Added a `/ticktok` helper command with `now` and `convert` subcommands so admins can inspect the current day time/phase and convert tick values in-game.
- HUD clock overlays now respect explicit corner selections (top/bottom + left/right) and accept only the supported enum values to avoid misconfiguration.
- Expanded tick/time helpers with long/double inputs, new clock formatting options, countdown callbacks, and time-of-day phase utilities.
- Added an optional tick-optimization mode (enabled by default) that only samples world time once per second to reduce per-tick overhead.

### Server/Admin actions
- Verify `game_time_position` and `local_time_position` config values use the enum-friendly names (`TOP_LEFT`, `TOP_RIGHT`, `BOTTOM_LEFT`, `BOTTOM_RIGHT`). Legacy string values may be rewritten when the config regenerates.
- Use the new tracing toggles (`enable_trace_conversions`, `enable_trace_formatting`, `enable_trace_events`) to narrow down diagnostics instead of enabling global debug logging.
- Grant permission level 2 to staff who should access the new `/ticktok` diagnostics.
