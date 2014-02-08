package gipsyking.demeter;

import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeSpecies;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Dispenser;
import org.bukkit.material.CocoaPlant;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Tree;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class Demeter extends JavaPlugin {
	
	protected static final BlockFace[] horizontalFaces = new BlockFace[]{BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
	public static ArrayList<Plantable> plantables = new ArrayList<Plantable>();
	
	public void onEnable(){
		
		
		plantables.add(new Plantable(Material.SEEDS, Material.CROPS, Material.SOIL));
		plantables.add(new Plantable(Material.CARROT_ITEM, Material.CARROT, Material.SOIL));
		plantables.add(new Plantable(Material.POTATO_ITEM, Material.POTATO, Material.SOIL));
		
		plantables.add(new Plantable(Material.NETHER_STALK, Material.NETHER_WARTS, new ValidPlantationCallback() {

			@Override
			public boolean canPlant(World world, Location target) {
				if (world.getBlockAt(target).getType() != Material.SOUL_SAND) {
					return false;
				}
				
				Block blockAbove = world.getBlockAt(target.add(new Vector(0, 1, 0)));
				if (blockAbove.getType() != Material.AIR) {
					return false;
				}
				
				return true;
			}
			
		}));
		
		plantables.add(new Plantable(Material.INK_SACK, 3, Material.COCOA, new ValidPlantationCallback() {

			@Override
			public boolean canPlant(World world, Location target) {
				Block blockAbove = world.getBlockAt(target.add(new Vector(0, 1, 0)));
				if (blockAbove.getType() != Material.AIR) {
					return false;
				}
				
				for (BlockFace face: horizontalFaces) {
					Block surroundingBlock = blockAbove.getRelative(face);
					if (surroundingBlock.getType() == Material.LOG
							&& ((Tree)surroundingBlock.getState().getData()).getSpecies() == TreeSpecies.JUNGLE) {
						return true;
					}
				}
				
				return false;
			}
			
		}, new PlantationCallback() {
			
			@Override
			public void plant(World world, Location target) {
				Block block = world.getBlockAt(target.add(new Vector(0, 1, 0)));
				block.setType(Material.COCOA);
				for (BlockFace face: horizontalFaces) {
					Block surroundingBlock = block.getRelative(face);
					if (surroundingBlock.getType() == Material.LOG
							&& ((Tree)surroundingBlock.getState().getData()).getSpecies() == TreeSpecies.JUNGLE) {
						BlockState state = block.getState();
						((CocoaPlant)state.getData()).setFacingDirection(face);
						state.update();
						return;
					}
				}
				getLogger().log(Level.SEVERE, "Cocoa's ValidPlantationCallback returned valid, but did not find a log to attach cocoa!");
			}
		}));
		
		plantables.add(new Plantable(Material.SAPLING, -1, Material.SAPLING, Material.DIRT, Material.GRASS));
				
		getServer().getPluginManager().registerEvents(new PlantListener(this), this);
	}

	public boolean plantMaterial(Dispenser dispenser, Plantable plantable, MaterialData materialData) {
		Block block = dispenser.getBlock();
		int power = block.getBlockPower();
		getLogger().info("power: " + power);
		BlockFace direction = ((org.bukkit.material.Dispenser)block.getState().getData()).getFacing();
		
		Location target = block.getLocation().clone().add(new Vector(direction.getModX() * power, -2, direction.getModZ() * power));
			
		World world = dispenser.getBlock().getWorld();
		if (plantable.canPlant(world, target)) {
			plantable.plant(world, target, materialData);
			return true;
		}
		
		return false;
	}
}
