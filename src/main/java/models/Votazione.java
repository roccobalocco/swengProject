package models;

import java.util.*;

/**
 * @author Piemme
 */
public abstract class Votazione {

    /**
     * Default constructor
     */
    public Votazione(String desc, Date scad) {
        descrizione = desc;
        scadenza = scad;
    }

    public final String descrizione;
    public final Date scadenza;

    /**
     * 
     */
    public void getRisultati() {
        // TODO implement here
    }

}