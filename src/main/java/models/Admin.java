package models;

import java.util.*;

/**
 * @author Piemme
 */
public class Admin extends Utente {

    /**
     * Default constructor
     */
    private Admin(String cf, String mail) {
        super(cf);
    }

    /**
     * 
     */
    private static Admin uniqueInstance;
    private String mail;

    /**
     * @return
     */
    public static Admin getInstance(String cf, String mail) {
        if(uniqueInstance == null) {
            uniqueInstance = new Admin(cf, mail);
        }
        return uniqueInstance;
    }

    /**
     * @return
     */
    private boolean logAction() {
        // TODO implement here

        return true;
    }

}