package gipsyking.demeter;

import org.bukkit.Material;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;


public class PlantListener implements Listener {
	
	private Demeter plugin;
	public PlantListener(Demeter demeter) {
		this.plugin = demeter;
	}

	@EventHandler(priority = EventPriority.HIGH)
    public void onDispense(BlockDispenseEvent event){
		if (event.isCancelled()) return;
		
		if (event.getBlock().getState() instanceof Dispenser) {
			Dispenser dispenser = (Dispenser) event.getBlock().getState();
			
			Material material = event.getItem().getType();
			short durability = event.getItem().getDurability();
			
			for (ItemStack stack: dispenser.getInventory().getContents()) {
				if (stack == null) {
					continue;
				}
				if (stack.getType() != Material.AIR && (stack.getType() != material || stack.getDurability() != durability)) {
					// there are different types of items, ignore
					return;
				}
			}
			
			for (Plantable plantable: Demeter.plantables) {
				if (plantable.seed == material
						&& (plantable.seedDurability == -1 || plantable.seedDurability == durability)) {
					
					boolean result = plugin.plantMaterial(dispenser, plantable, event.getItem().getData());
					if (result) {
						event.setCancelled(true);
						dispenser.getInventory().removeItem(new ItemStack(material, 1, durability));
					}
					return;
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
    public void onDispense(InventoryCloseEvent event){
		if (event.getView().getType() == InventoryType.DISPENSER) {
			Material found = null;
			for (ItemStack stack: event.getInventory().getContents()) {
				if (stack != null) {
					if (found == null) {
						found = stack.getType();
					} else if (stack.getType() != found) {
						return;
					}
				}
			}
			if (found != null) {
				// has exactly only one type of item
				((Player) event.getPlayer()).sendMessage("plants " + found + " at distance equal to redstone power level");
			}
		}
	}
}
