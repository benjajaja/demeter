package gipsyking.demeter;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

public class Plantable {

	public Material seed;
	public int seedDurability;
	private Material crop;
	private ValidPlantationCallback callback;
	private PlantationCallback plantCallback;
	

	public Plantable(Material seed, int durability, Material crop, ValidPlantationCallback callback, PlantationCallback plantCallback) {
		this.seed = seed;
		this.seedDurability = durability;
		this.crop = crop != null ? crop : seed;
		this.callback = callback;
		this.plantCallback = plantCallback;
	}

	public Plantable(Material seeds, Material crops, Material soil) {
		this(seeds, 0, crops, PlantableConfiguration.getValidSoilCallback(soil), null);
	}
	
	public Plantable(Material seeds, Material crops, ValidPlantationCallback callback) {
		this(seeds, 0, crops, callback, null);
	}
	
	public Plantable(Material seeds, int durability, Material crops, Material ...args) {
		this(seeds, durability, crops, PlantableConfiguration.getValidSoilCallback(new ArrayList<Material>(Arrays.asList(args))), null);
		
	}

	public boolean canPlant(World world, Location target) {
		return this.callback.canPlant(world, target.clone());
	}

	public void plant(World world, Location target, MaterialData materialData) {
		if (this.plantCallback != null) {
			this.plantCallback.plant(world, target);
		} else {
			Block block = world.getBlockAt(target.add(new Vector(0, 1, 0)));
			block.setType(crop);
			if (this.seedDurability != 0) {
				BlockState state = block.getState();
				state.setData(materialData);
				state.update();
			}
		}
		
	}

}
