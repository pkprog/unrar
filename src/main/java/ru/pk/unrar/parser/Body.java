package ru.pk.unrar.parser;

public class Body {
    private int size;
    byte[] bytes;

    public Body(int size, byte[] bytes) {
        this.size = size;
        this.bytes = bytes;
    }

}
