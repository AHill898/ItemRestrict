package net.craftersland.itemrestrict.restrictions;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import net.craftersland.itemrestrict.ItemRestrict;
import net.craftersland.itemrestrict.RestrictedItemsHandler.ActionType;
import net.craftersland.itemrestrict.utils.RestrictedItem;

public class Usage implements Listener {
	
	private ItemRestrict ir;
	private Set<String> safety = new HashSet<String>();
	
	public Usage(ItemRestrict ir) {
		this.ir = ir;
	}
	
	private boolean isEventSafe(final String pN) {
		if (safety.contains(pN) == true) {
			return false;
		}
		safety.add(pN);
		Bukkit.getScheduler().runTaskLaterAsynchronously(ir, new Runnable() {

			@Override
			public void run() {
				safety.remove(pN);
			}
			
		}, 1L);
		return true;
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.LOWEST)
	private void onInteract(PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		ItemStack item = null;
		ItemStack item2 = null;
		if (ir.is19Server == false) {
			item = player.getItemInHand();
		} else {
			if (isEventSafe(event.getPlayer().getName()) == false) return;
			item = player.getInventory().getItemInMainHand();
			item2 = player.getInventory().getItemInOffHand();
		}
		Block interactigBlock = event.getClickedBlock();
		RestrictedItem bannedInfoInteractingBlock = null;
		if (interactigBlock != null) {
			bannedInfoInteractingBlock = ir.getRestrictedItemsHandler().isBanned(ActionType.USAGE, player, interactigBlock.getType(), /*interactigBlock.getData(), */player.getLocation());
		}

		if (bannedInfoInteractingBlock != null) {
			event.setCancelled(true);
			ir.getSoundHandler().sendPlingSound(player);
			ir.getConfigHandler().printMessage(player, "chatMessages.ussageRestricted", bannedInfoInteractingBlock.reason);
		} else if (ir.is19Server == false) {
			if (event.isBlockInHand() == false) {
				if (ir.getRestrictedItemsHandler().isBanned(ActionType.OWNERSHIP, player, item.getType(), /*item.getDurability(), */player.getLocation()) == null) {
					RestrictedItem bannedInfoMainHand = ir.getRestrictedItemsHandler().isBanned(ActionType.USAGE, player, item.getType(), /*item.getDurability(), */player.getLocation());
					if (bannedInfoMainHand != null) {
						event.setCancelled(true);
						Bukkit.getScheduler().runTask(ir, new Runnable() {

							@Override
							public void run() {
								ItemStack handItem = player.getItemInHand();
								player.getWorld().dropItem(player.getLocation(), handItem);
								player.setItemInHand(null);
								player.closeInventory();
								player.updateInventory();
							}
							
						});
						
						ir.getSoundHandler().sendPlingSound(player);
						ir.getConfigHandler().printMessage(player, "chatMessages.ussageRestricted", bannedInfoMainHand.reason);
					}
				}
			}
		} else if (ir.is19Server == true) {
			if (event.isBlockInHand() == false) {
				if (ir.getRestrictedItemsHandler().isBanned(ActionType.OWNERSHIP, player, item.getType(), /*item.getDurability(), */player.getLocation()) == null) {
					RestrictedItem bannedInfoMainHand = ir.getRestrictedItemsHandler().isBanned(ActionType.USAGE, player, item.getType(), /*item.getDurability(), */player.getLocation());
					if (bannedInfoMainHand != null) {
						event.setCancelled(true);
						Bukkit.getScheduler().runTask(ir, new Runnable() {

							@Override
							public void run() {
								ItemStack handItem = player.getInventory().getItemInMainHand();
								player.getWorld().dropItem(player.getLocation(), handItem);
								player.getInventory().setItemInMainHand(null);
								player.closeInventory();
								player.updateInventory();
							}
							
						});
						
						ir.getSoundHandler().sendPlingSound(player);
						ir.getConfigHandler().printMessage(player, "chatMessages.ussageRestricted", bannedInfoMainHand.reason);
					}
				}
				if (ir.getRestrictedItemsHandler().isBanned(ActionType.OWNERSHIP, player, item.getType(), /*item.getDurability(), */player.getLocation()) == null) {
					RestrictedItem bannedInfoOffHand = ir.getRestrictedItemsHandler().isBanned(ActionType.USAGE, player, item2.getType(), /*item2.getDurability(), */player.getLocation());
					if (bannedInfoOffHand != null) {
						event.setCancelled(true);
						Bukkit.getScheduler().runTask(ir, new Runnable() {

							@Override
							public void run() {
								ItemStack handItem = player.getInventory().getItemInOffHand();
								player.getWorld().dropItem(player.getLocation(), handItem);
								player.getInventory().setItemInOffHand(null);
								player.closeInventory();
								player.updateInventory();
							}
							
						});
						
						ir.getSoundHandler().sendPlingSound(player);
						ir.getConfigHandler().printMessage(player, "chatMessages.ussageRestricted", bannedInfoOffHand.reason);
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	private void onItemHeldSwitch(PlayerItemHeldEvent event) {
		int newSlot = event.getNewSlot();
		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItem(newSlot);
		
		if (item != null) {
			if (item.getType().isBlock() == false) {
				RestrictedItem bannedInfo = ir.getRestrictedItemsHandler().isBanned(ActionType.OWNERSHIP, player, item.getType(), /*item.getDurability(), */player.getLocation());
				
				if (bannedInfo == null) {
					RestrictedItem bannedInfo2 = ir.getRestrictedItemsHandler().isBanned(ActionType.USAGE, player, item.getType(), /*item.getDurability(), */player.getLocation());
					
					if (bannedInfo2 != null) {
						player.getWorld().dropItem(player.getLocation(), item);
						player.getInventory().setItem(newSlot, null);
						player.updateInventory();
						
						ir.getSoundHandler().sendPlingSound(player);
						ir.getConfigHandler().printMessage(player, "chatMessages.ussageRestricted", bannedInfo2.reason);
					}
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.LOWEST)
	private void onEntityDamage(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			Player player = (Player) event.getDamager();
			ItemStack item = player.getItemInHand();
			RestrictedItem bannedInfo = ir.getRestrictedItemsHandler().isBanned(ActionType.USAGE, player, item.getType(), /*item.getDurability(), */player.getLocation());
			
			if (bannedInfo != null) {
				event.setCancelled(true);
			}
		} else if (ir.mcpcServer == false) {
			if (event.getDamager() instanceof Projectile) {
				Projectile pr = (Projectile) event.getDamager();
				if (pr.getShooter() instanceof Player) {
					Player player = (Player) pr.getShooter();
					ItemStack item = player.getItemInHand();
					RestrictedItem bannedInfo = ir.getRestrictedItemsHandler().isBanned(ActionType.USAGE, player, item.getType(), /*item.getDurability(), */player.getLocation());
					
					if (bannedInfo != null) {
						event.setCancelled(true);
					}
				}
			}
		}
	}
}
