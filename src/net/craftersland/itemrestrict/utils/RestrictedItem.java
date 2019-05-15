package net.craftersland.itemrestrict.utils;

import org.bukkit.Material;

public class RestrictedItem {
	
	public Material material;
	//public short data;
	public boolean allDataValues;
	public String description;
	public String reason;
	
	public RestrictedItem(Material material, /*short data, */String description, String reason) {
		this.material = material;
		//this.data = data;
		this.allDataValues = false;
		this.description = description;
		this.reason = reason;
	}
	
	/*public RestrictedItem(int typeID, String description, String reason) {
		this.typeID = typeID;
		//this.data = 0;
		this.allDataValues = true;
		this.description = description;
		this.reason = reason;
	}*/
	
	private RestrictedItem(Material material, /*short data, */boolean allDataValues, String description, String reason) {
		this.material = material;
		//this.data = data;
		this.allDataValues = allDataValues;
		this.description = description;
		this.reason = reason;
	}
	
	@Override
	public String toString() {
		//String returnValue = String.valueOf(this.typeID) + ":" + (this.allDataValues ? "*" : String.valueOf(this.data));
		String returnValue = String.valueOf(this.material);
		if(this.description != null) returnValue += ":" + this.description + ":" + this.reason;
		
		return returnValue;
	}
	
	public static RestrictedItem fromString(String string) {
		
		if(string == null || string.isEmpty()) return null;
		
		String [] parts = string.split(":");
		if(parts.length < 2) return null;
		
		try {
			
			//int typeID = Integer.parseInt(parts[0]);
			Material material = Material.valueOf(parts[0].toUpperCase());
			
			/*short data;
			boolean allDataValues;
			if(parts[1].equals("*"))
			{
				allDataValues = true;
				data = 0;
			}
			else {
				allDataValues = false;
				data = Short.parseShort(parts[1]);
			}*/
			
			//return new RestrictedItem(typeID, data, allDataValues, parts.length >= 3 ? parts[2] : "", parts.length >= 4 ? parts[3] : "(No reason provided.)");
			return new RestrictedItem(material, parts.length >= 2 ? parts[1] : "", parts.length >= 3 ? parts[2] : "(No reason provided.)");
		}
		catch(NumberFormatException exception) {
			return null;
		}
	}

}
