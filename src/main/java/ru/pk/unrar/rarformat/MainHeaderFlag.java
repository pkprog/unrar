package ru.pk.unrar.rarformat;

public enum MainHeaderFlag {
    MHD_VOLUME(0x0001),
	MHD_COMMENT(0x0002),
	MHD_LOCK(0x0004),
	MHD_SOLID(0x0008),
	MHD_PACK_COMMENT_OR_MHD_NEWNUMBERING(0x0010),
	MHD_AV(0x0020),
	MHD_PROTECT(0x0040),
	MHD_PASSWORD(0x0080),
	MHD_FIRSTVOLUME(0x0100),
	MHD_ENCRYPTVER(0x0200),

    UNKNOWN(-1);

    private int value;

    MainHeaderFlag(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static MainHeaderFlag getFlag(int value) {
        for (MainHeaderFlag t: MainHeaderFlag.values()) {
            if (t.getValue() == value) {
                return t;
            }
        }
        return UNKNOWN;
    }

}
