package gipsyking.demeter;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

public class PlantableConfiguration {
	public static ValidPlantationCallback farmCallback;
	
	public static ValidPlantationCallback getValidSoilCallback(final Material soil) {
		if (soil == Material.SOIL) {
			PlantableConfiguration.farmCallback = PlantableConfiguration._getValidSoilCallback(Material.SOIL);
			return PlantableConfiguration.farmCallback;
		}
		
		return _getValidSoilCallback(soil);
	}
	
	private static ValidPlantationCallback _getValidSoilCallback(final Material soil) {
		return new ValidPlantationCallback() {

			@Override
			public boolean canPlant(World world, Location target) {
				if (world.getBlockAt(target).getType() != soil) {
					return false;
				}
				
				Block blockAbove = world.getBlockAt(target.add(new Vector(0, 1, 0)));
				if (blockAbove.getType() != Material.AIR) {
					return false;
				}
				
				return true;
			}
			
		};
	}

	public static ValidPlantationCallback getValidSoilCallback(final ArrayList<Material> soils) {
		return new ValidPlantationCallback() {

			@Override
			public boolean canPlant(World world, Location target) {
				if (!soils.contains(world.getBlockAt(target).getType())) {
					return false;
				}
				
				Block blockAbove = world.getBlockAt(target.add(new Vector(0, 1, 0)));
				if (blockAbove.getType() != Material.AIR) {
					return false;
				}
				
				return true;
			}
			
		};
	}
}
