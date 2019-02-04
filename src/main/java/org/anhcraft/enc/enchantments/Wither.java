package org.anhcraft.enc.enchantments;

import org.anhcraft.enc.api.ActionReport;
import org.anhcraft.enc.api.Enchantment;
import org.anhcraft.enc.api.listeners.AttackEvent;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;

public class Wither extends Enchantment {
    public Wither() {
        super("Wither", new String[]{
                "There is a chance to give your target the wither effect"
        }, "anhcraft", null, 10, EnchantmentTarget.ALL);

        getEventListeners().add(new AttackEvent() {
            @Override
            public void onAttack(ActionReport report, LivingEntity entity, double damage) {
                double chance = computeConfigValue("chance", report)/100d;
                if(Math.random() < chance){
                    int level = (int) Math.ceil(computeConfigValue("effect_level", report));
                    int duration = (int) Math.ceil(computeConfigValue("effect_duration", report));
                    entity.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, duration, level));
                }
            }
        });
    }

    @Override
    public void onRegistered(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("chance", "{level}*3.5");
        map.put("effect_level", "{level}*0.25");
        map.put("effect_duration", "{level} > ({max_level}/2) ? 40 : 60");
        initConfigEntries(map);
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        return true;
    }
}