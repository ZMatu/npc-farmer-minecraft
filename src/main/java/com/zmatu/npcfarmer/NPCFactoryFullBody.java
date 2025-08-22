package com.zmatu.npcfarmer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class NPCFactoryFullBody {

    public static List<ArmorStand> createFullBodyNPC(Location loc, String name, String skinName) {
        World world = loc.getWorld();
        List<ArmorStand> parts = new ArrayList<>();

        ArmorStand body = world.spawn(loc, ArmorStand.class, stand -> {
            stand.setVisible(false);
            stand.setGravity(false);
            stand.setInvulnerable(true);
            stand.setCustomNameVisible(true);
            stand.setCustomName("§a" + name);
        });
        parts.add(body);

        ArmorStand headStand = world.spawn(loc.clone().add(0, 1.6, 0), ArmorStand.class, stand -> {
            stand.setVisible(false);
            stand.setGravity(false);
            stand.setInvulnerable(true);
            stand.setMarker(true);
        });
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        OfflinePlayer player = Bukkit.getOfflinePlayer(skinName);
        meta.setOwningPlayer(player);
        head.setItemMeta(meta);
        headStand.getEquipment().setHelmet(head);
        parts.add(headStand);

        ArmorStand torso = world.spawn(loc.clone().add(0, 0.9, 0), ArmorStand.class, stand -> {
            stand.setVisible(false);
            stand.setGravity(false);
            stand.setInvulnerable(true);
            stand.setMarker(true);
        });
        torso.getEquipment().setChestplate(new ItemStack(Material.BLUE_WOOL));
        parts.add(torso);

        ArmorStand leftArm = world.spawn(loc.clone().add(-0.25, 1.2, 0), ArmorStand.class, stand -> {
            stand.setVisible(false);
            stand.setGravity(false);
            stand.setInvulnerable(true);
            stand.setMarker(true);
        });
        leftArm.getEquipment().setHelmet(new ItemStack(Material.BLUE_WOOL));
        parts.add(leftArm);

        ArmorStand rightArm = world.spawn(loc.clone().add(0.25, 1.2, 0), ArmorStand.class, stand -> {
            stand.setVisible(false);
            stand.setGravity(false);
            stand.setInvulnerable(true);
            stand.setMarker(true);
        });
        rightArm.getEquipment().setHelmet(new ItemStack(Material.BLUE_WOOL));
        parts.add(rightArm);

        ArmorStand leftLeg = world.spawn(loc.clone().add(-0.15, 0.45, 0), ArmorStand.class, stand -> {
            stand.setVisible(false);
            stand.setGravity(false);
            stand.setInvulnerable(true);
            stand.setMarker(true);
        });
        leftLeg.getEquipment().setHelmet(new ItemStack(Material.BLUE_WOOL));
        parts.add(leftLeg);

        ArmorStand rightLeg = world.spawn(loc.clone().add(0.15, 0.45, 0), ArmorStand.class, stand -> {
            stand.setVisible(false);
            stand.setGravity(false);
            stand.setInvulnerable(true);
            stand.setMarker(true);
        });
        rightLeg.getEquipment().setHelmet(new ItemStack(Material.BLUE_WOOL));
        parts.add(rightLeg);

        return parts;
    }
}
