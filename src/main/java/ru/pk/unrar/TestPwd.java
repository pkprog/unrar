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
    private static String DIR1 = "C:\\Users\\phizhnyakov\\Documents";
    private static String DIR2 = "C:\\Users\\phizhnyakov\\Documents\\авто";

    public static void main(String ... params) {
        File d1 = new File(DIR1);
        File d2 = new File(DIR2);
        File f = new File(FILE_TO_OPEN);
        if (!d1.exists()) {
            log.warn("Dir1 not found!!");
            return;
        }
        if (!d2.exists()) {
            log.warn("Dir2 not found!!");
            return;
        }
        if (!f.exists()) {
            log.warn("File not found!!");
            return;
        }
        try (FileInputStream fis = new FileInputStream(f)) {
            log.info("Ok. File opened: {}", f.getAbsolutePath());

            Reader reader = new Reader(fis);
            reader.read();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
