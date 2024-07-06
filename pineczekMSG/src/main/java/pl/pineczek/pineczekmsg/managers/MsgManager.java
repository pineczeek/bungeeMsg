package pl.pineczek.pineczekmsg.managers;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MsgManager {
		public static Map<UUID, List<UUID>> ignoreList = new HashMap<>();
		public static Map<ProxiedPlayer, ProxiedPlayer> responseList = new HashMap<>();
}
