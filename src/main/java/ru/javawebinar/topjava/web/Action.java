package ru.javawebinar.topjava.web;

public enum Action {
    CREATE, EDIT, DELETE;

    public static Action parseAction(String value) {
        if (value == null) {
            return null;
        }
        try {
            return Action.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("action " + value + " is illegal");
        }
    }
}
