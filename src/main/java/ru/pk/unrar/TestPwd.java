package ru.pk.unrar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.pk.unrar.parser.Reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TestPwd {
    private static Logger log = LoggerFactory.getLogger(TestPwd.class);

    private static String FILE_TO_OPEN = "C:\\Users\\phizhnyakov\\Documents\\авто\\korando\\SSang_SIW.part05.rar";

    public static void main(String ... params) {
        File f = new File(FILE_TO_OPEN);
        if (!f.exists()) {
            log.warn("File not found!!");
            return;
        }
        try (FileInputStream fis = new FileInputStream(f)) {
            log.debug("Ok. File opened: {}", f.getAbsolutePath());

            Reader reader = new Reader(fis);
            reader.read();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
