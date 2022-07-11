package models;

import java.time.LocalDate;

/**
 * @author Piemme
 */
public class Classica extends Votazione {

    /**
     * Default constructor
     */
    public Classica(String desc, LocalDate scad, boolean o, boolean p, int id, boolean a) {
        super(desc, scad, id);
        ordinale = o;
        preferenza = p;
        assoluta = a;
    }

    private final boolean ordinale;
    private final boolean preferenza;
    private final boolean assoluta;

    /**
     *
     * @return false se la votazione non é assoluta, true altrimenti
     */
    public boolean isAssoluta() {
        return assoluta;
    }

    /**
     * @return 0 se é ordinale, 1 se categorico, 2 se categorico con preferenza
     */
    public int whichType() {
        if (ordinale)
            return 0;
        else if (preferenza)
            return 2;
        return 1;
    }

    public String toString() {
        StringBuilder s = new StringBuilder("Votazione per " + descrizione + "\n");
        switch (whichType()) {
            case 0 -> s.append("Votazione ordinale -/- ");
            case 1 -> s.append("Votazione categorica non preferenziale -/- ");
            case 2 -> s.append("Votazione categorica preferenziale -/- ");
        }
        s.append(getScadenza());
        return s.toString();
    }

}