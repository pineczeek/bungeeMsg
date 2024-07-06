package pl.pineczek.pineczekmsg;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import pl.pineczek.pineczekmsg.commands.IgnoreCommand;
import pl.pineczek.pineczekmsg.commands.MsgCommand;
import pl.pineczek.pineczekmsg.commands.ReplyCommand;
import pl.pineczek.pineczekmsg.commands.UnIgnoreCommand;
import pl.pineczek.pineczekmsg.managers.ConfigManager;
import pl.pineczek.pineczekmsg.managers.MsgManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public final class Main extends Plugin {

		static Main instance;

    @Override
    public void onEnable() {

				// TODO: zrobic aby ignore cos robilo fr

				instance = this;

				try {
						makeConfig();
						ConfigManager.loadConfig();
				} catch (IOException e) {
						throw new RuntimeException(e);
				}

				Configuration configuration;

				try {
						configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "data.yml"));
				} catch (IOException e) {
						throw new RuntimeException(e);
				}

				Collection<String> keys = configuration.getKeys();

				for (String key : keys) {
						List<String> uuids = configuration.getStringList(key);
						List<UUID> uuidsReal = new ArrayList<>();
						for (String s : uuids) {
								uuidsReal.add(UUID.fromString(s));
						}
						if (!uuids.isEmpty()) {
								MsgManager.ignoreList.put(UUID.fromString(key), uuidsReal);
						}
				}

				String lang = ConfigManager.getConfig().getString("lang");

				if (lang.equals("pl")) {
						System.out.println("=========================");
						System.out.println("=");
						System.out.println("= Zaladowano pineczekMSG!");
						System.out.println("=");
						System.out.println("= Mozesz zmienic jezyk w config.yml");
						System.out.println("= Aktualny jezyk: " + lang);
						System.out.println("=");
						System.out.println("=========================");
				} else {
						System.out.println("=========================");
						System.out.println("=");
						System.out.println("= Loaded pineczekMSG!");
						System.out.println("=");
						System.out.println("= You can change language at config.yml");
						System.out.println("= Actual Language: " + lang);
						System.out.println("=");
						System.out.println("=========================");
				}

				ProxyServer.getInstance().getPluginManager().registerCommand(this, new MsgCommand("msg"));
				ProxyServer.getInstance().getPluginManager().registerCommand(this, new ReplyCommand("r"));
				ProxyServer.getInstance().getPluginManager().registerCommand(this, new ReplyCommand("reply"));
				ProxyServer.getInstance().getPluginManager().registerCommand(this, new IgnoreCommand("ignore"));
				ProxyServer.getInstance().getPluginManager().registerCommand(this, new UnIgnoreCommand("unignore"));

    }

    @Override
    public void onDisable() {

				Configuration configuration;

				try {
						configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "data.yml"));
				} catch (IOException e) {
						throw new RuntimeException(e);
				}

				for (Map.Entry<UUID, List<UUID>> map : MsgManager.ignoreList.entrySet()) {
						configuration.set(map.getKey().toString(), map.getValue());
				}

				try {
						ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, new File(getDataFolder(), "data.yml"));
				} catch (IOException e) {
						throw new RuntimeException(e);
				}

		}

		public static String c(String s) {
				return ChatColor.translateAlternateColorCodes('&', s);
		}

		public void makeConfig() throws IOException {
				if (!getDataFolder().exists()) {
						getDataFolder().mkdir();
				}

				File configFile = new File(getDataFolder(), "config.yml");
				File plMessagesFile = new File(getDataFolder(), "messages-pl.yml");
				File engMessagesFile = new File(getDataFolder(), "messages-eng.yml");
				File dataFile = new File(getDataFolder(), "data.yml");

				if (!configFile.exists()) {
						FileOutputStream outputStream = new FileOutputStream(configFile);
						InputStream in = getResourceAsStream("config.yml");
						in.transferTo(outputStream);
				}
				if (!plMessagesFile.exists()) {
						FileOutputStream outputStream = new FileOutputStream(plMessagesFile);
						InputStream in = getResourceAsStream("messages-pl.yml");
						in.transferTo(outputStream);
				}
				if (!engMessagesFile.exists()) {
						FileOutputStream outputStream = new FileOutputStream(engMessagesFile);
						InputStream in = getResourceAsStream("messages-eng.yml");
						in.transferTo(outputStream);
				}
				if (!dataFile.exists()) {
						dataFile.createNewFile();
				}
		}

		public static Main getInstance() {
				return instance;
		}

}
