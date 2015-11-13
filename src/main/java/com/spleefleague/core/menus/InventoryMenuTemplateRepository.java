package com.spleefleague.core.menus;

import static com.spleefleague.core.utils.inventorymenu.InventoryMenuAPI.item;
import static com.spleefleague.core.utils.inventorymenu.InventoryMenuAPI.menu;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import com.spleefleague.core.SpleefLeague;
import com.spleefleague.core.chat.Theme;
import com.spleefleague.core.player.Rank;
import com.spleefleague.core.plugin.GamePlugin;
import com.spleefleague.core.utils.inventorymenu.InventoryMenu;
import com.spleefleague.core.utils.inventorymenu.InventoryMenuTemplate;

public class InventoryMenuTemplateRepository {

    public static InventoryMenuTemplate modMenu, slMenu, testMenu;
    
    public static void initTemplates() {
//        slMenu = menu()
//            .title("SpleefLeague Menu")
//            .displayIcon(Material.SIGN)
//            .displayName("SL Menu")
//            .description("A menu for doing")
//            .description("various things")
//            .component(0, 0, menu()
//                .displayName("Options")
//                .displayIcon(Material.GHAST_TEAR)
//                .description("Change your settings")
//                .component(0, 0, item()
//                    .displayName("M")
//                    .displayIcon(Material.REDSTONE_TORCH_ON)
//                    .description("Reloads the server")
//                    .onClick(event -> {
//                        Bukkit.reload();
//                    })
//                )
//            ).build();
    	modMenu = menu()
            .title("ModMenu")
            .displayIcon(Material.GHAST_TEAR)
            .displayName("ModMenu")
            .description("A selection of")
            .description("tools for")
            .description("moderational purposes")
            .rank(Rank.MODERATOR)
            
            .component(0, 0, menu()
            	.title("Reload Menu")
                .displayName("Reload Menu")
                .displayIcon(Material.WATCH)
                .description("Reloading various things")
                .component(2, 0, item()
                    .displayName("Server")
                    .displayIcon(Material.REDSTONE_TORCH_ON)
                    .description("Reloads the server")
                    .onClick(event -> {
                    	event.getPlayer().closeInventory();
                    	
                        Bukkit.reload(); 
                    })
                ).component(3, 0, item()
                    .displayName("Ranks")
                    .displayIcon(Material.BOOK_AND_QUILL)
                    .description("Reloads all ranks")
                    .onClick(event -> {
                        event.getPlayer().closeInventory();
                        
                        Rank.init();
                        SpleefLeague.getInstance().getPlayerManager().getAll().stream().forEach((slp) -> {
                            slp.setRank(Rank.valueOf(slp.getRank().getName()));
                        });
                        
                        event.getPlayer().sendMessage(Theme.SUCCESS + "Reloaded " + Rank.values().length + " ranks!");
                    })
                )
                .menuControls(true)
            )
            
            .component(1, 0, item()
                .displayName("Cancel all")
                .displayIcon(Material.DIAMOND_SPADE)
                .description("Cancels all currently")
                .description("running matches")
                .onClick(event -> {
                	event.getPlayer().closeInventory();
                	
                    GamePlugin.cancelAllMatches();    
                    
                    event.getPlayer().sendMessage(Theme.SUCCESS + "All games have been cancelled.");
                })
            )
            
            
            //TODO: Implement after new Queue Implementation
            /*
            .component(3,0, menu()
            		.title("Queues - Spleef")
            		.displayName("Queues  - Spleef")
            		.displayIcon(Material.GOLD_SPADE))
            		.description("Queue Status for Spleef")
            		.menuControls(true)
            		.dynamicComponents(dynamic ->{
            			CorePlugin spleefPlugin = null;
		                for(CorePlugin cp : CorePlugin.getAll()) {
		                     if(cp.getName().equals("SuperSpleef"))
		                        spleefPlugin = cp;
		                }
			            
		               
		                
			               
			              
            			
            			
            		})
            		
             .component(4,0, menu()
            		.title("Queues - SuperJump")
            		.displayName("Queues  - SuperJump")
            		.displayIcon(Material.GOLD_BOOTS))
            		.description("Queue Status for SuperJump")
            		.menuControls(true)
            		.dynamicComponents(dynamic ->{
            			CorePlugin jumpPlugin = null;
            			for(CorePlugin cp : CorePlugin.getAll()) {
            				if(cp.getName().equals("SuperJump")) {
            					jumpPlugin = cp;
            				}
            			}

			            
		               
		                
			               
			              
            			
            			
            		})
            		
            		*/
            		
            .component(8, 1, item()
                .displayName("Firework")
                .displayIcon(Material.FIREWORK)
                .description("Fires a random firework")
                .description("at the players location")
                .onClick(event -> {
                    Player p = event.getPlayer();
                    Firework fw = (Firework) p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
                    FireworkMeta fwm = fw.getFireworkMeta();

                    Random r = new Random();
                    Type type;

                    switch (r.nextInt(5)) {
                        case 0:
                            type = Type.BALL;
                            break;
                        case 1:
                            type = Type.BALL_LARGE;
                            break;
                        case 2:
                            type = Type.BURST;
                            break;
                        case 3:
                            type = Type.CREEPER;
                            break;
                        case 4:
                            type = Type.STAR;
                            break;
                        default:
                            type = Type.BALL;
                    }

                    FireworkEffect fwe = FireworkEffect.builder()
                    .flicker(r.nextBoolean())
                    .withColor(Color.fromRGB(r.nextInt(256), r.nextInt(256), r.nextInt(256)))
                    .withFade(Color.fromRGB(r.nextInt(256), r.nextInt(256), r.nextInt(256)))
                    .with(type)
                    .trail(r.nextBoolean())
                    .build();

                    fwm.addEffect(fwe);
                    fwm.setPower(r.nextInt(3) + 1);

                    fw.setFireworkMeta(fwm);
                })
            ).build();
    }

    public static void showModMenu(Player p) {
        InventoryMenu menu = modMenu.construct(SpleefLeague.getInstance().getPlayerManager().get(p));
        menu.open();
    }
    
    public static void showSLMenu(Player p) {
        InventoryMenu menu = slMenu.construct(SpleefLeague.getInstance().getPlayerManager().get(p));
        menu.open();
    }
}