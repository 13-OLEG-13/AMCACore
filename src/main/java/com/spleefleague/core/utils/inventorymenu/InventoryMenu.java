package com.spleefleague.core.utils.inventorymenu;

import java.util.Map;
import java.util.OptionalInt;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.scheduler.BukkitRunnable;

import com.spleefleague.core.SpleefLeague;
import com.spleefleague.core.player.SLPlayer;
import com.spleefleague.core.utils.function.Dynamic;
import java.util.HashMap;
import java.util.Map.Entry;

public class InventoryMenu extends InventoryMenuComponent implements InventoryHolder {

    private static final int ROWSIZE = 9;

    private final Inventory inventory;
    private final Map<Integer, InventoryMenuComponent> allComponents;
    private final boolean exitOnClickOutside;
    private final boolean menuControls;
    private final Dynamic<Boolean> accessController;
    private final SLPlayer slp;
    private final Map<Integer, InventoryMenuComponent> currentComponents;
    
    protected InventoryMenu(ItemStackWrapper displayItem, String title, Map<Integer, InventoryMenuComponent> components, boolean exitOnClickOutside, boolean menuControls, Dynamic<Boolean> accessController, SLPlayer slp) {
        super(displayItem, Dynamic.getConstant(true));
        this.slp = slp;
        this.allComponents = components;
        this.inventory = Bukkit.createInventory(this, calcRows() * ROWSIZE, title);
        this.exitOnClickOutside = exitOnClickOutside;
        this.menuControls = menuControls;
        this.accessController = accessController;
        this.currentComponents = new HashMap<>();
        setParents();
        populateInventory();
    }
    
    public SLPlayer getOwner() {
        return slp;
    }

    private int calcRows() {
        OptionalInt oInt = allComponents.keySet().stream().mapToInt(i -> i).max();
        int maxIndex = oInt.orElse(0);

        //Normaly it would be (size + ROWSIZE - 1 ) / ROWSIZE but since we have an index no -1
        //and btw its an integer divison round up thingy -> black magic
        int rows = (Math.max(maxIndex, allComponents.size() - 1) + ROWSIZE) / ROWSIZE;
        return rows;
    }

    private void setParents() {
        allComponents.values().forEach(component -> component.setParent(this));
    }

    private void populateInventory() {
        inventory.clear();
        currentComponents.clear();
        allComponents.entrySet().stream().filter((entry) -> (entry.getKey() >= 0 && entry.getValue().isVisible(slp))).forEach((entry) -> {
            currentComponents.put(entry.getKey(), entry.getValue());
        });
        int current = 0;
        for(Entry<Integer, InventoryMenuComponent> entry : allComponents.entrySet()) {
            if(entry.getKey() < 0 && entry.getValue().isVisible(slp)) {
                while(currentComponents.containsKey(current)) {
                    current++;
                }
                currentComponents.put(current, entry.getValue());
            }
        }
        currentComponents.forEach((key, value) -> inventory.setItem(key, value.getDisplayItemWrapper().construct(slp)));
    }

    protected void addMenuControls() {
        if (menuControls) {
            InventoryMenuComponent rootComp = getRoot();

            if (rootComp instanceof InventoryMenu) {
                InventoryMenu rootMenu = (InventoryMenu) rootComp;

                if (getParent() != null) {
                    InventoryMenuItem mainMenuItem = InventoryMenuAPI.item()
                            .displayIcon(Material.MINECART)
                            .displayName(ChatColor.GREEN + "Main Menu")
                            .description("Click to back to the main menu")
                            .onClick(event -> rootMenu.open())
                            .build().construct(slp);

                    allComponents.put(8, mainMenuItem);
                    inventory.setItem(8, mainMenuItem.getDisplayItemWrapper().construct(slp));

                    InventoryMenuItem goBackItem = InventoryMenuAPI.item()
                            .displayIcon(Material.ANVIL)
                            .displayName(ChatColor.GREEN + "Go back")
                            .description("Click to go back one menu level")
                            .onClick(event -> getParent().open())
                            .build().construct(slp);
                    allComponents.put(0, goBackItem);

                    inventory.setItem(0, goBackItem.getDisplayItemWrapper().construct(slp));
                }
            }
        }

    }

    @Override
    public void selected() {
        open();
    }

    public void open() {
        Player player = slp.getPlayer();
        if (!accessController.get(slp)) {
            player.sendMessage(ChatColor.RED + "You are not allowed to open this InventoryMenu");
        }
        else {

            Inventory current = player.getOpenInventory().getTopInventory();

            if (current == null) {
                player.openInventory(inventory);
            }
            else {
                player.closeInventory();

                //wait 1 Tick
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.openInventory(inventory);
                    }
                }.runTask(SpleefLeague.getInstance());
            }
        }
    }

    public void close(Player player) {
        if (inventory.getViewers().contains(player)) {
            player.closeInventory();
            //TODO: Needed?

            inventory.getViewers().forEach(p -> System.out.println(p.getName()));

            inventory.getViewers().remove(player);

            inventory.getViewers().forEach(p -> System.out.println(p.getName()));
        }
    }

    public void selectItem(int index) {
        if (currentComponents.containsKey(index)) {
            InventoryMenuComponent component = currentComponents.get(index);
            component.selected();
        }
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public boolean exitOnClickOutside() {
        return exitOnClickOutside;
    }
    
    public void update() {
        populateInventory();
    }
}
