package net.craftersland.itemrestrict.restrictions;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import net.craftersland.itemrestrict.ItemRestrict;
import net.craftersland.itemrestrict.RestrictedItemsHandler.ActionType;
import net.craftersland.itemrestrict.utils.RestrictedItem;

public class Pickup implements Listener {
	
	private ItemRestrict ir = ItemRestrict.get();
	
	@EventHandler(priority = EventPriority.LOWEST)
	private void onItemPickup(PlayerPickupItemEvent event) {
		if (ir.getConfigHandler().getBoolean("General.Restrictions.PickupBans")) {
			Player player = event.getPlayer();
			ItemStack item = event.getItem().getItemStack();
			
			RestrictedItem bannedInfo = ir.getRestrictedItemsHandler().isBanned(ActionType.OWNERSHIP, player, item.getType(), /*item.getDurability(), */player.getLocation());
			
			if (bannedInfo == null) {
				RestrictedItem bannedInfo2 = ir.getRestrictedItemsHandler().isBanned(ActionType.PICKUP, player, item.getType(), /*item.getDurability(), */player.getLocation());
				
				if (bannedInfo2 != null) {
					event.setCancelled(true);
					
					Location loc = event.getItem().getLocation();
					event.getItem().teleport(new Location(loc.getWorld(), loc.getX() + getRandomInt(), loc.getY() + getRandomInt(), loc.getZ() + getRandomInt()));
					
					ir.getSoundHandler().sendPlingSound(player);
					ir.getConfigHandler().printMessage(player, "chatMessages.pickupRestricted", bannedInfo2.reason);
				}
			}
		}
	}
	
	private int getRandomInt() {
		Random randomGenerator = new Random();
		int randSlot = randomGenerator.nextInt(5);
		return randSlot;
	}

}
