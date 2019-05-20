package net.craftersland.itemrestrict.restrictions;

import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import net.craftersland.itemrestrict.ItemRestrict;
import net.craftersland.itemrestrict.RestrictedItemsHandler.ActionType;
import net.craftersland.itemrestrict.utils.RestrictedItem;

public class Ownership implements Listener {
	
	private ItemRestrict ir = ItemRestrict.get();
	
	//Inventory Click
	@EventHandler(priority = EventPriority.LOWEST)
	private void onItemClicked(InventoryClickEvent event) {
		if (event.getSlotType() != null) {
			if (event.getCurrentItem() != null) {
				Player player = (Player) event.getWhoClicked();
				ItemStack currentItem = event.getCurrentItem();
				ItemStack cursorItem = event.getCursor();
				
				if (ir.getConfigHandler().getBoolean("General.Settings.OwnershipPlayerInvOnly") == true) {
					if (event.getInventory().getType() == InventoryType.PLAYER) {
						inventoryClickRestriction(event, currentItem, player, false);
					} else if (event.getRawSlot() >= event.getInventory().getSize()) {
						inventoryClickRestriction(event, cursorItem, player, true);
					}
				} else {
					inventoryClickRestriction(event, currentItem, player, false);
				}
				
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	private void onItemDrag(final InventoryDragEvent event) {
		if (ir.getConfigHandler().getBoolean("General.Settings.OwnershipPlayerInvOnly") == true) {
			Player p = (Player) event.getWhoClicked();
			ItemStack cursorItem = event.getOldCursor();
			
			if (cursorItem != null) {
				RestrictedItem bannedInfo = ir.getRestrictedItemsHandler().isBanned(ActionType.OWNERSHIP, p, cursorItem.getType(), /*cursorItem.getDurability(), */p.getLocation());
				
				if (bannedInfo != null) {
					event.setCancelled(true);
					
					ir.getSoundHandler().sendItemBreakSound(p);
					ir.getConfigHandler().printMessage(p, "chatMessages.restricted", bannedInfo.reason);
				}
			}
		}
	}
	
	private void inventoryClickRestriction(InventoryClickEvent event, ItemStack currentItem, Player player, Boolean removeCursorItem) {
		RestrictedItem bannedInfo = ir.getRestrictedItemsHandler().isBanned(ActionType.OWNERSHIP, player, currentItem.getType(), /*currentItem.getDurability(), */player.getLocation());
		
		if (bannedInfo != null) {
			event.setCancelled(true);
			
			if (event.getSlotType() == SlotType.ARMOR) {
				if (player.getInventory().getHelmet() != null) {
					if (player.getInventory().getHelmet().isSimilar(currentItem)) {
						player.getInventory().setHelmet(null);
					}
				}
				if (player.getInventory().getChestplate() != null) {
					if (player.getInventory().getChestplate().isSimilar(currentItem)) {
						player.getInventory().setChestplate(null);
					}
				}
				if (player.getInventory().getLeggings() != null) {
					if (player.getInventory().getLeggings().isSimilar(currentItem)) {
						player.getInventory().setLeggings(null);
					}
				}
				if (player.getInventory().getBoots() != null) {
					if (player.getInventory().getBoots().isSimilar(currentItem)) {
						player.getInventory().setBoots(null);
					}
				}
			} else if (removeCursorItem == true) {
				player.setItemOnCursor(null);
			} else {
				player.getInventory().remove(currentItem);
				event.getInventory().remove(currentItem);
			}
			
			ir.getSoundHandler().sendItemBreakSound(player);
			ir.getConfigHandler().printMessage(player, "chatMessages.restrictedConfiscated", bannedInfo.reason);
		}
	}
	
	//Item Pickup
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.LOWEST)
	private void onItemPickup(PlayerPickupItemEvent event) {
		Player player = event.getPlayer();
		ItemStack item = event.getItem().getItemStack();
		
		RestrictedItem bannedInfo = ir.getRestrictedItemsHandler().isBanned(ActionType.OWNERSHIP, player, item.getType(), /*item.getDurability(), */player.getLocation());
		
		if (bannedInfo != null) {
			event.setCancelled(true);
			
			event.getItem().remove();
			
			ir.getSoundHandler().sendItemBreakSound(player);
			player.playEffect(event.getItem().getLocation(), Effect.MOBSPAWNER_FLAMES, 5);
			ir.getConfigHandler().printMessage(player, "chatMessages.pickupRestricted", bannedInfo.reason);
		}
	}
	
	//Switch hotbar slot
	@EventHandler(priority = EventPriority.LOWEST)
	private void onItemHeldSwitch(PlayerItemHeldEvent event) {
		int newSlot = event.getNewSlot();
		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItem(newSlot);
		
		if (item != null) {
			RestrictedItem bannedInfo = ir.getRestrictedItemsHandler().isBanned(ActionType.OWNERSHIP, player, item.getType(), /*item.getDurability(), */player.getLocation());
			
			if (bannedInfo != null) {
				player.getInventory().setItem(newSlot, null);
				
				ir.getSoundHandler().sendItemBreakSound(player);
				ir.getConfigHandler().printMessage(player, "chatMessages.restrictedConfiscated", bannedInfo.reason);
			}
		}
	}
	
	//Creative event
	@EventHandler(priority = EventPriority.LOWEST)
	private void onCreativeEvents(InventoryCreativeEvent event) {
		ItemStack cursorItem = event.getCursor();
		
		if (cursorItem != null) {
			Player player = (Player) event.getWhoClicked();
			
			RestrictedItem bannedInfo = ir.getRestrictedItemsHandler().isBanned(ActionType.OWNERSHIP, player, cursorItem.getType(), /*cursorItem.getDurability(), */player.getLocation());
			
			if (bannedInfo != null) {
				event.setCancelled(true);
				event.setCursor(null);
				
				ir.getSoundHandler().sendItemBreakSound(player);
				ir.getConfigHandler().printMessage(player, "chatMessages.restrictedConfiscated", bannedInfo.reason);
			}
		}
	}
	
	//Interact event
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.LOWEST)
	private void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getItemInHand();
		
		if (item != null) {
			RestrictedItem bannedInfo = ir.getRestrictedItemsHandler().isBanned(ActionType.OWNERSHIP, player, item.getType(), /*item.getDurability(), */player.getLocation());
			
			if (bannedInfo != null) {
				event.setCancelled(true);
				player.setItemInHand(null);
				
				ir.getSoundHandler().sendItemBreakSound(player);
				ir.getConfigHandler().printMessage(player, "chatMessages.restrictedConfiscated", bannedInfo.reason);
			}
		}
	}
	
	//Item drop
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.LOWEST)
	private void onItemDrop(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		ItemStack item = event.getItemDrop().getItemStack();
		
		RestrictedItem bannedInfo = ir.getRestrictedItemsHandler().isBanned(ActionType.OWNERSHIP, player, item.getType(), /*item.getDurability(),*/player.getLocation());
		
		if (bannedInfo != null) {
			event.getItemDrop().remove();
			player.setItemInHand(null);
			
			ir.getSoundHandler().sendItemBreakSound(player);
			ir.getConfigHandler().printMessage(player, "chatMessages.restrictedConfiscated", bannedInfo.reason);
		}
	}

}
