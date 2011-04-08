package com.elmakers.mine.bukkit.plugins.wandmin;

import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;

public class WandminPlayerListener extends PlayerListener 
{
	@Override
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if (event.getPlayer().getInventory().getItemInHand().getTypeId() == plugin.getWandTypeId())
        {
            WandPermissions permissions = plugin.getPermissions(event.getPlayer().getName());   
            if (!permissions.canUse())
            {
                return;
            }
            
            PlayerWandList wands = plugin.getPlayerWands(event.getPlayer());
            Wand wand = wands.getCurrentWand();
            if (wand == null)
            {
                return;
            }
            
            if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK)
            {
                wand.use(plugin, event.getPlayer());
            }
            else if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
            {
                wand.nextCommand();
                event.getPlayer().sendMessage(" " + wand.getName() + " : " + wand.getCurrentCommand().getName());
            }           
        }
    }

    private WandminPlugin plugin;
	
	public void setPlugin(WandminPlugin plugin)
	{
		this.plugin = plugin;
	}
	
    /**
     * Called when a player joins a server
     *
     * @param event Relevant event details
     */
    @Override
    public void onPlayerJoin(PlayerJoinEvent event) 
    {
    	PlayerWandList wands = plugin.getPlayerWands(event.getPlayer().getName());
    	wands.setPlayer(event.getPlayer());
    	plugin.save();
    }

    /**
     * Called when a player leaves a server
     *
     * @param event Relevant event details
     */
    @Override
    public void onPlayerQuit(PlayerQuitEvent event) 
    {
    	PlayerWandList wands = plugin.getPlayerWands(event.getPlayer().getName());
    	wands.setPlayer(null);
    	plugin.save();
    }
}
