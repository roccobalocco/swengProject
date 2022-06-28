package models;

/**
 * @author Piemme
 */
public class Admin extends Utente {

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

    /**
     */
    private boolean logAction(String s) {
        // TODO implement here

        return true;
    }

}