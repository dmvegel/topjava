package ru.javawebinar.topjava.model;

import java.time.LocalDateTime;

public class UserMealWithExcess {
    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    private ExcessHolder excessHolder;

    private UserMealWithExcess(LocalDateTime dateTime, String description, int calories) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public UserMealWithExcess(LocalDateTime dateTime, String description, int calories, boolean excess) {
        this(dateTime, description, calories);
        this.excessHolder = new ExcessHolder(excess);
    }

    public UserMealWithExcess(LocalDateTime dateTime, String description, int calories, ExcessHolder excessHolder) {
        this(dateTime, description, calories);
        this.excessHolder = excessHolder;
    }

    public void setExcessHolder(ExcessHolder holder) {
        this.excessHolder = holder;
    }

    public static class ExcessHolder {
        private boolean excess;

        public ExcessHolder(boolean excess) {
            this.excess = excess;
        }

        public boolean getExcess() {
            return excess;
        }

        public void setExcess(boolean val) {
            this.excess = val;
        }
    }

    @Override
    public String toString() {
        return "UserMealWithExcess{" +
                "dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + excessHolder.getExcess() +
                '}';
    }
}
