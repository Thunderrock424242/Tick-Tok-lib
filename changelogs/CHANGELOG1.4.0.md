# Changelog

## [Released]
### Highlights
- Added a `/ticktok` helper command with `now` and `convert` subcommands so admins can inspect the current day time/phase and convert tick values in-game.
- Expanded tick/time helpers with long/double inputs, new clock formatting options, countdown callbacks, and time-of-day phase utilities.
- Added an optional tick-optimization mode (enabled by default) that only samples world time once per second to reduce per-tick overhead.
- Restored the HUD overlay to the classic top-left positioning with stacked entries when holding a clock.

### Server/Admin actions
- Use the new tracing toggles (`enable_trace_conversions`, `enable_trace_formatting`, `enable_trace_events`) to narrow down diagnostics instead of enabling global debug logging.
- Grant permission level 2 to staff who should access the new `/ticktok` diagnostics.
