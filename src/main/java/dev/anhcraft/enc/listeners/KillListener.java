package dev.anhcraft.enc.listeners;

import co.aikar.taskchain.TaskChain;
import dev.anhcraft.enc.ENC;
import dev.anhcraft.enc.api.ActionReport;
import dev.anhcraft.enc.api.Enchantment;
import dev.anhcraft.enc.api.EnchantmentAPI;
import dev.anhcraft.enc.api.listeners.AsyncKillListener;
import dev.anhcraft.enc.api.listeners.SyncKillListener;
import org.anhcraft.spaciouslib.utils.InventoryUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class KillListener implements Listener {
    @EventHandler
    public void death(EntityDeathEvent event){
        if(event.getEntity().getKiller() != null){
            Player killer = event.getEntity().getKiller();
            ItemStack item = killer.getInventory().getItemInMainHand();
            if(!InventoryUtils.isNull(item)) {
                HashMap<Enchantment, Integer> enchants = EnchantmentAPI.listEnchantments(item);
                if(enchants.isEmpty()){
                    return;
                }
                ActionReport report = new ActionReport(killer, item, enchants, false);
                TaskChain<Boolean> listenerChain = ENC.getTaskChainFactory().newChain();
                for(Map.Entry<Enchantment, Integer> e : enchants.entrySet()) {
                    Enchantment enchantment = e.getKey();
                    if(!enchantment.isEnabled() ||
                            !enchantment.isAllowedWorld(killer.getWorld().getName())) {
                        continue;
                    }
                    enchantment.getEventListeners().stream()
                        .filter(eventListener -> eventListener instanceof SyncKillListener)
                        .forEach(eventListener -> {
                            if(eventListener instanceof AsyncKillListener) {
                                listenerChain.async(() -> ((AsyncKillListener) eventListener)
                                        .onKill(report, event.getEntity(), event.getDrops()));
                            } else{
                                listenerChain.sync(() -> ((SyncKillListener) eventListener)
                                        .onKill(report, event.getEntity(), event.getDrops()));
                            }
                        });
                }
                listenerChain.execute();
            }
        }
    }
}
