package models;

import java.time.LocalDate;

/**
 * @author Piemme
 */
public abstract class Votazione {

    /**
     * Default constructor
     */
    public Votazione(String desc, LocalDate scad, int id) {
        descrizione = desc;
        scadenza = scad;
        this.id = id;
    }

    private final int id;
    public final String descrizione;
    private final LocalDate scadenza;

    /**
     * @return true se é passata la data di scadenza, false altrimenti
     */
    public boolean fineVotazione(){
        return scadenza.isBefore(LocalDate.now());
    }

    public int getId(){ return id; }

    public String getScadenza() {
        return scadenza.toString();
    }

    public LocalDate getScadenzaLD(){ return scadenza; }
}