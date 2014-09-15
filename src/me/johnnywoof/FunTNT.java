package me.johnnywoof;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class FunTNT extends JavaPlugin implements Listener{

	private final Random rand = new Random();
	
	private final ArrayList<UUID> blocks = new ArrayList<UUID>();
	
	private final ArrayList<Region> regions = new ArrayList<Region>();
	
	private final ArrayList<BlockData> bdl = new ArrayList<BlockData>();
	
	public void onEnable(){
		
		this.getServer().getPluginManager().registerEvents(this, this);
		
	}
	
	public void onDisable(){
		
		for(World w : this.getServer().getWorlds()){
			
			this.fixWorld(w);
			
		}
		
	}
	
	@SuppressWarnings("deprecation")
	private void fixWorld(World w){
		
		if(!this.blocks.isEmpty()){
		
			for(Entity e : w.getEntities()){
				
				if(e.getType().equals(EntityType.FALLING_BLOCK)){
					
					if(this.blocks.contains(e.getUniqueId())){
					
						e.remove();
						this.blocks.remove(e.getUniqueId());
					
					}
					
				}
				
			}
		
		}
		
		if(!this.bdl.isEmpty()){
			
			for(int i = 0; i < this.bdl.size(); i++){
				
				BlockData bd = this.bdl.get(i);
				
				if(bd.world.equals(w.getName())){
					
					Block b = w.getBlockAt(bd.x, bd.y, bd.z);
					
					if(!b.getChunk().isLoaded()){
						
						if(!b.getChunk().load()){
							
							continue;
							
						}
						
					}
					
					b.setTypeIdAndData(bd.m.getId(), bd.data, false);
					
					this.bdl.remove(i);
					
				}
				
				bd = null;
				
			}
			
		}
		
		if(!this.regions.isEmpty()){
			
			for(int i = 0; i < this.regions.size(); i++){
				
				if(this.regions.get(i).world.equals(w.getName())){
					
					this.regions.remove(i);
					
				}
				
			}
			
		}
		
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onWorldUnload(WorldUnloadEvent event){
		
		this.fixWorld(event.getWorld());
		
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPhysics(BlockPhysicsEvent event){
			
		if(event.getBlock().getType().equals(Material.AIR)){
			
			if(!this.regions.isEmpty()){
				
				for(int i = 0; i < this.regions.size(); i++){
					
					if(this.regions.get(i).isBlockInRegion(event.getBlock())){
						
						event.setCancelled(true);
						return;
						
					}
					
				}
				
			}
			
		}
		
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onBlockFall(EntityChangeBlockEvent event) {
	       
		if(event.getEntityType().equals(EntityType.FALLING_BLOCK)){
			
			if(this.blocks.contains(event.getEntity().getUniqueId())){
				
				event.setCancelled(true);
				
				this.blocks.remove(event.getEntity().getUniqueId());
				
				return;
				
			}
			
		}
		
		if(event.getTo().equals(Material.AIR)){
			
			if(!this.regions.isEmpty()){
				
				for(int i = 0; i < this.regions.size(); i++){
					
					if(this.regions.get(i).isBlockInRegion(event.getBlock())){
						
						event.setCancelled(true);
						return;
						
					}
					
				}
				
			}
			
		}
	 
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler(ignoreCancelled = true)
	public void onEntityExplodeEvent(EntityExplodeEvent event){
		
		Iterator<Block> it = event.blockList().iterator();
		
		int lx = Integer.MAX_VALUE, ly = Integer.MAX_VALUE, lz = Integer.MAX_VALUE, mx = 0, my = Integer.MIN_VALUE, mz = Integer.MIN_VALUE;
		
		while(it.hasNext()){
			
			Block b = it.next();
			
			if(b.getType() != Material.TNT){
				
				if(b.getX() < lx){
					
					lx = (b.getX() - 1);
					
				}
				
				if(b.getY() < ly){
					
					ly = (b.getY() - 1);
					
				}
				
				if(b.getZ() < lz){
					
					lz = (b.getZ() - 1);
					
				}
				
				if(b.getX() > mx){
					
					mx = (b.getX() + 1);
					
				}
				
				if(b.getY() > my){
					
					my = (b.getY() + 1);
					
				}
				
				if(b.getZ() > mz){
					
					mz = (b.getZ() + 1);
					
				}
				
				final Location loc = b.getLocation();
				
				final Material m = b.getType();
				
				final byte data = b.getData();
				
				float x = (float) -1 + (float) (Math.random() * ((1 - -1)+1));
				
		        float y = (float) -2 + (float) (Math.random() * ((2 - -2)+1));
		        
		        float z = (float) -1 + (float) (Math.random() * ((1 - -1)+1));
		   
		        FallingBlock fb = b.getWorld().spawnFallingBlock(b.getLocation(), b.getType(), b.getData());
		        
		        fb.setVelocity(new Vector(x, y, z));
		        
		        fb.setDropItem(false);
		        
		        this.blocks.add(fb.getUniqueId());
		        
		        this.bdl.add(new BlockData(b.getType(), b.getData(), b.getWorld().getName(), b.getX(), b.getY(), b.getZ(), b.getChunk().getX(), b.getChunk().getZ()));
				
				this.getServer().getScheduler().runTaskLater(this, new Runnable(){

					@Override
					public void run() {
						
						if(!loc.getChunk().isLoaded()){
							
							loc.getChunk().load();
							
						}
						
						loc.getBlock().setTypeIdAndData(m.getId(), data, false);
						
						for(int i = 0; i < bdl.size(); i++){
							
							BlockData bd = bdl.get(i);
							
							if(bd.x == loc.getBlockX() && bd.y == loc.getBlockY() && bd.z == loc.getBlockZ() && bd.m.toString().equals(m.toString()) && bd.world.equals(loc.getWorld().getName())){
								
								bdl.remove(i);
								break;
								
							}
							
							bd = null;
							
						}
						
					}
					
				}, (150 + rand.nextInt(200)));
				
				b.setTypeId(Material.AIR.getId(), false);
				
				it.remove();
				
			}
			
		}
		
		it = null;
		
		final Region r = new Region(lx, ly, lz, mx, my, mz, event.getLocation().getWorld().getName());
		
		this.regions.add(r);
		
		this.getServer().getScheduler().runTaskLater(this, new Runnable(){

			@Override
			public void run() {
				
				FunTNT.this.regions.remove(r);
				
			}
			
		}, 400);
		
	}
	
}
