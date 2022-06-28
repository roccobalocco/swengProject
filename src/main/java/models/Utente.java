package models;

/**
 * @author Piemme
 */
public abstract class Utente {

    /**
     * Default constructor
     */
    protected Utente(String cf) {
        this.cf = cf;
    }

    /**
     * 
     */
    private final String cf;

    /**
     */
    public String getCF() {
        return cf;
    }

    public String toString(){
        return "Codice fiscale: " + cf;
    }
}