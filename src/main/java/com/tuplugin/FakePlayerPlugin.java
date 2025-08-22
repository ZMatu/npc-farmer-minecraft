package com.tuplugin;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class FakePlayerPlugin extends JavaPlugin {

    private ProtocolManager protocolManager;
    private final Map<String, SkinData> skins = new HashMap<>();

    @Override
    public void onEnable() {
        protocolManager = ProtocolLibrary.getProtocolManager();
        getLogger().info("FakePlayerPlugin habilitado!");

        // Skins precargadas
        skins.put("gojo", new SkinData(
                "ewogICJ0aW1lc3RhbXAiIDogMTY5MjgxMjYyMTAxOCwKICAicHJvZmlsZUlkIiA6ICI2YTE1NjRiMWY5NzM0NWIzOTNlNWU3N2UwMjU1YWZjNiIsCiAgInByb2ZpbGVOYW1lIiA6ICJEYXJrX0h1IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzIyMDc1ZGNmN2UwNjVmZDI4ZjlhYzZkZDk0MDE3NjRmYTNmNWE0MWE0YjFjNmIzY2IzYjNiYjU3OGYxZjUzNWIiCiAgICB9CiAgfQp9",
                "PxqVKAUGSW44tVlysFeskd166HsDPT1DRegpEGXULdLCiq+6iQoq5pNRlF+McpCNJAOmUjgEVwgtoMNO9b8IVJel2qXgy8mRHcz9rh9Rd89To3j7mhl26VXQcLZPfVAXDDcZTkiZmwmanNnlVzWWmCGy5njXLUqBN3TRczlBktH8Zde4BXcjuu67+8aca696hI2hMSxnjd4GmBwsQfelZVbZodu68aR5MrwbuswMR52ZU60ubvn8z11Hde0wc1GM3ybpokgCHhx4Ooy138Vhr0FW9J0e4tbEeqRHFSO6KvbdPxX/ieVU9Y+40ojODO00M/JAWNi7IruRO5WtcAoGPeI+4UDslD5Mw4BJBPY28i/XMjI6Dii3+4G4JeyJfWWdcOpqgVllaVaAfcVAG+iuJUk+w+xBbdG3FktCSQSfwHhSInYgKQHj+DmDsiVm5uRYwtgN6+ubaCSBNXuKyQnn+WTLtxUx8WyHHdCvNGJXquKAX4ae6n1Jr96AJMJXLgJlLrZItCwmK0bIQVGYsUKqVdyA66Tr3y2ieZqoJjORgL1KZZEHQZKlnHVVPh9PIHZVk06hUrBOLsCCE2KxuRq1uHL9i8DSmRQdDq3umFRiHrUMTQalErRM6RGrskxvmcgzUJiHVXkhm/mVOuG+H5wsVSSaMpnCv0H5eE76us7ec1I="
        ));

        skins.put("toji", new SkinData(
                "ewogICJ0aW1lc3RhbXAiIDogMTc1NTg5Nzk5MTg0MiwKICAicHJvZmlsZUlkIiA6ICI1NjUwYzI4OTliOTQ0ZDY0YmRkNTZiMzQ1ODU0NGRhZCIsCiAgInByb2ZpbGVOYW1lIiA6ICIyTHZzIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzdlZWIwYWQ2YmIxOTNiNjNkZDI0ODJmMzE0ZjI2Njg2ZjJmMDUxZDU1YTAzNTgyNTZjY2QwZjY3ZjM2NWJjOGYiCiAgICB9LAogICAgIkNBUEUiIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2RiYzIxZTIyMjUyOGUzMGRjODg0NDUzMTRmN2JlNmZmMTJkM2FlZWJjM2MxOTIwNTRmYmE3ZTNiM2Y4Yzc3YjEiCiAgICB9CiAgfQp9",
                "Lkj16ckCI4wyFaO46+dj3I3Ey6eC9LAwGeIkp7p5imYTiRSKVul9Bt/bVb+v0Oc+FmmEbMwL0b8fGrpTMxw4I/V89hqat0jKdo9F8YtF/0geXuweiINIkan5cpus9nHA3UZOSRGslYIo7MH4Tuhx55K15olmmZ05tkEXGN3QeutqbXObWP9PpV18z2wja1SViuA72x9+Ksjyxc8Avd7u12so9FYQ8nblzBbCDdRdsLfvd70/5dKeUwRLuPEMeRwphnbcsBjf8aHPa52B76Cfbegb5+B4Ug0C506C7R9S5D6m9K5rdGpf++/Jozrt95Pbcgz5zw4fflrzgmxcyp3jNwnB7LNg2kSFEBkG17ZBmC1G3FIMyMpEH8YHvzpMVdbLNNWz8eE7xT7tupOrswTr+EMx1l7S+jM+10UIAiV7Lxyys/wJn9TArhpThW58j0a5Csh8uPxYbmJQk9mJfYayT6UBWMupfOnpe2l7cOIwf5SrsEobYW/r6pZETcKYkrhiYQo8eCuYoV8wiWUA5A4kzGVsMVo+aMxhTV59DMuRAgcwgNkB6I0v6174lv5ROXCP+wW2Ti/r2tkHLzNmIrIuJRcu6hX/noRChmg2ufMGmPLSr8yJRgOdasrmTGHYgAGrd5U+g+Gmk1O2McFmldQYT/KjI5uns9mWW2tpChhGn48="
        ));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cEste comando solo puede ser ejecutado por un jugador.");
            return true;
        }

        Player player = (Player) sender;

        // Solo operadores
        if (!player.isOp()) {
            player.sendMessage("§cNo tienes permiso para usar este comando.");
            return true;
        }

        if (command.getName().equalsIgnoreCase("npcspawn")) {
            if (args.length < 1) {
                player.sendMessage("§cUso: /npcspawn <nombre> [skin]");
                return true;
            }

            String npcName = args[0];
            String skinKey = args.length > 1 ? args[1].toLowerCase() : "gojo";
            SkinData skin = skins.getOrDefault(skinKey, skins.get("gojo"));

            spawnFakePlayer(player.getLocation(), npcName, skin);
            player.sendMessage("§aNPC '" + npcName + "' spawneado con skin " + skinKey + "!");
            return true;
        }

        return false;
    }

   private void spawnFakePlayer(Location loc, String name, SkinData skin) {
    try {
        GameProfile profile = new GameProfile(UUID.randomUUID(), name);
        profile.getProperties().put("textures", new Property("textures", skin.value, skin.signature));

        WrappedGameProfile wrappedProfile = WrappedGameProfile.fromHandle(profile);

        // PLAYER_INFO packet
        PacketContainer infoPacket = protocolManager.createPacket(PacketType.Play.Server.PLAYER_INFO);
        infoPacket.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.ADD_PLAYER);

        List<PlayerInfoData> data = new ArrayList<>();
        data.add(new PlayerInfoData(
                wrappedProfile,
                20, // latency dummy
                EnumWrappers.NativeGameMode.SURVIVAL,
                WrappedChatComponent.fromText(name)
        ));

        infoPacket.getPlayerInfoDataLists().write(0, data);
        protocolManager.broadcastServerPacket(infoPacket);

        // NAMED_ENTITY_SPAWN packet
        PacketContainer spawnPacket = protocolManager.createPacket(PacketType.Play.Server.NAMED_ENTITY_SPAWN);
        spawnPacket.getUUIDs().write(0, profile.getId());
        spawnPacket.getDoubles().write(0, loc.getX());
        spawnPacket.getDoubles().write(1, loc.getY());
        spawnPacket.getDoubles().write(2, loc.getZ());

        protocolManager.broadcastServerPacket(spawnPacket);

    } catch (Exception e) {
        e.printStackTrace();
    }
}

    private record SkinData(String value, String signature) {}
}
