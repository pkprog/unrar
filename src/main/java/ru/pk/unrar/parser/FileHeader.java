package ru.pk.unrar.parser;

import ru.pk.unrar.rarformat.FileHeaderFlag;
import ru.pk.unrar.rarformat.HeaderType;

import java.util.Collection;
import java.util.StringJoiner;

public class FileHeader extends Header {
    private Collection<FileHeaderFlag> fileHeaderFlags;

    public FileHeader(HeaderType type) {
        super(type);
    }

    public Collection<FileHeaderFlag> getFileHeaderFlags() {
        return fileHeaderFlags;
    }

    public void setFileHeaderFlags(Collection<FileHeaderFlag> fileHeaderFlags) {
        this.fileHeaderFlags = fileHeaderFlags;
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(", ", FileHeader.class.getSimpleName() + "[", "]");
        sj.add("Base:" + super.toString());
        sj.add("fileHeaderFlags=" + fileHeaderFlags);

        StringJoiner sjFhd = new StringJoiner(",", "{", "}");
        if (fileHeaderFlags != null && fileHeaderFlags.size() > 0) {
            for (FileHeaderFlag fhd: fileHeaderFlags) {
                sjFhd.add(fhd.name());
            }
        }
        sj.add("Flags:" + sjFhd);
        return sj.toString();
    }

}
