package gipsyking.demeter;

import org.bukkit.Location;
import org.bukkit.World;

public interface ValidPlantationCallback {
	boolean canPlant(World world, Location target);
}
