package net.ramslayer.duinomccore.quests;

import net.ramslayer.duinomccore.data.Settings;
import net.ramslayer.duinomccore.quests.util.DailyQuest;
import net.ramslayer.duinomccore.quests.util.Quest;
import net.ramslayer.duinomccore.quests.util.QuestDifficulty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestsManager {
    private final static QuestsManager instance = new QuestsManager();

    private final List<DailyQuest> todayQuests = new ArrayList<>();

    private QuestsManager() {}

    public static QuestsManager getInstance() {
        return instance;
    }

    public List<DailyQuest> getTodayQuests() {
        return todayQuests;
    }

    public void generateQuests() {
        List<DailyQuest> generated = new ArrayList<>();

        generated.add(generate(QuestDifficulty.EASY));
        generated.add(generate(QuestDifficulty.MEDIUM));
        generated.add(generate(QuestDifficulty.HARD));

        setTodayQuests(generated);
    }

    private void setTodayQuests(List<DailyQuest> quests) {
        todayQuests.clear();
        todayQuests.addAll(quests);
    }

    private DailyQuest generate(QuestDifficulty difficulty) {
        List<Quest> quests = new ArrayList<>(Settings.getInstance().getQuests().stream()
                .filter(q -> q.getDifficulty() == difficulty)
                .toList());


        Collections.shuffle(quests);

        return new DailyQuest(quests.getFirst());
    }
}
