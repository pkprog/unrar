package ru.pk.unrar.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.pk.check.Assert;

import java.io.FileInputStream;
import java.io.IOException;

public class Parse1 {
    private static Logger log = LoggerFactory.getLogger(Parse1.class);

    private FileInputStream s;

    public Parse1(FileInputStream s) {
        Assert.notNull(s, "Stream is null");
        this.s = s;
    }

    public void parse() throws IOException {
        log.debug("Start parsing");

        int headerCrc1 = s.read(), headerCrc2 = s.read();
        int headerType = s.read();
        log.debug("Header type=0x{}", Integer.toHexString(headerType));
        byte[] headerFlags = new byte[2];
        int read1 = s.read(headerFlags);
        byte[] headerSizeBytes = new byte[2];
        int read2 = s.read(headerSizeBytes);
        int headerSize = headerSizeBytes[0] << 8 | headerSizeBytes[1];
        log.debug("Found header size={}", headerSize);

        log.debug("End parsing");
    }
}
