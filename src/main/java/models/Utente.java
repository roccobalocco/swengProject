package models;

import java.util.*;

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
    private String cf;

    /**
     * @return
     */
    public String getCF() {
        return cf;
    }

}