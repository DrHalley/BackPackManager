package com.efetamturk.backPackManager.data;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.*;
import java.sql.*;
import java.util.Base64;
import java.util.UUID;

public class BackpackManager {
    private final String url = "jdbc:sqlite:plugins/BackpackPlugin/backpacks.db";

    public BackpackManager() {
        new File("plugins/BackpackPlugin").mkdirs(); // klasörü oluşturur (yoksa)

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS backpacks (uuid TEXT PRIMARY KEY, contents TEXT)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Inventory getBackpack(UUID uuid) {
        Inventory inv = Bukkit.createInventory(null, 9, "Your Backpack");

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement("SELECT contents FROM backpacks WHERE uuid = ?")) {

            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String base64 = rs.getString("contents");
                ItemStack[] items = deserializeItems(base64);
                inv.setContents(items);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return inv;
    }

    public void saveBackpack(Player player) {
        UUID uuid = player.getUniqueId();
        Inventory inv = player.getOpenInventory().getTopInventory();
        String base64 = serializeItems(inv.getContents());

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement("REPLACE INTO backpacks (uuid, contents) VALUES (?, ?)")) {

            ps.setString(1, uuid.toString());
            ps.setString(2, base64);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String serializeItems(ItemStack[] items) {
        try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
             BukkitObjectOutputStream out = new BukkitObjectOutputStream(byteOut)) {
            out.writeInt(items.length);
            for (ItemStack item : items) {
                out.writeObject(item);
            }
            return Base64.getEncoder().encodeToString(byteOut.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }


    private ItemStack[] deserializeItems(String base64) {
        try (ByteArrayInputStream byteIn = new ByteArrayInputStream(Base64.getDecoder().decode(base64));
             BukkitObjectInputStream in = new BukkitObjectInputStream(byteIn)) {
            int size = in.readInt();
            ItemStack[] items = new ItemStack[size];
            for (int i = 0; i < size; i++) {
                items[i] = (ItemStack) in.readObject();
            }
            return items;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ItemStack[9]; // Default boş backpack
        }
    }

}
