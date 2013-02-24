package com.craftminecraft.bukkit.tablistheader;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcsg.double0negative.tabapi.TabAPI;

public final class SrvTitleTabList extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        // Save a copy of the default config.yml if one is not there
        this.saveDefaultConfig();
        this.getServer().getPluginManager().registerEvents(this, this);
        updateAll();
    }

    @Override
    public void onDisable() {
        
    }
    
    public void updateAll() {
        for (Player player : this.getServer().getOnlinePlayers())
        	updateTabList(player);
    }
    
    public void updateTabList(Player player) {
        TabAPI.setPriority(this, player, 2);
    	int size = this.getConfig().getList("header").size();
    	String antishit = "";
    	for (int i=0;i<size;i++) {
    		if (!(this.getConfig().getList("header").get(i) instanceof List)) {
    			this.getLogger().warning(String.format("header.%d is not a list. Your config is wrong.", i));
    			continue;
    		}
    		List<String> stuff = (List<String>) this.getConfig().getList("header").get(i);
    		if (stuff.size() != 3) {
    			this.getLogger().warning(String.format("header.%d does not contain three entries. Your config is wrong.", i));
    			continue;
    		}
    		antishit = ChatColor.translateAlternateColorCodes('&', stuff.get(0));
    		TabAPI.setTabString(this, player, i, 0, ChatColor.translateAlternateColorCodes('&', stuff.get(0)));
    		TabAPI.setTabString(this, player, i, 1, ChatColor.translateAlternateColorCodes('&', stuff.get(1)));
    		TabAPI.setTabString(this, player, i, 2, ChatColor.translateAlternateColorCodes('&', stuff.get(2)));
    	}
    	int column = 0;
    	for (Player entry : this.getServer().getOnlinePlayers()) {
    		TabAPI.setTabString(this, player, size, column, entry.getName());
    		antishit = entry.getName();
    		column = (column+1)%3;
    		if (column == 0) {
    			size = size+1;
    		}
    	}
    	while (column != 0) {
    		TabAPI.setTabString(this, player, size, column, antishit);
    		column = (column+1)%3;
    	}
    	TabAPI.updatePlayer(player);
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent ev) {
    	// wait a few ticks so the player really is online ?
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
			public void run(){
				updateAll();
			}
		}, 3);
	}
    
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent ev) {
    	updateAll();
    }
}
