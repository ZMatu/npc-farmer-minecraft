package com.zmatu.npcfarmer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NPCFarmer extends JavaPlugin {

    private final Map<String, ArmorStand> npcs = new HashMap<>();

    @Override
    public void onEnable() {
        getLogger().info("NPCFarmer habilitado ✅");
    }

    @Override
    public void onDisable() {
        for (ArmorStand npc : npcs.values()) {
            if (npc != null && !npc.isDead()) {
                npc.remove();
            }
        }
        npcs.clear();
        getLogger().info("NPCFarmer deshabilitado ❌");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cEste comando solo puede ser usado por jugadores.");
            return true;
        }

        switch (command.getName().toLowerCase()) {
            case "createnpc":
                if (!player.hasPermission("npcfarmer.create")) {
                    player.sendMessage("§cNo tienes permisos para usar este comando.");
                    return true;
                }
                if (args.length < 1) {
                    player.sendMessage("§cUso correcto: /createnpc <nombre>");
                    return true;
                }
                String nombre = String.join(" ", args);
                if (npcs.containsKey(nombre.toLowerCase())) {
                    player.sendMessage("§cYa existe un NPC con ese nombre.");
                    return true;
                }
                Location loc = player.getLocation();
                World world = player.getWorld();
                ArmorStand npc = world.spawn(loc, ArmorStand.class, stand -> {
                    stand.setInvisible(true);
                    stand.setCustomNameVisible(true);
                    stand.setCustomName("§a" + nombre);
                    stand.setGravity(false);
                    stand.setInvulnerable(true);
                });
                npcs.put(nombre.toLowerCase(), npc);
                world.setChunkForceLoaded(loc.getBlockX() >> 4, loc.getBlockZ() >> 4, true);
                player.sendMessage("§aNPC creado con nombre: §f" + nombre);
                return true;

            case "removenpc":
                if (!player.hasPermission("npcfarmer.remove")) {
                    player.sendMessage("§cNo tienes permisos para usar este comando.");
                    return true;
                }
                if (args.length < 1) {
                    player.sendMessage("§cUso correcto: /removenpc <nombre>");
                    return true;
                }
                String nombreRemover = String.join(" ", args).toLowerCase();
                ArmorStand npcRemover = npcs.get(nombreRemover);
                if (npcRemover == null || npcRemover.isDead()) {
                    player.sendMessage("§cNo existe un NPC con ese nombre.");
                    return true;
                }
                npcRemover.remove();
                npcs.remove(nombreRemover);
                player.sendMessage("§aNPC eliminado: §f" + nombreRemover);
                return true;

            case "listnpcs":
                if (!player.hasPermission("npcfarmer.list")) {
                    player.sendMessage("§cNo tienes permisos para usar este comando.");
                    return true;
                }
                Set<String> lista = npcs.keySet();
                if (lista.isEmpty()) {
                    player.sendMessage("§eNo hay NPCs creados.");
                } else {
                    player.sendMessage("§aNPCs creados:");
                    for (String npcName : lista) {
                        player.sendMessage(" - " + npcName);
                    }
                }
                return true;
        }
        return false;
    }
}
