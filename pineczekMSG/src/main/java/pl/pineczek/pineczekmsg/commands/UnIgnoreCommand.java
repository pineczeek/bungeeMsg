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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class UnIgnoreCommand extends Command implements TabExecutor {
		public UnIgnoreCommand(String name) {
				super(name);
		}

		@Override
		public void execute(CommandSender commandSender, String[] strings) {

				ProxiedPlayer p = (ProxiedPlayer) commandSender;

				if (strings.length < 1) {
						p.sendMessage(Main.c(
							ConfigManager.getMsg("unignore_bad_usage")
						));
						return;
				}

				ProxiedPlayer target = ProxyServer.getInstance().getPlayer(strings[0]);

				if (target != null && target.isConnected()) {
						// unignore

						UUID pUUID = p.getUniqueId();
						UUID targetUUID = target.getUniqueId();

						if (MsgManager.ignoreList.containsKey(pUUID)) {

								List<UUID> ignoredList = MsgManager.ignoreList.get(pUUID);

								if (ignoredList.contains(targetUUID)) {

										ignoredList.remove(targetUUID);
										p.sendMessage(Main.c(
											ConfigManager.getMsg("unignore_success")
												.replaceAll("%player%", target.getDisplayName())

										));

								} else {
										p.sendMessage(Main.c(
											ConfigManager.getMsg("unignore_unknown_player")
												.replaceAll("%player%", target.getDisplayName())

										));
								}

						} else {
								p.sendMessage(Main.c(
									ConfigManager.getMsg("unignore_unknown_player")
										.replaceAll("%player%", target.getDisplayName())

								));
						}

				} else {
						p.sendMessage(Main.c(
							ConfigManager.getMsg("unignore_unknown_player")
								.replaceAll("%player%", target.getDisplayName())
						));
				}

		}

		@Override
		public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
				if ( args.length > 2 || args.length == 0 )
				{
						return ImmutableSet.of();
				}
				Set<String> matches = new HashSet<>();
				if ( args.length == 1 ) {
						String search = args[0].toLowerCase();
						for ( ProxiedPlayer player : ProxyServer.getInstance().getPlayers() )
						{
								if ( player.getName().toLowerCase().startsWith( search ) )
								{
										matches.add( player.getName() );
								}
						}
				}
				return matches;
		}

}