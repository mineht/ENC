package org.anhcraft.enc.api.listeners;

import org.anhcraft.enc.api.ActionReport;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * The listener of kill events.
 */
public abstract class SyncKillListener implements IListener {
    /**
     * This method is called when the kill event happens and the listener determines that the event is related to enchantment.
     * @param report the report
     * @param entity the entity
     * @param drops stacks of items which will drop when the entity dies
     */
    public abstract void onAttack(ActionReport report, LivingEntity entity, List<ItemStack> drops);
}

