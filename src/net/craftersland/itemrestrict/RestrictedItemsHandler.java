package net.craftersland.itemrestrict;

import java.io.File;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import net.craftersland.itemrestrict.utils.MaterialCollection;
import net.craftersland.itemrestrict.utils.RestrictedItem;

public class RestrictedItemsHandler {
	
	private ItemRestrict itemRestrict;
	private File restrictedItemsFile;
	
	public RestrictedItemsHandler(ItemRestrict itemRestrict) {
		this.itemRestrict = itemRestrict;
		
		try {
			restrictedItemsFile = new File(itemRestrict.getDataFolder() + "RestrictedItems.yml");
			
			//Create RestrictedItems.yml and/or load it
			if (!restrictedItemsFile.exists()) {
				ItemRestrict.log.info("No RestrictedItems.yml file found! Creating new one...");
				
				itemRestrict.saveResource("RestrictedItems.yml", false);
			}
			
			FileConfiguration ymlFormat = YamlConfiguration.loadConfiguration(restrictedItemsFile);
			
			//
			//OWNERSHIP BANS - players can't have these at all (crafting is also blocked in this case)
			//
			List<String> OwnershipBanned = ymlFormat.getStringList("OwnershipBanned");
			//parse the strings from the config file
			parseMaterialListFromConfig(OwnershipBanned, itemRestrict.ownershipBanned, "OwnershipBanned");
			
			//
			//CRAFTING BANS 
			//
			List<String> CraftingBanned = ymlFormat.getStringList("CraftingBanned");
			//parse the strings from the config file
			parseMaterialListFromConfig(CraftingBanned, itemRestrict.craftingBanned, "CraftingBanned");
			
			//
			//CRAFTING BANS 
			//
			List<String> SmeltingBanned = ymlFormat.getStringList("SmeltingBanned");
			//parse the strings from the config file
			parseMaterialListFromConfig(SmeltingBanned, itemRestrict.smeltingBanned, "SmeltingBanned");
			
			//
			//CRAFTING DISABLED
			//
			List<String> CraftingDisabled = ymlFormat.getStringList("CraftingDisabled");
			//parse the strings from the config file
			for (String s : CraftingDisabled) {
				itemRestrict.craftingDisabled.add(s);
			}
			
			//
			//BREWING BANS 
			//
			List<String> BrewingBanned = ymlFormat.getStringList("BrewingBanned");
			//parse the strings from the config file
			parseMaterialListFromConfig(BrewingBanned, itemRestrict.brewingBanned, "BrewingBanned");
			
			//
			//WEARING BANS 
			//
			List<String> WearingBanned = ymlFormat.getStringList("WearingBanned");
			//parse the strings from the config file
			parseMaterialListFromConfig(WearingBanned, itemRestrict.wearingBanned, "WearingBanned");
			
			//
			//USAGE BANS 
			//
			List<String> UsageBanned = ymlFormat.getStringList("UsageBanned");
			//parse the strings from the config file
			parseMaterialListFromConfig(UsageBanned, itemRestrict.usageBanned, "UsageBanned");
			
			//
			//PLACEMENT BANS 
			//
			List<String> PlacementBanned = ymlFormat.getStringList("PlacementBanned");
			//parse the strings from the config file
			parseMaterialListFromConfig(PlacementBanned, itemRestrict.placementBanned, "PlacementBanned");
			
			//
			//BLOCK BREAK BANS 
			//
			if (itemRestrict.getConfigHandler().getBoolean("General.Restrictions.BreakBans") == true) {
				List<String> BlockBreakBanned = ymlFormat.getStringList("BlockBreakBanned");
				//parse the strings from the config file
				parseMaterialListFromConfig(BlockBreakBanned, itemRestrict.blockBreakBanned, "BlockBreakBanned");
			}
			
			//
			//CREATIVE MENU BANS 
			//
			List<String> CreativeBanned = ymlFormat.getStringList("CreativeBanned");
			//parse the strings from the config file
			parseMaterialListFromConfig(CreativeBanned, itemRestrict.creativeBanned, "CreativeBanned");
			
			//
			//PICKUP BANS 
			//
			List<String> PickupBanned = ymlFormat.getStringList("PickupBanned");
			//parse the strings from the config file
			parseMaterialListFromConfig(PickupBanned, itemRestrict.pickupBanned, "PickupBanned");
			
			//
			//DROP BANS 
			//
			List<String> DropBanned = ymlFormat.getStringList("DropBanned");
			//parse the strings from the config file
			parseMaterialListFromConfig(DropBanned, itemRestrict.dropBanned, "DropBanned");
			
			//
			//SMELTING BANS 
			//
			List<String> SmeltingDisabled = ymlFormat.getStringList("SmeltingDisabled");
			//parse the strings from the config file
			for (String s : SmeltingDisabled) {
				itemRestrict.smeltingDisabled.add(s);
			}
			
			//
			//WORLD BANS 
			//
			List<String> WorldBanned = ymlFormat.getStringList("WorldBanned");
			//parse the strings from the config file
			parseMaterialListFromConfig(WorldBanned, itemRestrict.worldBanned, "WorldBanned");
			
		} catch (Exception e) {
			ItemRestrict.log.severe("Could not create RestrictedItems.yml file! Error: " + e.getMessage());
		    }
		
		}
	
	private void parseMaterialListFromConfig(List<String> stringsToParse, MaterialCollection materialCollection, String configString) {
		materialCollection.clear();
		
		//for each string in the list
		for(int i = 0; i < stringsToParse.size(); i++) {
			//try to parse the string value into a material info
			RestrictedItem restrictedItem = RestrictedItem.fromString(stringsToParse.get(i));
			
			//null value returned indicates an error parsing the string from the config file
			if(restrictedItem == null) {
				//show error in log
				ItemRestrict.log.warning("ERROR: Unable to read material entry: " + stringsToParse.get(i) + " ,from RestrictedItems.yml file.");
			}
			
			//otherwise store the valid entry in config data
			else {
				materialCollection.add(restrictedItem);
			}
		}		
	}
	
	public RestrictedItem isBanned(ActionType actionType, Player player, Material material, /*short data, */Location location) {
		if (itemRestrict.getConfigHandler().getString("General.EnableOnAllWorlds") != "true") {
			if (location != null) {
				if(!itemRestrict.enforcementWorlds.contains(location.getWorld())) return null;
			}
		}
		if (player != null && player.hasPermission("itemrestrict.admin") || player != null && player.hasPermission("itemrestrict.bypass")) return null;
		MaterialCollection collectionToSearch;
		String permissionNode;
		if(actionType == ActionType.USAGE) {
			collectionToSearch = itemRestrict.usageBanned;
			permissionNode = "use";
		} else if(actionType == ActionType.PLACEMENT)	{
			collectionToSearch = itemRestrict.placementBanned;
			permissionNode = "place";
		} else if(actionType == ActionType.BLOCKBREAK)	{
			collectionToSearch = itemRestrict.blockBreakBanned;
			permissionNode = "break";
		} else if(actionType == ActionType.CRAFTING) {
			collectionToSearch = itemRestrict.craftingBanned;
			permissionNode = "craft";
		} else if(actionType == ActionType.BREWING) {
			collectionToSearch = itemRestrict.brewingBanned;
			permissionNode = "brew";
		} else if(actionType == ActionType.WEARING) {
			collectionToSearch = itemRestrict.wearingBanned;
			permissionNode = "wear";
		} else if(actionType == ActionType.CREATIVE) {
			collectionToSearch = itemRestrict.creativeBanned;
			permissionNode = "creative";
		} else if(actionType == ActionType.PICKUP) {
			collectionToSearch = itemRestrict.pickupBanned;
			permissionNode = "pickup";
		} else if(actionType == ActionType.DROP) {
			collectionToSearch = itemRestrict.dropBanned;
			permissionNode = "drop";
		} else if(actionType == ActionType.SMELTING) {
			collectionToSearch = itemRestrict.smeltingBanned;
			permissionNode = "smelt";
		} else {
			collectionToSearch = itemRestrict.ownershipBanned;
			permissionNode = "own";
		}
		
		RestrictedItem bannedInfo = collectionToSearch.contains(new RestrictedItem(material, /*data, */null, null));
		if(bannedInfo != null) {
			if (player == null) return bannedInfo;
			if(player.hasPermission("itemrestrict.bypass." + material + ".*.*")) return null;
			if(player.hasPermission("itemrestrict.bypass." + material + ".*." + permissionNode)) return null;
			//if(player.hasPermission("ItemRestrict.bypass." + material + "." + data + "." + permissionNode)) return null;			
			//if(player.hasPermission("ItemRestrict.bypass." + material + "." + data + ".*")) return null;
			
			return bannedInfo;
		}
		return null;
	}
	
	public enum ActionType {
		USAGE, OWNERSHIP, PLACEMENT, BLOCKBREAK, CRAFTING, SMELTING, CREATIVE, BREWING, WEARING, PICKUP, DROP
	}

}
