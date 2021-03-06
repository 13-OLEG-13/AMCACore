package com.spleefleague.core.cosmetics;

import com.spleefleague.core.SpleefLeague;
import com.spleefleague.core.chat.Theme;
import com.spleefleague.core.player.SLPlayer;
import com.spleefleague.core.utils.SimpleItemStack;
import com.spleefleague.core.utils.UtilChat;
import com.spleefleague.core.utils.inventorymenu.InventoryMenuTemplate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
@Data
public abstract class CItem {

    private final int id;
    
    private final String name;
    
    private final CType type;
    
    private final List<String> description;
    
    private final int costInCoins, costInPremiumCredits;
    
    private ItemStack icon, selectedIcon, emptyIcon;
    
    public CItem(int id, String name, CType type, int costInCoins, int costInPremiumCredits) {
        this(id, name, type, new ArrayList<>(), costInCoins, costInPremiumCredits);
    }
    
    public CItem(int id, String name, CType type, List<String> description, int costInCoins, int costInPremiumCredits) {
        this.id = id;
        this.name = UtilChat.c("&f%s", name);
        this.type = type;
        this.description = description.stream().map(UtilChat::c).collect(Collectors.toList());
        this.costInCoins = costInCoins;
        this.costInPremiumCredits = costInPremiumCredits;
        
        setupEmptyIcon();
        setupIcon();
        setupSelectedIcon();
    }
    
    public boolean select(Player p) {
        SpleefLeague.getInstance().getPlayerManager().get(p).getCollectibles().addActive(this);
        onSelecting(p);
        return true;
    }
    
    public void buy(Player p, boolean usingCoins, InventoryMenuTemplate submenu) {
        SLPlayer slp = SpleefLeague.getInstance().getPlayerManager().get(p);
        //check for already having
        if(usingCoins)
            if(slp.getCoins() >= costInCoins) {
                slp.changeCoins(-costInCoins);
                SpleefLeague.getInstance().getPlayerManager().get(p).getCollectibles().addItem(id);
                UtilChat.s(Theme.SUCCESS, p, "You have just bought %s &afor &6%d coins&a.", name, costInCoins);
            }else {
                UtilChat.s(Theme.ERROR, p, "You don't have enough coins to buy this.");
            }
        else
            if(slp.getPremiumCredits() >= costInPremiumCredits) {
                slp.changeCoins(-costInPremiumCredits);
                SpleefLeague.getInstance().getPlayerManager().get(p).getCollectibles().addItem(id);
                UtilChat.s(Theme.SUCCESS, p, "You have just bought %s &afor &b%d premium credits&a.", name, costInPremiumCredits);
            }else {
                UtilChat.s(Theme.ERROR, p, "You don't have enough premium credits to buy this.");
            }
        submenu.construct(slp).open();
    }
    
    public abstract void onSelecting(Player p);
    
    public abstract void onRemoving(Player p);
    
    private void setupEmptyIcon() {
        List<String> lore = new ArrayList<>();
        if(!description.isEmpty()) {
            lore.addAll(description);
            lore.add("");
        }
        lore.add(UtilChat.c("&7Left click to buy"));
        lore.add(UtilChat.c("&7it for &6%d coins", costInCoins));
        lore.add("");
        lore.add(UtilChat.c("&7Right click to buy"));
        lore.add(UtilChat.c("&7it for &b%d premium credits", costInPremiumCredits));
        this.emptyIcon = new SimpleItemStack(Material.STAINED_CLAY, getName(), lore, (short) 14);
    }
    
    private void setupIcon() {
        List<String> lore = new ArrayList<>();
        if(!description.isEmpty()) {
            lore.addAll(description);
            lore.add("");
        }
        lore.add(UtilChat.c("&aClick to select!"));
        this.icon = new SimpleItemStack(Material.STAINED_CLAY, getName(), lore, (short) 13);
    }
    
    private void setupSelectedIcon() {
        List<String> lore = new ArrayList<>();
        if(!description.isEmpty()) {
            lore.addAll(description);
            lore.add("");
        }
        lore.add(UtilChat.c("&eAlready selected!"));
        this.selectedIcon = new SimpleItemStack(Material.STAINED_CLAY, getName(), lore, (short) 3);
    }
    
    public boolean isOwnedBy(SLPlayer slp) {
        return slp.getCollectibles().getItems().contains(id);
    }
    
    public boolean isActive(SLPlayer slp) {
        return slp.getCollectibles().getActive().contains(id);
    }
    
    public ItemStack getDisplayItem(SLPlayer slp) {
        if(isActive(slp))
            return selectedIcon;
        if(isOwnedBy(slp))
            return icon;
        return emptyIcon;
    }
    
}
