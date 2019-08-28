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
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Documentation from
 * https://codedread.github.io/bitjs/docs/unrar.html
 * https://www.rarlab.com/technote.htm
 */
public class Parser {
    private static Logger log = LoggerFactory.getLogger(Parser.class);

    private FileInputStream s;

    public Parser(FileInputStream s) {
        Assert.notNull(s, "Stream is null");
        this.s = s;
    }

    public Volume parse(byte headerTypeByte, byte[] headerFlagsBytes, byte[] headerSizeBytes, int volumeNumber) throws IOException {
        final int BASE_HEAD_LENGTH = 7;
        log.debug("Start parsing");

        HeaderType headerType = HeaderType.getHeaderType(headerTypeByte);
        log.debug("Header type=0x{} - {}", Integer.toHexString(headerTypeByte), headerType.name());
        int headerSize = headerSizeBytes[1] << 8 | headerSizeBytes[0];
        log.debug("Found header size={} bytes", headerSize);

        Body body = null;
        if (headerSize - 7 > 0) {
            byte[] bodyBytes = new byte[headerSize - BASE_HEAD_LENGTH];
            int readBodyCnt = s.read(bodyBytes);

            log.trace("Body read. Size={}", readBodyCnt);
            body = new Body(readBodyCnt, bodyBytes);
        }

        Volume volume = new Volume(volumeNumber);
        Header basicHeader = parseBasicHeader(headerTypeByte, headerFlagsBytes, headerSizeBytes);
        if (HeaderType.FILE_HEAD.equals(headerType)) {
            volume.setHeader(parseFileHeader(basicHeader, headerFlagsBytes, body == null ? null : body.getBytes()));
        } else {
            volume.setHeader(basicHeader);
        }
        volume.setBody(body);

        log.debug("End parsing");
        return volume;
    }

    private FileHeader parseFileHeader(Header basicHeader, byte[] headerFlagsBytes, byte[] bodyBytes) {
        FileHeader header = new FileHeader(HeaderType.FILE_HEAD);
        header.setFileHeaderFlags(parseFileHeaderFlags(headerFlagsBytes));

        if (bodyBytes == null) {
            return header;
        }

        byte[] packSizeBytes = Arrays.copyOfRange(bodyBytes, 0, 4);
        byte[] unpackSizeBytes = Arrays.copyOfRange(bodyBytes, 4, 8);
        byte[] hostOsBytes = Arrays.copyOfRange(bodyBytes, 8, 9);
        byte[] fileCrcBytes = Arrays.copyOfRange(bodyBytes, 9, 13);
        byte[] fileTimeBytes = Arrays.copyOfRange(bodyBytes, 13, 17);
        byte[] unpVersBytes = Arrays.copyOfRange(bodyBytes, 17, 18);
        byte[] methodBytes = Arrays.copyOfRange(bodyBytes, 18, 19);
        byte[] nameSizeBytes = Arrays.copyOfRange(bodyBytes, 19, 21);
        byte[] fileAttrBytes = Arrays.copyOfRange(bodyBytes, 21, 25);
        //HighPackSize
        //HighUnpSize
        int nameSize = nameSizeBytes[1] << 8 | nameSizeBytes[0];
        byte[] fileNameBytes = Arrays.copyOfRange(bodyBytes, 25, 25+nameSize);

        log.debug("unpVers={}", unpVersBytes);
        log.debug("File name={}" + new String(fileNameBytes));

        return header;
    }

    private Collection<MainHeaderFlag> parseMainHeaderFlags(byte[] bytes) {
        if (bytes.length != 2) {
            throw new ApplicationRuntimeException("bytes.lenght <> 2 for Main Header flags");
        }

        int allFlags = bytes[1] << 8 | bytes[0];

        List<MainHeaderFlag> flagsResult = new LinkedList<>();
        for (MainHeaderFlag f: MainHeaderFlag.values()) {
            if (!MainHeaderFlag.UNKNOWN.equals(f) && (f.getValue() & allFlags) != 0) {
                flagsResult.add(f);
            }
        }

        return flagsResult;
    }

    private Collection<FileHeaderFlag> parseFileHeaderFlags(byte[] bytes) {
        if (bytes.length != 2) {
            throw new ApplicationRuntimeException("bytes.lenght <> 2 for File Header flags");
        }

        int allFlags = bytes[1] << 8 | bytes[0];

        List<FileHeaderFlag> flagsResult = new LinkedList<>();
        for (FileHeaderFlag f: FileHeaderFlag.values()) {
            if (!FileHeaderFlag.UNKNOWN.equals(f) && (f.getValue() & allFlags) != 0) {
                flagsResult.add(f);
            }
        }

        return flagsResult;
    }

    private Header parseBasicHeader(byte headerTypeByte, byte[] headerFlagsBytes, byte[] headerSizeBytes) {
        HeaderType headerType = HeaderType.getHeaderType(headerTypeByte);
        log.debug("Header type=0x{} - {}", Integer.toHexString(headerTypeByte), headerType.name());
        int headerSize = headerSizeBytes[1] << 8 | headerSizeBytes[0];
        log.debug("Found header size={} bytes", headerSize);

        Header header = new Header(headerType);
        header.setMainHeaderFlags(parseMainHeaderFlags(headerFlagsBytes));
        return header;
    }

}
