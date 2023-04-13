package me.balbucio.tab;

import java.io.IOException;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class Comand extends Command {
	public Comand() {
		super("btab", "btab.use", "bungeetab");
	}

	private Main m = Main.getInstance();

	@SuppressWarnings("deprecation")
	public void execute(CommandSender sender, String[] args) {
		String prefix = m.messages.getString("prefix").replace("&", "§");
		if (!sender.hasPermission("btab.use")) {
			if (m.configuration.getBoolean("nopermissionmessage")) {
				sender.sendMessage(m.messages.getString("nopermission"));
				return;
			}
		}

		if (args.length == 0) {
			sender.sendMessage("§a§lbalbBungeeTab v1.3 §7- Desenvolvido por Sr_Balbucio");
			return;
		}

		String arg = args[0];
		if (arg.equalsIgnoreCase("reload")) {
			m.loadConfig();
			sender.sendMessage("§aPlugin recarregado!");
		} else if (arg.equalsIgnoreCase("anunciar") || arg.equalsIgnoreCase("anuncio") || arg.equalsIgnoreCase("ad")) {
			if (!sender.hasPermission("btab.anuncio")) {
				sender.sendMessage(m.messages.getString("nopermission"));
				return;
			}
			if(!(args.length > 1)) {
				sender.sendMessage(m.messages.getString("mensagempequena").replace("&", "§").replace("{prefix}", prefix));
				return;
			}
			String message = "";
			for (int i = 1; i < args.length; i++) {
				if (message.equalsIgnoreCase("")) {
					message = args[i];
				} else {
					message = message + " " + args[i];
				}
			}
			m.anunciar(message, sender.getName());
			sender.sendMessage(m.messages.getString("anunciado").replace("&", "§").replace("{prefix}", prefix));
		} else if(arg.equalsIgnoreCase("remover")){
			m.anunciar(null, null);
			sender.sendMessage(m.messages.getString("anuncioremovido").replace("&", "§").replace("{prefix}", prefix));
		} else if(arg.equalsIgnoreCase("help") || arg.equalsIgnoreCase("ajuda")) {
			sender.sendMessage("§a§lbalbBungeeTab §7- Comandos:");
			sender.sendMessage("§7Criado por Sr_Balbucio! - v1.3");
			sender.sendMessage("§b/btab §7- Mostra a versão do plugin");
			sender.sendMessage("§b/btab anunciar §7- Cria um anúncio");
			sender.sendMessage("§b/btab remover §7- Remove um anúncio");
			sender.sendMessage("§b/btab reload §7- Recarrega o plugin");
		} else {
			sender.sendMessage(m.messages.getString("notfound").replace("&", "§").replace("{prefix}", prefix));
		}
	}

}
