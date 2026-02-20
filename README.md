
Installation information
=======

This template repository can be directly cloned to get you started with a new
mod. Simply create a new repository cloned from this one, by following the
instructions provided by [GitHub](https://docs.github.com/en/repositories/creating-and-managing-repositories/creating-a-repository-from-a-template).

Once you have your clone, simply open the repository in the IDE of your choice. The usual recommendation for an IDE is either IntelliJ IDEA or Eclipse.

If at any point you are missing libraries in your IDE, or you've run into problems you can
run `gradlew --refresh-dependencies` to refresh the local cache. `gradlew clean` to reset everything 
{this does not affect your code} and then start the process again.

Mapping Names:
============
By default, the MDK is configured to use the official mapping names from Mojang for methods and fields 
in the Minecraft codebase. These names are covered by a specific license. All modders should be aware of this
license. For the latest license text, refer to the mapping file itself, or the reference copy here:
https://github.com/NeoForged/NeoForm/blob/main/Mojang.md

Additional Resources: 
==========
Community Documentation: https://docs.neoforged.net/  
NeoForged Discord: https://discord.neoforged.net/

Sub-Tick Scheduler
------------------
The library now exposes a small `SubTickScheduler` utility for running tasks at
fractions of a Minecraft tick. This lets you schedule work with half or quarter
 tick precision by delaying runnables using real time. Example:

```java
SubTickScheduler.scheduleHalfTick(() -> {
    // code to execute ~25ms later
});
```

Remember that scheduled tasks run on a separate thread; interact with the game
only from the main thread.

To execute sub-tick tasks back on the server thread, you can pass a
`MinecraftServer` instance:

```java
SubTickScheduler.scheduleOnServer(0.5, server, () -> {
    // runs about half a tick later on the main thread
});
```

Fast Water Flow Example
----------------------
Override the `getTickDelay` method in `WaterFluid` so flowing water updates
every tick. This pairs well with scheduling partial updates using
`SubTickScheduler`:

```java
@Mixin(WaterFluid.class)
public abstract class WaterFluidMixin {
    @Inject(method = "getTickDelay", at = @At("HEAD"), cancellable = true)
    private void fastTicks(LevelReader level, CallbackInfoReturnable<Integer> cir) {
        if (level instanceof Level world && !world.isClientSide()) {
            cir.setReturnValue(1);
        }
    }
}
```

Duration Formatting
------------------
TickTok now includes helpers for turning tick durations into short or long
human-friendly strings:

```java
String compact = TickTokFormatter.formatDurationShort(1250); // "1m 2s 500ms"
String verbose = TickTokFormatter.formatDurationLong(1250);  // "1 minute 2 seconds 500 milliseconds"
```

Need the raw breakdown instead? You can also pull a structured breakdown of ticks:

```java
TickTokDurationBreakdown breakdown = TickTokTimeUtils.breakdownTicks(1250);
long seconds = breakdown.seconds();
```

Cooldown Utility
---------------
Need a simple reusable cooldown timer? The `TickTokCooldown` helper tracks a
duration window and can format the remaining time:

```java
TickTokCooldown cooldown = new TickTokCooldown(200, level.getGameTime());
if (cooldown.isReady(level.getGameTime())) {
    cooldown.reset(level.getGameTime());
}
String remaining = cooldown.formatRemainingShort(level.getGameTime());
```

Overload Diagnostics
--------------------
When Minecraft logs the "Can't keep up" warning, TickTok now appends a quick
cause hint and, when detectable, a list of likely involved mod IDs.

You can turn this off in config with:

```toml
enable_overload_diagnostics = true
```
