package net.TouhouMC.noteumaple.Chatsound.listener;

import java.io.File;

import net.TouhouMC.noteumaple.Chatsound.Chatsound;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
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
        String soundfullname;
		String soundcutname;
        if (!pl.hasPermission("chatsound.speaker")) return;
		int volume;
		int pitch;
		if (conf.getInt("user." + pl.getUniqueId() + ".volume") != 0) {
            soundcutname = conf.getString("user." + pl.getUniqueId() + ".sound");
            soundfullname = conf.getString("sounds." + soundcutname);
            if (soundfullname == null) return;
            volume = conf.getInt("user." + pl.getUniqueId() + ".volume");
            pitch = conf.getInt("user." + pl.getUniqueId() + ".pitch");
            if (Sound.valueOf((String)soundfullname.toUpperCase()) == null) return;
            for (Player lpl : Bukkit.getServer().getOnlinePlayers()) {
                if (!lpl.hasPermission("chatsound.listen")) continue;
                lpl.playSound(lpl.getLocation(), Sound.valueOf((String)soundfullname.toUpperCase()), (float)volume, (float)pitch);
            }
            return;
        } 
		ConfigurationSection groups = conf.getConfigurationSection("group");
			for ( String group1 : groups.getKeys(false)) {
				ConfigurationSection group = groups.getConfigurationSection(group1);
                if (group.getString("sound") == null || !pl.hasPermission("chatsound.group." + group1.toLowerCase())) continue;
                soundcutname = group.getString("sound");
                soundfullname = conf.getString("sounds." + soundcutname);
                if (soundfullname == null) continue;
                volume = group.getInt("volume");
                pitch = group.getInt("pitch");
                if (Sound.valueOf((String)soundfullname.toUpperCase()) == null) continue;
                for (Player lpl : Bukkit.getServer().getOnlinePlayers()) {
                        if (!lpl.hasPermission("chatsound.listen")) continue;
                        lpl.playSound(lpl.getLocation(), Sound.valueOf((String)soundfullname.toUpperCase()), (float)volume, (float)pitch);
                }
            }
    }
}