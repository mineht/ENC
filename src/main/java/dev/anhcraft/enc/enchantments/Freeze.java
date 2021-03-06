package dev.anhcraft.enc.enchantments;

import dev.anhcraft.enc.ENC;
import dev.anhcraft.enc.api.ActionReport;
import dev.anhcraft.enc.api.Enchantment;
import dev.anhcraft.enc.api.listeners.AsyncAttackListener;
import dev.anhcraft.enc.utils.UnitUtils;
import org.anhcraft.spaciouslib.annotations.AnnotationHandler;
import org.anhcraft.spaciouslib.annotations.PlayerCleaner;
import org.anhcraft.spaciouslib.internal.listeners.PlayerListener;
import org.anhcraft.spaciouslib.utils.PlayerUtils;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class Freeze extends Enchantment {
    @PlayerCleaner
    private static final HashMap<UUID, Long> DATA = new HashMap<>();

    public Freeze() {
        super("Freeze", new String[]{
                "There is a chance to freeze your enemy in a short time"
        }, "anhcraft", null, 3, EnchantmentTarget.ALL);

        AnnotationHandler.register(Freeze.class, null);

        new BukkitRunnable() {
            @Override
            public void run() {
                Iterator<Map.Entry<UUID, Long>> entries = DATA.entrySet().iterator();
                while(entries.hasNext()){
                    Map.Entry<UUID, Long> entry = entries.next();
                    if(System.currentTimeMillis() > entry.getValue()){
                        PlayerListener.freezedPlayers.remove(entry.getKey());
                        entries.remove();
                    }
                }
            }
        }.runTaskTimerAsynchronously(ENC.getInstance(), 0, 20);

        getEventListeners().add(new AsyncAttackListener() {
            @Override
            public void onAttack(ActionReport report, LivingEntity entity, double damage) {
                if(report.isPrevented() || DATA.containsKey(entity.getUniqueId())
                        || !(entity instanceof Player)){
                    return;
                }
                double chance = computeConfigValue("chance", report)/100d;
                if(Math.random() > chance){
                    return;
                }
                PlayerUtils.freeze((Player) entity);
                long duration = (long) UnitUtils.tick2ms(computeConfigValue("duration", report));
                DATA.put(entity.getUniqueId(), System.currentTimeMillis()+duration);
            }
        });
    }

    @Override
    public void onRegistered(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("chance", "{level}*12");
        map.put("duration", "{level}*50");
        initConfigEntries(map);
    }
}