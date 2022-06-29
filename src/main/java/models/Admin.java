package models;

import util.Observer;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Scanner;

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
        File f = new File("src/main/resources/logFiles/adminLogs.log");
        FileWriter fw = new FileWriter(f, true);
        BufferedWriter bw =  new BufferedWriter(fw);

        bw.write(LocalDateTime.now() + " - ");
        bw.write(Admin.getInstance().toString() + " - ");
        bw.write(s);
        bw.write("\n");
        bw.close();
    }

}