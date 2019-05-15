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
	
	private ItemRestrict ir;
	
	public Crafting(ItemRestrict ir) {
		this.ir = ir;
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	private void onItemCrafted(PrepareItemCraftEvent event) {
		if (event.getRecipe() != null) {
			ItemStack item = event.getRecipe().getResult();
			if (event.getViewers().isEmpty() == false) {
				Player p = (Player) event.getViewers().get(0);
				
				RestrictedItem bannedInfo = ir.getRestrictedItemsHandler().isBanned(ActionType.CRAFTING, p, item.getType(), /*item.getDurability(), */p.getLocation());
				
				if (bannedInfo != null) {
					event.getInventory().setResult(null);
					
					ir.getSoundHandler().sendPlingSound(p);
					ir.getConfigHandler().printMessage(p, "chatMessages.craftingRestricted", bannedInfo.reason);
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
