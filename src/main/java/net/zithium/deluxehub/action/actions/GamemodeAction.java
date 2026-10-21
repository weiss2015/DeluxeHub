package net.zithium.deluxehub.action.actions;

import net.zithium.deluxehub.DeluxeHubPlugin;
import net.zithium.deluxehub.action.Action;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class GamemodeAction implements Action {

    @Override
    public String getIdentifier() {
        return "GAMEMODE";
    }

    @Override
    public void execute(DeluxeHubPlugin plugin, Player player, String data) {
        GameMode gameMode;
        try {
            gameMode = GameMode.valueOf(data.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            Bukkit.getLogger().warning("[DeluxeHub Action] Invalid gamemode name: " + data.toUpperCase());
            return;
        }

        // Folia: gamemode must be changed on the player's own thread
        DeluxeHubPlugin.scheduler().runAtEntity(player, task -> player.setGameMode(gameMode));
    }
}
