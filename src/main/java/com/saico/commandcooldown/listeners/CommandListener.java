package com.saico.commandcooldown.listeners;

import com.saico.commandcooldown.CommandCooldown;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandListener implements Listener {

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		if (event.isCancelled()) {
			return;
		}

		Player player = event.getPlayer();
		if (player.hasPermission("cmdcooldown.override")) {
			return;
		}

		int wait = CommandCooldown.getWait(player.getDisplayName(), event.getMessage());
		if (wait > 0) {
			player.sendMessage(ChatColor.RED + "Please wait " + ChatColor.YELLOW + wait + " seconds!");
			event.setCancelled(true);
		}
	}
}
