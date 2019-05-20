package net.craftersland.itemrestrict.restrictions;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import net.craftersland.itemrestrict.ItemRestrict;
import net.craftersland.itemrestrict.RestrictedItemsHandler.ActionType;
import net.craftersland.itemrestrict.utils.RestrictedItem;

public class OffHandSwap implements Listener {
	
	private ItemRestrict ir = ItemRestrict.get();
	
	@EventHandler(priority = EventPriority.HIGHEST)
	private void onOffHandSwap(PlayerSwapHandItemsEvent event) {
		RestrictedItem bannedInfo = ir.getRestrictedItemsHandler().isBanned(ActionType.OWNERSHIP, event.getPlayer(), event.getOffHandItem().getType(), /*event.getOffHandItem().getDurability(), */event.getPlayer().getLocation());
		if (bannedInfo == null) {
			RestrictedItem bannedInfo2 = ir.getRestrictedItemsHandler().isBanned(ActionType.USAGE, event.getPlayer(), event.getOffHandItem().getType(), /*event.getOffHandItem().getDurability(), */event.getPlayer().getLocation());
			RestrictedItem bannedInfo3 = ir.getRestrictedItemsHandler().isBanned(ActionType.PLACEMENT, event.getPlayer(), event.getOffHandItem().getType(), /*event.getOffHandItem().getDurability(), */event.getPlayer().getLocation());
			if (bannedInfo2 != null) {
				event.setCancelled(true);
				//event.setOffHandItem(null);
				ir.getSoundHandler().sendPlingSound(event.getPlayer());
				ir.getConfigHandler().printMessage(event.getPlayer(), "chatMessages.ussageRestricted", bannedInfo2.reason);
			} else if (bannedInfo3 != null) {
				event.setCancelled(true);
				//event.setOffHandItem(null);
			}
		} else {
			event.setCancelled(true);
			event.setOffHandItem(null);
		}
	}

}
