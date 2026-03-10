# Changelog

## [Released]
### Highlights
- Improved tick phase tracking internals so phase transitions stay accurate while reducing unnecessary work during server ticks.
- Hardened repeating timer execution to better isolate task failures and keep scheduler loops running reliably.
- Expanded overloaded-server diagnostics to append clearer "Can't keep up" cause hints and likely involved mod IDs when they can be detected.

### Server/Admin actions
- Keep `enable_overload_diagnostics = true` if you want enhanced overload hinting in server logs; disable it to fall back to vanilla-style warnings.
- No API-breaking migration is required for existing TickTok timer or phase scheduling integrations.
