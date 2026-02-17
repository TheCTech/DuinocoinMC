package net.ramslayer.duinomccore.quests.util;

import java.time.LocalDate;

public class DailyQuest {
    private final Quest quest;
    private final LocalDate date;

    public DailyQuest(Quest quest) {
        this.quest = quest;
        this.date = LocalDate.now();
    }

    public LocalDate getDate() {
        return date;
    }

    public Quest getQuest() {
        return quest;
    }
}
