package models;

import util.Observer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

/**
 * @author Piemme
 */
public class Admin extends Utente implements Observer {

    /**
     * Default constructor
     */
    private Admin(String cf) {
        super(cf);
    }

    /**
     * 
     */
    private static Admin uniqueInstance;

    /**
     */
    public static Admin getInstance(String cf) {
        if(uniqueInstance == null) {
            uniqueInstance = new Admin(cf);
        }
        return uniqueInstance;
    }

    public static Admin getInstance(){
        return uniqueInstance;
    }

    @Override
    public void update(String s) throws IOException {
        File f = new File("/java/resources/logFiles/adminLogs.log");
        FileWriter fw = new FileWriter(f);
        fw.write(LocalDate.now().toString());
        fw.write(Admin.getInstance().toString());
        fw.write(s);
        fw.write("\n");
    }

}