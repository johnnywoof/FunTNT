package me.johnnywoof;

import org.bukkit.block.Block;


public class Region {

	private int x, y, z, mx, my, mz;
	public String world;
	
	public Region(int x, int y, int z, int mx, int my, int mz, String world){
		this.x = x;
		this.y = y;
		this.z = z;
		this.mx = mx;
		this.my = my;
		this.mz = mz;
		this.world = world;
	}
	
	public Region(String[] data){
		
		this.x = Integer.parseInt(data[0]);
		this.y = Integer.parseInt(data[1]);
		this.z = Integer.parseInt(data[2]);
		this.mx = Integer.parseInt(data[3]);
		this.my = Integer.parseInt(data[4]);
		this.mz = Integer.parseInt(data[5]);
		this.world = data[6];
		
	}
	
	public String toWriteAble(){
		
		return this.x + "," + this.y + "," + this.z + "," + this.mx + "," + this.my + "," + this.mz + "," + this.world;
		
	}
	
	public int getMX(){
		return this.mx;
	}
	
	public int getMY(){
		return this.my;
	}
	
	public int getMZ(){
		return this.mz;
	}
	
	public int getX(){
		return this.x;
	}
	
	public int getY(){
		return this.y;
	}
	
	public int getZ(){
		return this.z;
	}
	
	public boolean isBlockInRegion(Block b){
		
		return b.getWorld().getName().equals(this.world) && (b.getX() >= this.x && b.getX() <= this.mx && b.getY() >= this.y && b.getY() <= this.my && b.getZ() >= this.z && b.getZ() <= this.mz);
	}
	
}
