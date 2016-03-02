package net.TouhouMC.noteumaple.Chatsound.listener;

import java.io.File;
import net.TouhouMC.noteumaple.Chatsound.Chatsound;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

public class EventActionListener
implements Listener {
    static String pluginpre = Chatsound.chatsound_pre;
    static File config = Chatsound.configfile;
    static FileConfiguration conf = Chatsound.conf;

    public EventActionListener(Chatsound plugin) {
        plugin.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)plugin);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @EventHandler
    public void soundChat(AsyncPlayerChatEvent e) {
        Player pl = e.getPlayer();
        if (!pl.hasPermission("chatsound.speaker")) return;
        if (conf.getString("user." + pl.getUniqueId() + ".sound") != null && conf.getString("user." + pl.getUniqueId() + ".sound") != "none") {
            String soundcutname = conf.getString("user." + pl.getUniqueId() + ".sound");
            String soundfullname = conf.getString("sounds." + soundcutname);
            if (soundfullname == null) return;
            int volume = conf.getInt("user." + pl.getUniqueId() + ".volume");
            int pitch = conf.getInt("user." + pl.getUniqueId() + ".pitch");
            if (Sound.valueOf((String)soundfullname.toUpperCase()) == null) return;
            for (Player listener : pl.getWorld().getPlayers()) {
                if (!listener.hasPermission("chatsound.listen")) continue;
                listener.playSound(listener.getLocation(), Sound.valueOf((String)soundfullname.toUpperCase()), (float)volume, (float)pitch);
            }
            return;
        } else {
            for (String group : conf.getConfigurationSection("group").getKeys(false)) {
                String soundfullname;
                if (conf.getString("group." + group + ".sound") == null || conf.getString("group." + group + ".sound") == "none" || !pl.hasPermission("chatsound.group." + group) || (soundfullname = conf.getString("sounds." + (conf.getString(new StringBuilder("group.").append(group).append(".sound").toString())))) == null) continue;
                int volume = conf.getInt("group." + pl.getUniqueId() + ".volume");
                int pitch = conf.getInt("group." + pl.getUniqueId() + ".pitch");
                if (Sound.valueOf((String)soundfullname.toUpperCase()) == null) continue;
                for (Player listener : pl.getWorld().getPlayers()) {
                    if (!listener.hasPermission("chatsound.listen")) continue;
                    listener.playSound(listener.getLocation(), Sound.valueOf((String)soundfullname.toUpperCase()), (float)volume, (float)pitch);
                }
            }
        }
    }
}