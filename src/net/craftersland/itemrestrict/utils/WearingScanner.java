package net.craftersland.itemrestrict.utils;

import net.craftersland.itemrestrict.ItemRestrict;
import net.craftersland.itemrestrict.RestrictedItemsHandler.ActionType;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

public class WearingScanner {
	
	private ItemRestrict ir = ItemRestrict.get();
	
	//WEARING RESTRICTIONS TASK
	public void wearingScanTask() {
			
		BukkitTask task = Bukkit.getScheduler().runTaskTimerAsynchronously(ir, new Runnable() {
			@Override
			public void run() {
				for (final Player player : Bukkit.getOnlinePlayers()) {
					final ItemStack boots = player.getInventory().getBoots();
					final ItemStack leggings = player.getInventory().getLeggings();
					final ItemStack chestplate = player.getInventory().getChestplate();
					final ItemStack helmet = player.getInventory().getHelmet();
					
					if (boots != null) {
						RestrictedItem bannedInfo = ir.getRestrictedItemsHandler().isBanned(ActionType.WEARING, player, boots.getType(), /*boots.getData().getData(), */player.getLocation());
						if (bannedInfo != null) {
							ir.getConfigHandler().printMessage(player, "chatMessages.wearingRestricted", bannedInfo.reason);
							Bukkit.getScheduler().runTask(ir, new Runnable() {
								@Override
								public void run() {
									
									player.getInventory().addItem(boots);
									player.getInventory().setBoots(null);
									ir.getSoundHandler().sendPlingSound(player);
								}
							});
						}
					}
					
					if (leggings != null) {
						RestrictedItem bannedInfo = ir.getRestrictedItemsHandler().isBanned(ActionType.WEARING, player, leggings.getType(), /*leggings.getData().getData(), */player.getLocation());
						if (bannedInfo != null) {
							ir.getConfigHandler().printMessage(player, "chatMessages.wearingRestricted", bannedInfo.reason);
							Bukkit.getScheduler().runTask(ir, new Runnable() {
								@Override
								public void run() {
									
									player.getInventory().addItem(leggings);
									player.getInventory().setLeggings(null);
									ir.getSoundHandler().sendPlingSound(player);
								}
							});
						}
					}
					
					if (chestplate != null) {
						RestrictedItem bannedInfo = ir.getRestrictedItemsHandler().isBanned(ActionType.WEARING, player, chestplate.getType(), /*chestplate.getData().getData(), */player.getLocation());
						if (bannedInfo != null) {
							ir.getConfigHandler().printMessage(player, "chatMessages.wearingRestricted", bannedInfo.reason);
							Bukkit.getScheduler().runTask(ir, new Runnable() {
								@Override
								public void run() {
									
									player.getInventory().addItem(chestplate);
									player.getInventory().setChestplate(null);
									ir.getSoundHandler().sendPlingSound(player);
								}
							});
						}
					}
					
					if (helmet != null) {
						RestrictedItem bannedInfo = ir.getRestrictedItemsHandler().isBanned(ActionType.WEARING, player, helmet.getType(), /*helmet.getData().getData(), */player.getLocation());
						if (bannedInfo != null) {
							ir.getConfigHandler().printMessage(player, "chatMessages.wearingRestricted", bannedInfo.reason);
							Bukkit.getScheduler().runTask(ir, new Runnable() {
								@Override
								public void run() {
									
									player.getInventory().addItem(helmet);
									player.getInventory().setHelmet(null);
									ir.getSoundHandler().sendPlingSound(player);
								}
							});
						}
					}
				}
			}
		}, 20L, 20L);
		
		ir.wearingScanner.clear();
		ir.wearingScanner.put(true, task.getTaskId());
	}

}
