package me.balbucio.tab;

import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import balbucio.checker.bungeecord.Checker;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.YamlConfiguration;
import me.balbucio.tab.listener.PlayerListener;

public class Main extends Plugin {
	private static Main instance;
	private File folder = new File("plugins/balbBungeeTab");
	private File config = new File("plugins/balbBungeeTab", "config.yml");
	private File msgs = new File("plugins/balbBungeeTab", "messages.yml");

	public Anuncio ad;
	private Checker checker = new Checker("balbBungeeTab", 1.3, this);

	public Configuration configuration, messages;

	public void onEnable() {
		setInstance(this);
		loadConfig();
		BungeeCord.getInstance().pluginManager.registerListener(this, new PlayerListener());
		BungeeCord.getInstance().pluginManager.registerCommand(this, new Comand());
		if(checker.hasNewVersion()) {
			if(configuration.getBoolean("autoupdate")) {
				checker.startUpdater();
			} else {
				BungeeCord.getInstance().getConsole().sendMessage(new TextComponent("[BalbucioBungeeTab] §aHá uma nova versão do plugin! Atualize em "+checker.getDetailsPage()));
			}
		}
		BungeeCord.getInstance().getConsole().sendMessage(new TextComponent("[BalbucioBungeeTab] §aAtivado com sucesso!"));
		BungeeCord.getInstance().getConsole().sendMessage(new TextComponent("[BalbucioBungeeTab] §aConheça outros plugins meus em: §fhttps://plugins.balbucio.xyz"));
		startScheduler();
	}

	public static Main getInstance() {
		return instance;
	}

	private static void setInstance(final Main instance) {
		Main.instance = instance;
	}

	public void loadConfig() {
		try {
			if (!folder.exists()) {
				folder.mkdir();
			}

			if (!config.exists()) {
				Files.copy(this.getResourceAsStream("config.yml"), config.toPath());
			}
			if (!msgs.exists()) {
				Files.copy(this.getResourceAsStream("messages.yml"), msgs.toPath());
			}
			configuration = YamlConfiguration.getProvider(YamlConfiguration.class).load(config);
			messages = YamlConfiguration.getProvider(YamlConfiguration.class).load(msgs);
		} catch (Exception e) {
			e.printStackTrace();
			BungeeCord.getInstance().getConsole()
					.sendMessage("§c[BalbucioBungeeTab] §aNão foi possível carregar os arquivos!");
		}
	}

	@SuppressWarnings("deprecation")
	public void anunciar(String anuncio, String player) {
		if(anuncio == null) { ad = null; return; }
		
		ad = new Anuncio(player, anuncio.replace("&", "§"));

		if (configuration.getBoolean("alertar")) {
			getLogger().info(messages.getString("adaviso").replace("&", "§").replace("{player}", player)
					.replace("{anuncio}", anuncio.replace("&", "§")));

			for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
				update(all);
				if (all.hasPermission("btab.view")) {
					all.sendMessage(messages.getString("prefix").replace("&", "§") + " "
							+ messages.getString("adaviso").replace("&", "§").replace("{player}", player)
									.replace("{anuncio}", anuncio.replace("&", "§")));
				}
			}
		}
	}

	public void update(ProxiedPlayer player) {
		if(player.getServer() == null) { return; }
		if(!allow(player.getServer().getInfo().getName())) { return; }
		
		player.resetTabHeader();
		
		boolean hasAd = ad != null;
		String server = player.getServer().getInfo().getName();
		String anuncio = hasAd
				? messages.getString("tab.anuncio").replace("&", "§").replace("{anuncio}", ad.getAnuncio())
						.replace("{anuncio_owner}", ad.getOwner())
				: messages.getString("tab.semanuncio").replace("&", "§").replace("{remover}", "");
		String owner = hasAd ? ad.getOwner() : messages.getString("tab.semowner").replace("&", "§");

		String pattern = "MM-dd-yyyy";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String date = simpleDateFormat.format(new Date());

		String hpattern = "HH:mm:ss";
		SimpleDateFormat hsimpleDateFormat = new SimpleDateFormat(hpattern);
		String hour = hsimpleDateFormat.format(new Date());

		TextComponent tabCima = new TextComponent(messages.getString("tab.tabCima").replace("&", "§")
				.replace("{anuncio}", anuncio).replace("{anuncio_owner}", owner).replace("{server}", server)
				.replace("{online}", String.valueOf(ProxyServer.getInstance().getOnlineCount())).replace("{date}", date)
				.replace("{hour}", hour));
		TextComponent tabBaixo = new TextComponent(messages.getString("tab.tabBaixo").replace("&", "§")
				.replace("{anuncio}", anuncio).replace("{anuncio_owner}", owner).replace("{server}", server)
				.replace("{online}", String.valueOf(ProxyServer.getInstance().getOnlineCount())).replace("{date}", date)
				.replace("{hour}", hour));

		// Seta o Tab
		player.setTabHeader(tabCima, tabBaixo);
	}
	
	public boolean allow(String server) {
		if (configuration.getBoolean("whitelist") && !configuration.getStringList("serverlist").contains(server)) {
			return false;
		}
		if(!configuration.getBoolean("whitelist") && configuration.getStringList("serverlist").contains(server)) {
			return false;
		}
		return true;
	}
	
	private void startScheduler() {
		ProxyServer.getInstance().getScheduler().schedule(this, new Runnable() {
			
			@Override
			public void run() {
				for(ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
					update(player);
				}
			}
		}, 0, configuration.getInt("atualizartab"), TimeUnit.SECONDS);
	}
}
