package me.SuperRonanCraft.BetterRTP.versions;

import io.papermc.lib.PaperLib;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

/**
 * Version-safe async teleport bridge.
 *
 * Player#teleportAsync is used on modern Paper/Folia servers. Reflection keeps
 * the plugin binary-compatible with the legacy 1.8 API used for compilation.
 */
public final class TeleportHandler {

    private static Method teleportAsyncMethod;

    private TeleportHandler() {}

    public static CompletableFuture<Boolean> teleportAsync(Player player, Location location) {
        try {
            if (teleportAsyncMethod == null)
                teleportAsyncMethod = Player.class.getMethod("teleportAsync", Location.class);

            Object value = teleportAsyncMethod.invoke(player, location);
            if (value instanceof CompletableFuture) {
                @SuppressWarnings("unchecked")
                CompletableFuture<Boolean> future = (CompletableFuture<Boolean>) value;
                return future;
            }
        } catch (ReflectiveOperationException ignored) {
            // Legacy server; use PaperLib.
        } catch (RuntimeException ignored) {
            // Platform rejected the native call; use the compatibility fallback.
        }
        return PaperLib.teleportAsync(player, location);
    }
}
