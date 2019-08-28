package ru.pk.unrar.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.pk.check.Assert;
import ru.pk.unrar.exception.ApplicationRuntimeException;
import ru.pk.unrar.rarformat.FileHeaderFlag;
import ru.pk.unrar.rarformat.HeaderType;
import ru.pk.unrar.rarformat.MainHeaderFlag;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Documentation from
 * https://codedread.github.io/bitjs/docs/unrar.html
 */
public class Parser {
    private static Logger log = LoggerFactory.getLogger(Parser.class);

    private FileInputStream s;

    public Parser(FileInputStream s) {
        Assert.notNull(s, "Stream is null");
        this.s = s;
    }

    /**
     * Overall .RAR file format:
     *
     * signature               7 bytes    (0x52 0x61 0x72 0x21 0x1A 0x07 0x00)
     * [1st volume header]
     * ...
     * [2nd volume header]
     * ...
     * ...
     * [nth volume header]
     * ...
     *
     * In general, a modern single-volume RAR file has a MAIN_HEAD structure followed by multiple FILE_HEAD structures.
     * @throws IOException
     */
    public void parse() throws IOException {
        log.debug("Start parsing");

        Volume v;
        int i = 1;
        while ((v = nextVolume(i++)) != null) {
            Header h = v.getHeader();
            log.trace(h.toString());
            Body b = v.getBody();
        }

        log.debug("End parsing");
    }

    private Volume nextVolume(final int volumeNumber) throws IOException {
        log.trace("Start read next volume #{}", volumeNumber);

        Volume volume = new Volume(volumeNumber);

        log.trace("Read header");
        byte[] headerCrcBytes = new byte[2];
        int read1 = s.read(headerCrcBytes);
        if (read1 < 0) {
            return null;
        }

        int headerType = s.read();
        log.debug("Header type=0x{}", Integer.toHexString(headerType));
        Header header = new Header(HeaderType.getHeaderType(headerType));

        byte[] headerFlagsBytes = new byte[2];
        int read2 = s.read(headerFlagsBytes);
        if (HeaderType.MAIN_HEAD.equals(header.getType())) {
            header.setMainHeaderFlags(parseMainHeaderFlags(headerFlagsBytes));
        } else if (HeaderType.FILE_HEAD.equals(header.getType())) {
            header.setFileHeaderFlags(parseFileHeaderFlags(headerFlagsBytes));
        }

        byte[] headerSizeBytes = new byte[2];
        int read3 = s.read(headerSizeBytes);
        int headerSize = headerSizeBytes[0] << 8 | headerSizeBytes[1];
        log.debug("Found header size={} bytes", headerSize);

        volume.setHeader(header);

        byte[] bodyBytes = null;
        if (headerSize - 7 > 0) {
            bodyBytes = new byte[headerSize - 7];
            int read4 = s.read(bodyBytes);
        }

        //Если тело есть
        if (bodyBytes != null) {
            log.trace("Body read. Size={}", bodyBytes.length);
            Body body = new Body(bodyBytes.length, bodyBytes);
            volume.setBody(body);
        }

        log.trace("End read next volume #{}", volumeNumber);

        return volume;
    }

    private Collection<MainHeaderFlag> parseMainHeaderFlags(byte[] bytes) {
        if (bytes.length != 2) {
            throw new ApplicationRuntimeException("bytes.lenght <> 2 for Main Header flags");
        }

        int allFlags = bytes[0] << 8 | bytes[1];

        List<MainHeaderFlag> flagsResult = new LinkedList<>();
        for (MainHeaderFlag f: MainHeaderFlag.values()) {
            if ((f.getValue() & allFlags) != 0) {
                flagsResult.add(f);
            }
        }

        return flagsResult;
    }

    private Collection<FileHeaderFlag> parseFileHeaderFlags(byte[] bytes) {
        if (bytes.length != 2) {
            throw new ApplicationRuntimeException("bytes.lenght <> 2 for File Header flags");
        }

        return Collections.emptyList();
    }

}
