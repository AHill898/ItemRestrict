package net.craftersland.itemrestrict.restrictions;

import net.craftersland.itemrestrict.ItemRestrict;
import net.craftersland.itemrestrict.RestrictedItemsHandler.ActionType;
import net.craftersland.itemrestrict.utils.RestrictedItem;

import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;

public class Smelting implements Listener {
	
	private ItemRestrict ir = ItemRestrict.get();
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	private void onItemCrafted(FurnaceSmeltEvent event) {
		ItemStack item = event.getSource();
		Furnace furnace = (Furnace) event.getBlock().getState();
		if (furnace.getInventory().getViewers().isEmpty() == false) {
			Player player = (Player) furnace.getInventory().getViewers().get(0);
			
			RestrictedItem bannedInfo = ir.getRestrictedItemsHandler().isBanned(ActionType.SMELTING, player, item.getType(), /*item.getDurability(), */player.getLocation());
			
			if (bannedInfo != null) {
				event.setCancelled(true);
				
				ir.getSoundHandler().sendPlingSound(player);
				ir.getConfigHandler().printMessage(player, "chatMessages.smeltingRestricted", bannedInfo.reason);
			}
		} else {
            RestrictedItem bannedInfo = ir.getRestrictedItemsHandler().isBanned(ActionType.SMELTING, null, item.getType(), /*item.getDurability(), */null);
			
			if (bannedInfo != null) {
				event.setCancelled(true);
			}
		}
	}

}
