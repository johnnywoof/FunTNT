package me.johnnywoof;

import org.bukkit.Material;

public class BlockData {

	public Material m;
	public byte data;
	public String world;
	public int x, y, z, cx, cz;
	
	public BlockData(String[] data){
		
		this.m = Material.valueOf(data[0].toUpperCase());
		this.data = Byte.valueOf(data[1]);
		this.world = data[2];
		this.x = Integer.parseInt(data[3]);
		this.y = Integer.parseInt(data[4]);
		this.z = Integer.parseInt(data[5]);
		this.cx = Integer.parseInt(data[6]);
		this.cz = Integer.parseInt(data[7]);
		
	}
	
	public BlockData(Material m, byte data, String world, int x, int y, int z, int cx, int cz) {
		
		this.x = x;
		this.y = y;
		this.z = z;
		this.m = m;
		this.data = data;
		this.world = world;
		this.cx = cx;
		this.cz = cz;
		
	}

	@Override
	public String toString(){
		
		return m.toString() + "," + data + "," + world + "," + x + "," + y + "," + z + "," + this.cx + "," + this.cz;
		
	}
	
}
