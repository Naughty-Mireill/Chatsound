package net.TouhouMC.noteumaple.Chatsound;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import net.TouhouMC.noteumaple.Chatsound.listener.EventActionListener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class Chatsound extends JavaPlugin implements Listener {
    public static String chatsound_pre;
    public static FileConfiguration conf;
    public static File configfile;
    public static Logger logger;
    public static PluginDescriptionFile pdfFile;
    public static Chatsound plugin;
    public static Plugin plugin0;
    private static File pluginDir;

    static {
        logger = Logger.getLogger("Minecraft");
        chatsound_pre = ChatColor.WHITE + "[" + ChatColor.DARK_AQUA + "Chatsound" + ChatColor.WHITE + "]";
        pluginDir = new File("plugins", "Chatsound");
        configfile = new File(pluginDir, "config.yml");
        conf = YamlConfiguration.loadConfiguration(configfile);
    }

    public void onDisable() {
        logger.info("[chatsound] Plugin Successfully Disabled!");
        SaveTMCConfig();
    }

    public void onEnable() {
        pdfFile = getDescription();
        logger.info("[chatsound]" + pdfFile.getVersion() + "\u306f\u6b63\u3057\u304f\u8d77\u52d5\u3057\u307e\u3057\u305f");
        getServer().getPluginManager().registerEvents(this, this);
        saveDefaultConfig();
        registerEventListener();
        plugin0 = this;
    }

    @SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String commandlabel, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (cmd.getName().equalsIgnoreCase("chatsound")) {
                Object sound;
				if (args.length == 0) {
                    p.sendMessage(chatsound_pre + "Version " + pdfFile.getVersion() + ". Made by:" + pdfFile.getAuthors().toString());
                    return true;
                } else if (args[0].equalsIgnoreCase("help")) {
                    if (p.hasPermission("chatsound.help") || p.hasPermission("chatsound.user")) {
                        sender.sendMessage(chatsound_pre + ChatColor.GOLD + "\u53ef\u80fd\u306a\u30b3\u30de\u30f3\u30c9\u4e00\u89a7");
                        sender.sendMessage(chatsound_pre + ChatColor.AQUA + "chatsound : \u30d0\u30fc\u30b8\u30e7\u30f3\u8aac\u660e");
                        sender.sendMessage(chatsound_pre + ChatColor.AQUA + "chatsound usersound <playername> <soundname> [volume] [pitch]: \u30d7\u30ec\u30a4\u30e4\u30fc\u306e\u767a\u3059\u308b\u30c1\u30e3\u30c3\u30c8\u97f3\u3092\u8a2d\u5b9a\u3059\u308b");
                        sender.sendMessage(chatsound_pre + ChatColor.AQUA + "chatsound groupsound <groupname> <soundname> [volume] [pitch]: \u30b0\u30eb\u30fc\u30d7\u306e\u767a\u3059\u308b\u30c1\u30e3\u30c3\u30c8\u97f3\u3092\u8a2d\u5b9a\u3059\u308b");
                        return true;
                    }
                    p.sendMessage(chatsound_pre + ChatColor.RED + "\u6a29\u9650\u304c\u3042\u308a\u307e\u305b\u3093\uff01");
                    return false;
                } else if (args[0].equalsIgnoreCase("usersound")) {
                    if (args.length < 3) {
                        p.sendMessage(chatsound_pre + ChatColor.RED + "/chatsound usersound <playername> <soundname> [volume] [pitch]");
                        return false;
                    } else if (!p.hasPermission("chatsound.user")) {
                        p.sendMessage(chatsound_pre + ChatColor.RED + "\u6a29\u9650\u304c\u3042\u308a\u307e\u305b\u3093\uff01");
                        return false;
                    } else if (Bukkit.getOfflinePlayer(args[1]) != null) {
                        sound = null;
                        for (String soundname : conf.getConfigurationSection("sounds").getKeys(false)) {
                            if (soundname.equalsIgnoreCase(args[2])) {
                                sound = soundname;
                                break;
                            }
                        }
                        if (sound != null) {
                            conf.set("user." + Bukkit.getOfflinePlayer(args[1]).getUniqueId() + ".sound", sound);
                            conf.set("user." + Bukkit.getOfflinePlayer(args[1]).getUniqueId() + ".volume", Integer.valueOf(0));
                            conf.set("user." + Bukkit.getOfflinePlayer(args[1]).getUniqueId() + ".pitch", Integer.valueOf(0));
                            if (args.length >= 4) {
                                conf.set("user." + Bukkit.getOfflinePlayer(args[1]).getUniqueId() + ".volume", Integer.valueOf(Integer.parseInt(args[3])));
                                if (args.length >= 5) {
                                    conf.set("user." + Bukkit.getOfflinePlayer(args[1]).getUniqueId() + ".pitch", Integer.valueOf(Integer.parseInt(args[4])));
                                }
                            }
                            p.sendMessage(chatsound_pre + ChatColor.AQUA + "\u30e6\u30fc\u30b6\u30fc\u30c1\u30e3\u30c3\u30c8\u97f3\u3092\u8a2d\u5b9a\u3057\u307e\u3057\u305f");
                            SaveTMCConfig();
                        } else {
                            p.sendMessage(chatsound_pre + ChatColor.RED + "\u97f3\u306e\u30b7\u30e7\u30fc\u30c8\u30ab\u30c3\u30c8\u304c\u898b\u3064\u304b\u308a\u307e\u305b\u3093\u3067\u3057\u305f\u3002");
                            return false;
                        }
                    } else {
                        p.sendMessage(chatsound_pre + ChatColor.RED + "\u305d\u306e\u30d7\u30ec\u30a4\u30e4\u30fc\u306f\u53c2\u52a0\u3057\u305f\u3053\u3068\u304c\u3042\u308a\u307e\u305b\u3093\u3002");
                        return false;
                    }
                } else if (args[0].equalsIgnoreCase("groupsound")) {
                    if (args.length < 3) {
                        p.sendMessage(chatsound_pre + ChatColor.RED + "/chatsound groupsound <groupname> <soundname> [volume] [pitch]");
                        return false;
                    } else if (p.hasPermission("chatsound.group")) {
                        sound = null;
                        for (String soundname2 : conf.getConfigurationSection("sounds").getKeys(false)) {
                            if (soundname2.equalsIgnoreCase(args[2])) {
                                sound = soundname2;
                                break;
                            }
                        }
                        if (sound != null) {
                            conf.set("group." + args[1] + ".sound", sound);
                            conf.set("group." + args[1] + ".volume", Integer.valueOf(0));
                            conf.set("group." + args[1] + ".pitch", Integer.valueOf(0));
                            if (args.length >= 4) {
                                conf.set("group." + args[1] + ".volume", Integer.valueOf(Integer.parseInt(args[3])));
                                if (args.length >= 5) {
                                    conf.set("group." + args[1] + ".pitch", Integer.valueOf(Integer.parseInt(args[4])));
                                }
                            }
                            p.sendMessage(chatsound_pre + ChatColor.AQUA + "\u30b0\u30eb\u30fc\u30d7\u30c1\u30e3\u30c3\u30c8\u97f3\u3092\u8a2d\u5b9a\u3057\u307e\u3057\u305f");
                            SaveTMCConfig();
                        } else {
                            p.sendMessage(chatsound_pre + ChatColor.RED + "\u97f3\u306e\u30b7\u30e7\u30fc\u30c8\u30ab\u30c3\u30c8\u304c\u898b\u3064\u304b\u308a\u307e\u305b\u3093\u3067\u3057\u305f\u3002");
                            return false;
                        }
                    } else {
                        p.sendMessage(chatsound_pre + ChatColor.RED + "\u6a29\u9650\u304c\u3042\u308a\u307e\u305b\u3093\uff01");
                        return false;
                    }
                } else if (args[0].equalsIgnoreCase("reload") && p.hasPermission("chatsound.reload")) {
                    reloadTMCConfig();
                    p.sendMessage(chatsound_pre + ChatColor.AQUA + "\u30ea\u30ed\u30fc\u30c9\u3057\u307e\u3057\u305f");
                }
            }
        }
        return false;
    }

    public void registerEventListener() {
        new EventActionListener(this);
    }

    public static void SaveTMCConfig() {
        try {
            conf.save(configfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void reloadTMCConfig() {
        configfile = new File(pluginDir, "config.yml");
        conf = YamlConfiguration.loadConfiguration(configfile);
    }
}