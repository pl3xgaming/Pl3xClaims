package net.pl3x.bukkit.claims.player.task;

import net.pl3x.bukkit.claims.configuration.Config;
import net.pl3x.bukkit.claims.configuration.Lang;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class WelcomeTask extends BukkitRunnable {
    private final Player player;

    public WelcomeTask(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        if (!player.isOnline()) {
            return;
        }

        Lang.send(player, Lang.AVOID_GRIEF_CLAIM_LAND);

        if (!Config.SUPPLY_CLAIMBOOK) {
            return;
        }

        BookMeta meta = (BookMeta) Bukkit.getItemFactory().getItemMeta(Material.WRITTEN_BOOK);
        meta.setAuthor(Lang.CLAIMBOOK_AUTHOR);
        meta.setTitle(Lang.CLAIMBOOK_TITLE);
        meta.setPages(Lang.CLAIMBOOK_PAGES);

        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        book.setItemMeta(meta);
        player.getInventory().addItem(book);
    }
}