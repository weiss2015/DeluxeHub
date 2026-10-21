package net.zithium.deluxehub.module.modules.hotbar;

import net.zithium.deluxehub.DeluxeHubPlugin;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Marks hotbar items using the server's PersistentDataContainer,
 * which works on every Paper/Folia version without NMS access.
 */
public final class HotbarTag {

    private static final NamespacedKey KEY = new NamespacedKey(JavaPlugin.getPlugin(DeluxeHubPlugin.class), "hotbar_item");

    private HotbarTag() {
    }

    public static ItemStack tag(ItemStack item, String key) {
        ItemStack tagged = item.clone();
        ItemMeta meta = tagged.getItemMeta();
        if (meta != null) {
            meta.getPersistentDataContainer().set(KEY, PersistentDataType.STRING, key);
            tagged.setItemMeta(meta);
        }
        return tagged;
    }

    public static String get(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return null;
        }
        return item.getItemMeta().getPersistentDataContainer().get(KEY, PersistentDataType.STRING);
    }

    public static boolean is(ItemStack item, String key) {
        return key.equals(get(item));
    }

    public static boolean has(ItemStack item) {
        return get(item) != null;
    }
}
