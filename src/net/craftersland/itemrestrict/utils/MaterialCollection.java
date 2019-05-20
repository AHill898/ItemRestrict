package net.craftersland.itemrestrict.utils;

import java.util.ArrayList;
import java.util.List;

public class MaterialCollection {
	
	List<RestrictedItem> materials = new ArrayList<RestrictedItem>();
	
	public void add(RestrictedItem restrictedItem) {
		/*int i;
		for(i = 0; i < this.materials.size() && this.materials.get(i).material <= material.material; i++);
		this.materials.add(i, material);*/
		materials.add(restrictedItem);
	}
	
	//returns a MaterialInfo complete with the friendly material name from the config file
	public RestrictedItem contains(RestrictedItem restrictedItem) {
		for(int i = 0; i < materials.size(); i++) {
			RestrictedItem thisMaterial = materials.get(i);
			if(restrictedItem.material == thisMaterial.material/* && (thisMaterial.allDataValues || material.data == thisMaterial.data)*/) {
				return thisMaterial;
			}
			/*else if(thisMaterial.material > material.material)
			{
				return null;				
			}*/
		}
			
		return null;
	}
	
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		for(int i = 0; i < materials.size(); i++) {
			stringBuilder.append(materials.get(i).toString() + " ");
		}
		
		return stringBuilder.toString();
	}
	
	public int size() {
		return materials.size();
	}

	public void clear() {
		materials.clear();
	}

}
