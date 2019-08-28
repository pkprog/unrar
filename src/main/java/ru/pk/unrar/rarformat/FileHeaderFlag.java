package ru.pk.unrar.rarformat;

public enum FileHeaderFlag {
    LHD_SPLIT_BEFORE(0x0001),
	LHD_SPLIT_AFTER(0x0002),
	LHD_PASSWORD(0x0004),
	LHD_COMMENT(0x0008),
	LHD_SOLID(0x0010),
	LHD_LARGE(0x0100),
	LHD_UNICODE(0x0200),
	LHD_SALT(0x0400),
	LHD_VERSION(0x0800),
	LHD_EXTTIME(0x1000),
	LHD_EXTFLAGS(0x2000),

    UNKNOWN(-1);

    private int value;

    FileHeaderFlag(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
