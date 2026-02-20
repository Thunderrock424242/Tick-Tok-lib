package com.thunder.ticktoklib.util;

import net.neoforged.fml.ModList;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Lightweight heuristics for attaching "what might be causing this" hints to
 * overload warnings.
 */
public class TickTokOverloadDiagnostics {

    private static final Set<String> FRAME_PREFIX_BLOCKLIST = Set.of(
            "java.",
            "javax.",
            "jdk.",
            "sun.",
            "org.slf4j.",
            "org.apache.logging.",
            "org.spongepowered.asm.",
            "net.minecraft.",
            "net.neoforged."
    );

    private TickTokOverloadDiagnostics() {
    }

    public static String buildCauseHint(long ticksBehind, long millisecondsBehind) {
        Set<String> suspectedMods = detectSuspectedModsFromCurrentThread();
        if (!suspectedMods.isEmpty()) {
            return "Likely involved mod(s): " + String.join(", ", suspectedMods);
        }

        if (ticksBehind >= 200 || millisecondsBehind >= 10_000) {
            return "Likely causes: chunk generation/loading spikes, heavy worldgen, or mass entity/block updates";
        }

        if (ticksBehind >= 40 || millisecondsBehind >= 2_000) {
            return "Likely causes: temporary chunk loading bursts, autosaves, or scripted/modded tick spikes";
        }

        return "Likely causes: brief IO or tick scheduling spike";
    }

    private static Set<String> detectSuspectedModsFromCurrentThread() {
        LinkedHashSet<String> hits = new LinkedHashSet<>();

        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        Arrays.stream(trace)
                .map(StackTraceElement::getClassName)
                .filter(TickTokOverloadDiagnostics::isCandidateClass)
                .map(TickTokOverloadDiagnostics::toPotentialModId)
                .filter(modId -> ModList.get().isLoaded(modId))
                .limit(3)
                .forEach(hits::add);

        return hits;
    }

    private static boolean isCandidateClass(String className) {
        for (String blockedPrefix : FRAME_PREFIX_BLOCKLIST) {
            if (className.startsWith(blockedPrefix)) {
                return false;
            }
        }

        return className.contains(".");
    }

    private static String toPotentialModId(String className) {
        String rootPackage = className.substring(0, className.indexOf('.'));
        return rootPackage.toLowerCase();
    }
}
