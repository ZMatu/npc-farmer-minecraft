package com.tuplugin;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FakePlayerPlugin extends JavaPlugin {
    private ProtocolManager protocolManager;
    private final Map<String, SkinData> skins = new HashMap<>();

    @Override
    public void onEnable() {
        protocolManager = ProtocolLibrary.getProtocolManager();
        getLogger().info("FakePlayerPlugin habilitado!");

        // Skins precargadas
        skins.put("steve", new SkinData("VALOR_STEVE", "SIGN_STEVE"));
        skins.put("alex", new SkinData("VALOR_ALEX", "SIGN_ALEX"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;

        if (command.getName().equalsIgnoreCase("npcspawn")) {
            if (args.length < 1) {
                player.sendMessage("§cUso: /npcspawn <nombre> [skin]");
                return true;
            }
            String npcName = args[0];
            String skinKey = args.length > 1 ? args[1].toLowerCase() : "steve";
            SkinData skin = skins.getOrDefault(skinKey, skins.get("steve"));
            spawnFakePlayer(player.getLocation(), npcName, skin);
            player.sendMessage("§aNPC '" + npcName + "' spawneado con skin " + skinKey + "!");
            return true;
        }
        return false;
    }

    private void spawnFakePlayer(Location loc, String name, SkinData skin) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), name);
        profile.getProperties().put("textures", new Property("textures", skin.value, skin.signature));

        WrappedGameProfile wrappedProfile = WrappedGameProfile.fromHandle(profile);

        PacketContainer addPlayer = protocolManager.createPacket(
                com.comphenix.protocol.PacketType.Play.Server.PLAYER_INFO);
        addPlayer.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.ADD_PLAYER);
        addPlayer.getPlayerInfoDataLists().write(0,
                Collections.singletonList(new PlayerInfoData(
                        wrappedProfile, 0, EnumWrappers.NativeGameMode.SURVIVAL,
                        WrappedChatComponent.fromText(profile.getName()))));
        protocolManager.broadcastServerPacket(addPlayer);

        PacketContainer spawn = protocolManager.createPacket(
                com.comphenix.protocol.PacketType.Play.Server.NAMED_ENTITY_SPAWN);
        spawn.getUUIDs().write(0, profile.getId());
        spawn.getDoubles().write(0, loc.getX());
        spawn.getDoubles().write(1, loc.getY());
        spawn.getDoubles().write(2, loc.getZ());
        protocolManager.broadcastServerPacket(spawn);
    }

    private record SkinData(String value, String signature) {}
}
