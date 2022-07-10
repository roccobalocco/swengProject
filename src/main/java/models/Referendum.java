package models;

import java.time.LocalDate;

/**
 * @author Piemme
 */
public class Referendum extends Votazione {

    /**
     * Default constructor
     */
    public Referendum(String desc, LocalDate scad, boolean q, int id) {
        super(desc, scad, id);
        this.quorum = q;

    }

    public final boolean quorum;
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

    @Override
    public Risultati getRisultati() {
        return null;
    }
}