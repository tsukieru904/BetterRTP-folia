package me.SuperRonanCraft.BetterRTP.player.rtp.effects;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.player.rtp.packets.WrapperPlayServerNamedSoundEffect;
import me.SuperRonanCraft.BetterRTP.references.file.FileOther;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;

public class RTPEffect_Sounds {

    private boolean enabled;
    private String soundTeleport, soundDelay;

    void load() {
        FileOther.FILETYPE config = FileOther.FILETYPE.EFFECTS;
        enabled = config.getBoolean("Sounds.Enabled");
        if (enabled) {
            soundTeleport = config.getString("Sounds.Success");
            soundDelay = config.getString("Sounds.Delay");
        }
    }

    public void playTeleport(Player p) {
        if (!enabled)
            return;
        if (soundTeleport != null) {
            playSound(p.getLocation(), p, soundTeleport);
            //p.playSound(p.getLocation(), soundTeleport, 1F, 1F);
        }
    }

    public void playDelay(Player p) {
        if (!enabled) return;
        if (soundDelay != null) {
            playSound(p.getLocation(), p, soundDelay);
            //p.playSound(p.getLocation(), soundDelay, 1F, 1F);
        }
    }

    void playSound(Location loc, Player p, String sound) {
        if (BetterRTP.getInstance().getSettings().isProtocolLibSounds()) {
            try {
                ProtocolManager pm = ProtocolLibrary.getProtocolManager();
                WrapperPlayServerNamedSoundEffect packet = new WrapperPlayServerNamedSoundEffect(pm.createPacket(PacketType.Play.Server.NAMED_SOUND_EFFECT));
                packet.setSoundName(sound);
                packet.setEffectPositionX(loc.getBlockX());
                packet.setEffectPositionY(loc.getBlockY());
                packet.setEffectPositionZ(loc.getBlockZ());
                packet.sendPacket(p);
            } catch (NoClassDefFoundError | Exception e) {
                BetterRTP.getInstance().getLogger().severe("ProtocolLib Sounds is enabled in the effects.yml file, but no ProtocolLib plugin was found!");
                if (!playConfiguredSound(p, sound))
                    BetterRTP.getInstance().getLogger().warning("The sound '" + sound + "' is unavailable on this server.");
            }
        } else {
            if (!playConfiguredSound(p, sound))
                BetterRTP.getInstance().getLogger().warning(
                        "The sound '" + sound + "' is unavailable on this server.");
        }
    }

    private boolean playConfiguredSound(Player p, String sound) {
        if (sound == null || sound.trim().isEmpty())
            return false;

        try {
            // Use the resource-key String overload on modern versions. This avoids
            // hard dependency on a Sound enum constant that changed in 1.21+.
            Method method = Player.class.getMethod(
                    "playSound", Location.class, String.class, float.class, float.class);
            method.invoke(p, p.getLocation(), sound, 1F, 1F);
            return true;
        } catch (NoSuchMethodException ignored) {
            try {
                Sound legacySound = Sound.valueOf(sound.toUpperCase());
                p.playSound(p.getLocation(), legacySound, 1F, 1F);
                return true;
            } catch (IllegalArgumentException ignoredLegacy) {
                return false;
            } catch (ReflectiveOperationException ignoredReflection) {
                return false;
            }
        } catch (ReflectiveOperationException e) {
            return false;
        }
    }
}
