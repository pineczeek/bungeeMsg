package pl.pineczek.pineczekmsg.commands;

import com.google.common.collect.ImmutableSet;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import pl.pineczek.pineczekmsg.Main;
import pl.pineczek.pineczekmsg.managers.ConfigManager;
import pl.pineczek.pineczekmsg.managers.MsgManager;

import java.util.*;

public class IgnoreCommand extends Command implements TabExecutor {
		public IgnoreCommand(String name) {
				super(name);
		}

		@Override
		public void execute(CommandSender commandSender, String[] strings) {
				ProxiedPlayer p = (ProxiedPlayer) commandSender;

				if (strings.length < 1) {
						p.sendMessage(Main.c(
							ConfigManager.getMsg("ignore_bad_usage")
						));
						return;
				}

				ProxiedPlayer target = ProxyServer.getInstance().getPlayer(strings[0]);

				if (target != null && target.isConnected()) {

						List<UUID> list;
						if (MsgManager.ignoreList.containsKey(p.getUniqueId())) {
								list = MsgManager.ignoreList.get(p.getUniqueId());
						} else {
								list = new ArrayList<>();
						}
						list.add(target.getUniqueId());
						MsgManager.ignoreList.put(p.getUniqueId(), list);

						p.sendMessage(Main.c(
							ConfigManager.getMsg("ignore_started")
								.replaceAll("%player%", target.getDisplayName())
						));

				} else {
						p.sendMessage(Main.c(
							ConfigManager.getMsg("ignore_unknown_player")
						));
				}

		}

		@Override
		public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
				if (args.length > 2 || args.length == 0)
				{
						return ImmutableSet.of();
				}
				Set<String> matches = new HashSet<>();
				if (args.length == 1) {
						String search = args[0].toLowerCase();
						for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
								if (player.getName().toLowerCase().startsWith(search)) matches.add(player.getName());
						}
				}
				return matches;
		}

}
