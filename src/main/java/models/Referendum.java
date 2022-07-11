package models;

import java.time.LocalDate;

/**
 * @author Piemme
 * Classe atta a semplificare la gestione del tipo di Votazione Referendum, poche aggiunte rispetto alla sua super-classe
 */
public class Referendum extends Votazione {

    /**
     * Default constructor
     */
    public Referendum(String desc, LocalDate scad, boolean q, int id) {
        super(desc, scad, id);
        this.quorum = q;

    }

    private final boolean quorum;

    /**
     *
     * @return true se il referendum ha il quorum, false altrimenti
     */
    public boolean hasQuorum() { return quorum; }

    public String toString(){
        StringBuilder s = new StringBuilder("Referendum per " + descrizione + "\n");
        if(hasQuorum())
            s.append("Con quorum -/- ");
        else
            s.append("Senza quorum -/- ");
        s.append("Scadenza: ").append(getScadenza());
        return s.toString();
    }

}