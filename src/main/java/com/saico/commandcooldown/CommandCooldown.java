package com.saico.commandcooldown;

import com.saico.commandcooldown.listeners.CommandListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class CommandCooldown extends JavaPlugin {
	private static Map<String, Integer> cache = new HashMap<>();    //player,command --> expiry time
	private static Map<String, Integer> commands = new HashMap<>(); //command --> cooldown length

	public void onEnable() {
		// get config
		File config_file = new File(getDataFolder(), "config.yml");
		if (!config_file.exists()) {
			getConfig().options().copyDefaults(true);
			saveConfig();
		}
		for (String s : getConfig().getStringList("commands")) {
			try {
				String[] p = s.split(",");
				commands.put(p[0], Integer.parseInt(p[1]));
			} catch (Exception e) {
				getLogger().info("invalid config: " + s);
			}
		}

		//register listener
		getServer().getPluginManager().registerEvents(new CommandListener(), this);
	}

	private static int getTime() {
		return (int) (System.currentTimeMillis() / 1000L);
	}

	private static int getCooldown(String command) {
		for (String k : commands.keySet()) {
			if (command.startsWith(k)) {
				return commands.get(k);
			}
		}
		return 0;
	}

	public static int getWait(String player, String command) {
		int cooldown = getCooldown(command);
		if (cooldown == 0) {
			return 0;
		}

		String key = player + ',' + command;
		Integer t = cache.get(key);
		if (t != null) {
			int diff = t - getTime();
			if (diff > 0) {
				return diff;
			} else {
				cache.remove(key);
			}
		}
		cache.put(key, getTime() + cooldown);
		return 0;
	}

}
