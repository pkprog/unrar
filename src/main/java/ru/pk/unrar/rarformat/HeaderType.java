package ru.pk.unrar.rarformat;

/**
 * The header type is 8 bits (1 byte) and can have the following values:
 */
public enum HeaderType {
    MARK_HEAD(0x72),
    MAIN_HEAD(0x73),
	FILE_HEAD(0x74),
	COMM_HEAD(0x75),
	AV_HEAD(0x76),
	SUB_HEAD(0x77),
	PROTECT_HEAD(0x78),
	SIGN_HEAD(0x79),
	NEWSUB_HEAD(0x7a),
	ENDARC_HEAD(0x7b),

    UNKNOWN(-1)
    ;

    private byte value;

    HeaderType(int value) {
        this.value = (byte) value;
    }

    public byte getValue() {
        return value;
    }

    public static HeaderType getHeaderType(byte value) {
        for (HeaderType t: HeaderType.values()) {
            if (t.getValue() == value) {
                return t;
            }
        }
        return UNKNOWN;
    }
    public static HeaderType getHeaderType(int value) {
        return getHeaderType((byte) value);
    }
}
