package ru.pk.unrar.parser;

import ru.pk.unrar.rarformat.FileHeaderFlag;
import ru.pk.unrar.rarformat.HeaderType;
import ru.pk.unrar.rarformat.MainHeaderFlag;

import java.util.Collection;
import java.util.StringJoiner;

public class Header {
    private HeaderType type;
    private Collection<MainHeaderFlag> mainHeaderFlags;
    private Collection<FileHeaderFlag> fileHeaderFlags;

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

    public Collection<FileHeaderFlag> getFileHeaderFlags() {
        return fileHeaderFlags;
    }

    public void setFileHeaderFlags(Collection<FileHeaderFlag> fileHeaderFlags) {
        this.fileHeaderFlags = fileHeaderFlags;
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(", ", Header.class.getSimpleName() + "[", "]")
                .add("type=" + type)
                .add("mainHeaderFlags.count=" + (mainHeaderFlags == null ? 0 : mainHeaderFlags.size()))
                .add("fileHeaderFlags.count=" + (fileHeaderFlags == null ? 0 : fileHeaderFlags.size()));

        StringJoiner sjMhd = new StringJoiner(",", "{", "}");
        if (mainHeaderFlags != null && mainHeaderFlags.size() > 0) {
            for (MainHeaderFlag mhd: mainHeaderFlags) {
                sjMhd.add(mhd.name());
            }
        }
        StringJoiner sjFhd = new StringJoiner(",", "{", "}");
        if (fileHeaderFlags != null && fileHeaderFlags.size() > 0) {
            for (FileHeaderFlag fhd: fileHeaderFlags) {
                sjFhd.add(fhd.name());
            }
        }
        if (sjMhd.length() > 0) {
            sj.add("MainHeaderFlags=" + sjMhd.toString());
        }
        if (sjFhd.length() > 0) {
            sj.add("FileHeaderFlags=" + sjFhd.toString());
        }

        return sj.toString();
    }
}
