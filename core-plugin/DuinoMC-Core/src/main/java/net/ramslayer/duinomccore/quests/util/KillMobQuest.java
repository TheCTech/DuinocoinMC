package net.ramslayer.duinomccore.quests.util;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDeathEvent;

public class KillMobQuest extends Quest {

    private final EntityType entity;

    public KillMobQuest(String id,
                          QuestDifficulty difficulty,
                          EntityType entity,
                          int min_amount,
                          int max_amount,
                          int reward) {

        super(id, difficulty, min_amount, max_amount, reward);
        this.entity = entity;
    }

    @Override
    public QuestType getType() {
        return QuestType.KILL_MOB;
    }

    @Override
    public boolean matches(Event event) {

        if (!(event instanceof EntityDeathEvent e))
            return false;

        // Assert killer
        if (e.getEntity().getKiller() == null)
            return false;

        return e.getEntityType() == entity;
    }
}
