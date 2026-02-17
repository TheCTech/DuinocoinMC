package net.ramslayer.duinomccore.quests.util;

import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;

public class MineBlockQuest extends Quest {

    private final Material material;

    public MineBlockQuest(String id,
                     QuestDifficulty difficulty,
                     Material material,
                     int min_amount,
                     int max_amount,
                     int reward) {

        super(id, difficulty, min_amount, max_amount, reward);
        this.material = material;
    }

    @Override
    public QuestType getType() {
        return QuestType.MINE_BLOCK;
    }

    @Override
    public boolean matches(Event event) {
        if (!(event instanceof BlockBreakEvent e)) {
            return false;
        }

        return e.getBlock().getType() == material;
    }

    public Material getMaterial() {
        return material;
    }
}
