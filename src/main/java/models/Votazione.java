package models;

import java.time.LocalDateTime;
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
    private final Date scadenza;

    /**
     * @return true se é passata la data di scadenza, false altrimenti
     */
    public boolean fineVotazione(){
        return scadenza.after(new Date());
    }

    /**
     * 
     */
    public void getRisultati() {
        // TODO implement here
    }

    public String getScadenza() {
        return scadenza.toString();
    }
}