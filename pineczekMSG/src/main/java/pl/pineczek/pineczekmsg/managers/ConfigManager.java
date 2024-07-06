package pl.pineczek.pineczekmsg.managers;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import pl.pineczek.pineczekmsg.Main;

import java.io.File;
import java.io.IOException;

public class ConfigManager {
		private static Main plugin = Main.getInstance();
		private static Configuration config;
		private static Configuration messages;
		public static void loadConfig() throws IOException {

				config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(plugin.getDataFolder(), "config.yml"));

				if (config.getString("lang").isEmpty()) return;

				String lang = config.getString("lang");
				File langFile = new File(plugin.getDataFolder(), "messages-" + lang + ".yml");

				if (langFile.exists()) messages = ConfigurationProvider.getProvider(YamlConfiguration.class).load(langFile);

		}

		public static Configuration getConfig() { return config; };
		public static Configuration getMessages() { return messages; };

		public static String getMsg(String key) {
				return getMessages().getString(key);
		}


}
