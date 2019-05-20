package net.craftersland.itemrestrict.restrictions;

import net.craftersland.itemrestrict.ItemRestrict;
import net.craftersland.itemrestrict.RestrictedItemsHandler.ActionType;
import net.craftersland.itemrestrict.utils.RestrictedItem;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

public class Crafting implements Listener {
	
	private ItemRestrict ir = ItemRestrict.get();
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	private void onItemCrafted(PrepareItemCraftEvent event) {
		if (event.getRecipe() != null) {
			ItemStack item = event.getRecipe().getResult();
			if (!event.getViewers().isEmpty()) {
				Player player = (Player) event.getViewers().get(0);
				
				RestrictedItem bannedInfo = ir.getRestrictedItemsHandler().isBanned(ActionType.CRAFTING, player, item.getType(), /*item.getDurability(), */player.getLocation());
				
				if (bannedInfo != null) {
					event.getInventory().setResult(null);
					
					ir.getSoundHandler().sendPlingSound(player);
					ir.getConfigHandler().printMessage(player, "chatMessages.craftingRestricted", bannedInfo.reason);
				}
			} else {
	            RestrictedItem bannedInfo = ir.getRestrictedItemsHandler().isBanned(ActionType.CRAFTING, null, item.getType(), /*item.getDurability(), */null);
				
				if (bannedInfo != null) {
					event.getInventory().setResult(null);
				}
			}
		}
	}

}
