package ru.pk.unrar.parser;

public class Volume {
    private int number;
    //
    private Header header;
    private Body body;

    public Volume(int number) {
        this.number = number;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }
}
