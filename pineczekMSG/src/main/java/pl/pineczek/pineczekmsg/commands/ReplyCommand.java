package pl.pineczek.pineczekmsg.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import pl.pineczek.pineczekmsg.Main;
import pl.pineczek.pineczekmsg.managers.ConfigManager;
import pl.pineczek.pineczekmsg.managers.MsgManager;

public class ReplyCommand extends Command {
		public ReplyCommand(String name) {
				super(name);
		}

		@Override
		public void execute(CommandSender commandSender, String[] strings) {
				ProxiedPlayer p = (ProxiedPlayer)commandSender;

				if (strings.length < 1) {
						p.sendMessage(Main.c(
							ConfigManager.getMsg("reply_bad_usage")
						));
						return;
				}

				if (!MsgManager.responseList.containsKey(p)) {
						p.sendMessage(Main.c(ConfigManager.getMsg("msg_unknown_player_to_reply")));
						return;
				}

				if (MsgManager.responseList.get(p).isConnected()) {

						ProxiedPlayer target = MsgManager.responseList.get(p);

						if (MsgManager.ignoreList.containsKey(target.getUniqueId()) && MsgManager.ignoreList.get(target.getUniqueId()).contains(p.getUniqueId())) {
								p.sendMessage(Main.c(ConfigManager.getMsg("msg_ignored")));
								return;
						}

						target.sendMessage(Main.c(
							ConfigManager.getConfig().getString("msg_syntax.message_getter")
								.replaceAll("%getter%", target.getDisplayName())
								.replaceAll("%sender%", p.getDisplayName())
								.replaceAll("%message%", strings[0])
						));

						p.sendMessage(Main.c(
							ConfigManager.getConfig().getString("msg_syntax.message_sender")
								.replaceAll("%getter%", target.getDisplayName())
								.replaceAll("%sender%", p.getDisplayName())
								.replaceAll("%message%", strings[0])
						));

						if (ConfigManager.getConfig().getInt("reply_type") != 1) { // != 1 so == 0
								MsgManager.responseList.put(p, target);
						} else {
								MsgManager.responseList.put(p, target);
								MsgManager.responseList.put(target, p);
						}

				} else {
						p.sendMessage(Main.c(ConfigManager.getMsg("msg_unknown_player_to_reply")));
				}

		}

}
