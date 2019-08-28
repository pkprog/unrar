package ru.pk.unrar.parser;

import ru.pk.unrar.rarformat.HeaderType;
import ru.pk.unrar.rarformat.MainHeaderFlag;

import java.util.Collection;
import java.util.StringJoiner;

public class Header {
    private HeaderType type;
    private Collection<MainHeaderFlag> mainHeaderFlags;

    public Header(HeaderType type) {
        this.type = type;
    }

    public HeaderType getType() {
        return type;
    }

    public Collection<MainHeaderFlag> getMainHeaderFlags() {
        return mainHeaderFlags;
    }

    public void setMainHeaderFlags(Collection<MainHeaderFlag> mainHeaderFlags) {
        this.mainHeaderFlags = mainHeaderFlags;
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(", ", Header.class.getSimpleName() + "[", "]")
                .add("type=" + type)
                .add("mainHeaderFlags.count=" + (mainHeaderFlags == null ? 0 : mainHeaderFlags.size()));

        StringJoiner sjMhd = new StringJoiner(",", "{", "}");
        if (mainHeaderFlags != null && mainHeaderFlags.size() > 0) {
            for (MainHeaderFlag mhd: mainHeaderFlags) {
                sjMhd.add(mhd.name());
            }
        }
        if (sjMhd.length() > 0) {
            sj.add("MainHeaderFlags=" + sjMhd.toString());
        }

        return sj.toString();
    }
}
