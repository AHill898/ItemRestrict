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
	
	private ItemRestrict ir = ItemRestrict.get();
	private File restrictedItemsFile;
	
	public RestrictedItemsHandler() {
		
		try {
			restrictedItemsFile = new File(ir.getDataFolder() + "RestrictedItems.yml");
			
			//Create RestrictedItems.yml and/or load it
			if (!restrictedItemsFile.exists()) {
				ItemRestrict.log.info("No RestrictedItems.yml file found! Creating new one...");
				
				ir.saveResource("RestrictedItems.yml", false);
			}
			
			FileConfiguration ymlFormat = YamlConfiguration.loadConfiguration(restrictedItemsFile);
			
			//
			//OWNERSHIP BANS - players can't have these at all (crafting is also blocked in this case)
			//
			List<String> OwnershipBanned = ymlFormat.getStringList("OwnershipBanned");
			//parse the strings from the config file
			parseMaterialListFromConfig(OwnershipBanned, ir.ownershipBanned, "OwnershipBanned");
			
			//
			//CRAFTING BANS 
			//
			List<String> CraftingBanned = ymlFormat.getStringList("CraftingBanned");
			//parse the strings from the config file
			parseMaterialListFromConfig(CraftingBanned, ir.craftingBanned, "CraftingBanned");
			
			//
			//CRAFTING BANS 
			//
			List<String> SmeltingBanned = ymlFormat.getStringList("SmeltingBanned");
			//parse the strings from the config file
			parseMaterialListFromConfig(SmeltingBanned, ir.smeltingBanned, "SmeltingBanned");
			
			//
			//CRAFTING DISABLED
			//
			List<String> CraftingDisabled = ymlFormat.getStringList("CraftingDisabled");
			//parse the strings from the config file
			for (String s : CraftingDisabled) {
				ir.craftingDisabled.add(s);
			}
			
			//
			//BREWING BANS 
			//
			List<String> BrewingBanned = ymlFormat.getStringList("BrewingBanned");
			//parse the strings from the config file
			parseMaterialListFromConfig(BrewingBanned, ir.brewingBanned, "BrewingBanned");
			
			//
			//WEARING BANS 
			//
			List<String> WearingBanned = ymlFormat.getStringList("WearingBanned");
			//parse the strings from the config file
			parseMaterialListFromConfig(WearingBanned, ir.wearingBanned, "WearingBanned");
			
			//
			//USAGE BANS 
			//
			List<String> UsageBanned = ymlFormat.getStringList("UsageBanned");
			//parse the strings from the config file
			parseMaterialListFromConfig(UsageBanned, ir.usageBanned, "UsageBanned");
			
			//
			//PLACEMENT BANS 
			//
			List<String> PlacementBanned = ymlFormat.getStringList("PlacementBanned");
			//parse the strings from the config file
			parseMaterialListFromConfig(PlacementBanned, ir.placementBanned, "PlacementBanned");
			
			//
			//BLOCK BREAK BANS 
			//
			if (ir.getConfigHandler().getBoolean("General.Restrictions.BreakBans") == true) {
				List<String> BlockBreakBanned = ymlFormat.getStringList("BlockBreakBanned");
				//parse the strings from the config file
				parseMaterialListFromConfig(BlockBreakBanned, ir.blockBreakBanned, "BlockBreakBanned");
			}
			
			//
			//CREATIVE MENU BANS 
			//
			List<String> CreativeBanned = ymlFormat.getStringList("CreativeBanned");
			//parse the strings from the config file
			parseMaterialListFromConfig(CreativeBanned, ir.creativeBanned, "CreativeBanned");
			
			//
			//PICKUP BANS 
			//
			List<String> PickupBanned = ymlFormat.getStringList("PickupBanned");
			//parse the strings from the config file
			parseMaterialListFromConfig(PickupBanned, ir.pickupBanned, "PickupBanned");
			
			//
			//DROP BANS 
			//
			List<String> DropBanned = ymlFormat.getStringList("DropBanned");
			//parse the strings from the config file
			parseMaterialListFromConfig(DropBanned, ir.dropBanned, "DropBanned");
			
			//
			//SMELTING BANS 
			//
			List<String> SmeltingDisabled = ymlFormat.getStringList("SmeltingDisabled");
			//parse the strings from the config file
			for (String s : SmeltingDisabled) {
				ir.smeltingDisabled.add(s);
			}
			
			//
			//WORLD BANS 
			//
			List<String> WorldBanned = ymlFormat.getStringList("WorldBanned");
			//parse the strings from the config file
			parseMaterialListFromConfig(WorldBanned, ir.worldBanned, "WorldBanned");
			
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
		if (ir.getConfigHandler().getString("General.EnableOnAllWorlds") != "true") {
			if (location != null) {
				if(!ir.enforcementWorlds.contains(location.getWorld())) return null;
			}
		}
		if (player != null && player.hasPermission("itemrestrict.admin") || player != null && player.hasPermission("itemrestrict.bypass")) return null;
		MaterialCollection collectionToSearch;
		String permissionNode;
		if(actionType == ActionType.USAGE) {
			collectionToSearch = ir.usageBanned;
			permissionNode = "use";
		} else if(actionType == ActionType.PLACEMENT)	{
			collectionToSearch = ir.placementBanned;
			permissionNode = "place";
		} else if(actionType == ActionType.BLOCKBREAK)	{
			collectionToSearch = ir.blockBreakBanned;
			permissionNode = "break";
		} else if(actionType == ActionType.CRAFTING) {
			collectionToSearch = ir.craftingBanned;
			permissionNode = "craft";
		} else if(actionType == ActionType.BREWING) {
			collectionToSearch = ir.brewingBanned;
			permissionNode = "brew";
		} else if(actionType == ActionType.WEARING) {
			collectionToSearch = ir.wearingBanned;
			permissionNode = "wear";
		} else if(actionType == ActionType.CREATIVE) {
			collectionToSearch = ir.creativeBanned;
			permissionNode = "creative";
		} else if(actionType == ActionType.PICKUP) {
			collectionToSearch = ir.pickupBanned;
			permissionNode = "pickup";
		} else if(actionType == ActionType.DROP) {
			collectionToSearch = ir.dropBanned;
			permissionNode = "drop";
		} else if(actionType == ActionType.SMELTING) {
			collectionToSearch = ir.smeltingBanned;
			permissionNode = "smelt";
		} else {
			collectionToSearch = ir.ownershipBanned;
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
