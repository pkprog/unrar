package ru.pk.check;

public class Assert {
    public static void notNull(Object obj, String message) {
        if (obj == null)
            throw new IllegalArgumentException("Argument is null." + message);
    }
}
