package org.anhcraft.enc.api.listeners;

import org.anhcraft.enc.api.ActionReport;
import org.bukkit.entity.LivingEntity;

/**
 * The listener of attack events.
 */
public abstract class SyncAttackListener implements IListener {
    /**
     * This method is called when the attack event happens and the listener determines that the event is related to enchantment.
     * @param report the report
     * @param entity the entity
     * @param damage amount of damage
     */
    public abstract void onAttack(ActionReport report, LivingEntity entity, double damage);
}
