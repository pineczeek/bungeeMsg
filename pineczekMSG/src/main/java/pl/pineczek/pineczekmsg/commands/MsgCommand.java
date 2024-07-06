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
import java.util.Set;

public class MsgCommand extends Command implements TabExecutor {
		public MsgCommand(String name) {
				super(name);
		}

		@Override
		public void execute(CommandSender commandSender, String[] strings) {
				ProxiedPlayer p = (ProxiedPlayer)commandSender;

				if (strings.length < 2) {
						p.sendMessage(Main.c(ConfigManager.getMsg("msg_bad_usage")));
						return;
				}

				ProxiedPlayer target = ProxyServer.getInstance().getPlayer(strings[0]);

				if (target != null && target.isConnected()) {

						if (MsgManager.ignoreList.containsKey(target.getUniqueId()) && MsgManager.ignoreList.get(target.getUniqueId()).contains(p.getUniqueId())) {
								p.sendMessage(Main.c(ConfigManager.getMsg("msg_ignored")));
								return;
						}

						if (MsgManager.ignoreList.containsKey(p.getUniqueId()) && MsgManager.ignoreList.get(p.getUniqueId()).contains(target.getUniqueId())) {
								p.sendMessage(Main.c(ConfigManager.getMsg("msg_ignored_by_you")));
								return;
						}

						target.sendMessage(Main.c(
							ConfigManager.getConfig().getString("msg_syntax.message_getter")
								.replaceAll("%getter%", target.getDisplayName())
								.replaceAll("%sender%", p.getDisplayName())
								.replaceAll("%message%", strings[1])
						));

						p.sendMessage(Main.c(
							ConfigManager.getConfig().getString("msg_syntax.message_sender")
								.replaceAll("%getter%", target.getDisplayName())
								.replaceAll("%sender%", p.getDisplayName())
								.replaceAll("%message%", strings[1])
						));

						if (ConfigManager.getConfig().getInt("reply_type") != 1) { //  type != 1 so type == 0
								MsgManager.responseList.put(p, target);
						} else {
								MsgManager.responseList.put(p, target);
								MsgManager.responseList.put(target, p);
						}

				} else {
						p.sendMessage(Main.c(
							ConfigManager.getMsg("msg_unknown_player")
								.replaceAll("%player%", strings[0])
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
