package net.craftersland.itemrestrict.restrictions;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import net.craftersland.itemrestrict.ItemRestrict;
import net.craftersland.itemrestrict.RestrictedItemsHandler.ActionType;
import net.craftersland.itemrestrict.utils.RestrictedItem;

public class Drop implements Listener {
	
	private ItemRestrict ir;
	
	public Drop(ItemRestrict ir) {
		this.ir = ir;
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	private void onItemDrop(PlayerDropItemEvent event) {
		if (ir.getConfigHandler().getBoolean("General.Restrictions.DropBans") == true) {
			Player p = event.getPlayer();
			ItemStack item = event.getItemDrop().getItemStack();
			
			RestrictedItem bannedInfo = ir.getRestrictedItemsHandler().isBanned(ActionType.OWNERSHIP, p, item.getType(), /*item.getDurability(), */p.getLocation());
			
			if (bannedInfo == null) {
				RestrictedItem bannedInfo2 = ir.getRestrictedItemsHandler().isBanned(ActionType.DROP, p, item.getType(), /*item.getDurability(), */p.getLocation());
				
				if (bannedInfo2 != null) {
					event.setCancelled(true);
					
					ir.getSoundHandler().sendPlingSound(p);
					ir.getConfigHandler().printMessage(p, "chatMessages.dropingRestricted", bannedInfo2.reason);
				}
			}
		}
	}

}
