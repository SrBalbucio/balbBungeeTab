package me.balbucio.tab.listener;

import me.balbucio.tab.Main;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerListener implements Listener{
	
	@EventHandler
	public void onJoin(PostLoginEvent evt) {
		ProxiedPlayer player = evt.getPlayer();
		Main.getInstance().update(player);
	}
	
	@EventHandler
	public void onSwitch(ServerSwitchEvent evt) {
		ProxiedPlayer player = evt.getPlayer();
		Main.getInstance().update(player);
	}
}
