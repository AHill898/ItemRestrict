package net.craftersland.itemrestrict.restrictions;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import net.craftersland.itemrestrict.ItemRestrict;
import net.craftersland.itemrestrict.RestrictedItemsHandler.ActionType;
import net.craftersland.itemrestrict.utils.RestrictedItem;

public class Creative implements Listener {
	
	private ItemRestrict ir = ItemRestrict.get();
	
	@EventHandler(priority = EventPriority.LOWEST)
	private void onCreativeEvents(InventoryCreativeEvent event) {
		if (ir.getConfigHandler().getBoolean("General.Restrictions.CreativeBans")) {
			ItemStack cursorItem = event.getCursor();
			
			if (cursorItem != null) {
				Player player = (Player) event.getWhoClicked();
				RestrictedItem bannedInfo = ir.getRestrictedItemsHandler().isBanned(ActionType.OWNERSHIP, player, cursorItem.getType(), /*cursoritem.getDurability(), */player.getLocation());
				
				if (bannedInfo == null) {
					RestrictedItem bannedInfo2 = ir.getRestrictedItemsHandler().isBanned(ActionType.CREATIVE, player, cursorItem.getType(), /*cursoritem.getDurability(), */player.getLocation());
					
					if (bannedInfo2 != null) {
						event.setCancelled(true);
						event.setCursor(null);
						
						ir.getSoundHandler().sendItemBreakSound(player);
						ir.getConfigHandler().printMessage(player, "chatMessages.creativeRestricted", bannedInfo2.reason);
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	private void onItemClicked(InventoryClickEvent event) {
		if (ir.getConfigHandler().getBoolean("General.Restrictions.CreativeBans")) {
			if (event.getSlotType() != null) {
				if (event.getCurrentItem() != null) {
					Player p = (Player) event.getWhoClicked();
					if (p.getGameMode() == GameMode.CREATIVE) {
						ItemStack currentItem = event.getCurrentItem();
						
						RestrictedItem bannedInfo = ir.getRestrictedItemsHandler().isBanned(ActionType.OWNERSHIP, p, currentItem.getType(), /*currentitem.getDurability(), */p.getLocation());
						
						if (bannedInfo == null) {
							RestrictedItem bannedInfo2 = ir.getRestrictedItemsHandler().isBanned(ActionType.CREATIVE, p, currentItem.getType(), /*currentitem.getDurability(), */p.getLocation());
							
							if (bannedInfo2 != null) {
								event.setCancelled(true);
								
								ir.getSoundHandler().sendItemBreakSound(p);
								ir.getConfigHandler().printMessage(p, "chatMessages.creativeRestricted", bannedInfo2.reason);
							}
						}
					}
				}				
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.LOWEST)
	private void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		
		if (player.getGameMode() == GameMode.CREATIVE) {
			ItemStack item = player.getItemInHand();
			
			RestrictedItem bannedInfo = ir.getRestrictedItemsHandler().isBanned(ActionType.OWNERSHIP, player, item.getType(), /*item.getDurability(), */player.getLocation());
			
			if (bannedInfo == null) {
				RestrictedItem bannedInfo2 = ir.getRestrictedItemsHandler().isBanned(ActionType.CREATIVE, player, item.getType(), /*item.getDurability(), */player.getLocation());
				
				if (bannedInfo2 != null) {
					event.setCancelled(true);
					
					ir.getSoundHandler().sendItemBreakSound(player);
					ir.getConfigHandler().printMessage(player, "chatMessages.creativeRestricted", bannedInfo2.reason);
				}
			}
		}
	}

}
