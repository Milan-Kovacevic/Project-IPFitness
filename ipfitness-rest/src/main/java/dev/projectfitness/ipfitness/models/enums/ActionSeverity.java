package dev.projectfitness.ipfitness.models.enums;

public enum ActionSeverity {
    INFO(1), EXCEPTION(2), SENSITIVE(3);

    private final Integer level;

    ActionSeverity(Integer level) {
        this.level = level;
    }

    public Integer getLevel() {
        return this.level;
    }
}
