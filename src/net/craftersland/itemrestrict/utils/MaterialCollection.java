package net.craftersland.itemrestrict.utils;

import java.util.ArrayList;
import java.util.List;

public class MaterialCollection {
	
	List<RestrictedItem> materials = new ArrayList<RestrictedItem>();
	
	public void Add(RestrictedItem restrictedItem)
	{
		/*int i;
		for(i = 0; i < this.materials.size() && this.materials.get(i).material <= material.material; i++);
		this.materials.add(i, material);*/
		materials.add(restrictedItem);
	}
	
	//returns a MaterialInfo complete with the friendly material name from the config file
	public RestrictedItem Contains(RestrictedItem material)
	{
		for(int i = 0; i < this.materials.size(); i++)
		{
			RestrictedItem thisMaterial = this.materials.get(i);
			if(material.material == thisMaterial.material/* && (thisMaterial.allDataValues || material.data == thisMaterial.data)*/)
			{
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
	public String toString()
	{
		StringBuilder stringBuilder = new StringBuilder();
		for(int i = 0; i < this.materials.size(); i++)
		{
			stringBuilder.append(this.materials.get(i).toString() + " ");
		}
		
		return stringBuilder.toString();
	}
	
	public int size()
	{
		return this.materials.size();
	}

	public void clear() 
	{
		this.materials.clear();
	}

}
