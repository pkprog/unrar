package ru.pk.unrar.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.pk.check.Assert;
import ru.pk.unrar.exception.ApplicationRuntimeException;
import ru.pk.unrar.rarformat.HeaderType;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Documentation from
 * https://codedread.github.io/bitjs/docs/unrar.html
 * https://www.rarlab.com/technote.htm
 */
public class Reader {
    private static Logger log = LoggerFactory.getLogger(Reader.class);

    private FileInputStream s;

    public Reader(FileInputStream s) {
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
    public void read() throws IOException {
        log.trace("Start reading");

        Volume vSignature = readSignature();

        Volume v;
        int i = 1;
        while ((v = readNextVolume(i++)) != null) {
            Header h = v.getHeader();
            log.trace(h.toString());
            Body b = v.getBody();
        }

        log.trace("End reading");
    }

    private Volume readSignature() throws IOException {
        final int SIGNATURE_LENGTH = 7;
        log.trace("Start read signature");
        byte[] sign = new byte[SIGNATURE_LENGTH];
        int readCnt = s.read(sign);
        if (readCnt != SIGNATURE_LENGTH) {
            throw new ApplicationRuntimeException("signature length <> 7 bytes");
        }

        Volume volume = new Volume(0);

        HeaderType headerType = HeaderType.getHeaderType(sign[2]);
        log.debug("Header-signature type=0x{} - {}", Integer.toHexString(sign[2]), headerType.name());

        int headerSize = sign[6] << 8 | sign[5];
        log.debug("Header-signature size={} bytes", headerSize);
        if (headerSize != SIGNATURE_LENGTH) {
            throw new ApplicationRuntimeException("signature header size <> 7 bytes. But=" + headerSize);
        }

        log.trace("Start read signature");
        return volume;
    }

    private Volume readNextVolume(final int volumeNumber) throws IOException {
        final int BASE_HEAD_LENGTH = 7;
        log.trace("Start read next volume #{}", volumeNumber);

        log.trace("Read header");
        byte[] bytes = new byte[BASE_HEAD_LENGTH];
        int readHeadCnt = s.read(bytes);
        if (readHeadCnt < 0) {
            return null;
        }
        if (readHeadCnt != BASE_HEAD_LENGTH) {
            throw new ApplicationRuntimeException("Header length <> "+ BASE_HEAD_LENGTH +" bytes");
        }


        byte[] headerCrcBytes = Arrays.copyOfRange(bytes, 0, 2);   //header_crc              2 bytes
        byte headerTypeByte = bytes[2];                            //header_type             1 byte
        byte[] headerFlagsBytes = Arrays.copyOfRange(bytes, 3, 5); //header_flags            2 bytes
        byte[] headerSizeBytes = Arrays.copyOfRange(bytes, 5, 7);  //header_size             2 bytes

        Parser p = new Parser(s);
        Volume v = p.parse(headerTypeByte, headerFlagsBytes, headerSizeBytes, volumeNumber);
        return v;
    }

}
