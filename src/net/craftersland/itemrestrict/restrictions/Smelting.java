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
	
	private ItemRestrict ir;
	
	public Smelting(ItemRestrict ir) {
		this.ir = ir;
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	private void onItemCrafted(FurnaceSmeltEvent event) {
		ItemStack item = event.getSource();
		Furnace f = (Furnace) event.getBlock().getState();
		if (f.getInventory().getViewers().isEmpty() == false) {
			Player p = (Player) f.getInventory().getViewers().get(0);
			
			RestrictedItem bannedInfo = ir.getRestrictedItemsHandler().isBanned(ActionType.SMELTING, p, item.getType(), /*item.getDurability(), */p.getLocation());
			
			if (bannedInfo != null) {
				event.setCancelled(true);
				
				ir.getSoundHandler().sendPlingSound(p);
				ir.getConfigHandler().printMessage(p, "chatMessages.smeltingRestricted", bannedInfo.reason);
			}
		} else {
            RestrictedItem bannedInfo = ir.getRestrictedItemsHandler().isBanned(ActionType.SMELTING, null, item.getType(), /*item.getDurability(), */null);
			
			if (bannedInfo != null) {
				event.setCancelled(true);
			}
		}
	}

}
