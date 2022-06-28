package models;

import java.time.LocalDate;
import java.util.*;

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

    private boolean ordinale = false;
    private boolean preferenza = false;
    private boolean assoluta = false;

    public boolean  isAssoluta(){ return assoluta; }

    /**
     * @return 0 se é ordinale, 1 se categorico, 2 se categorico con preferenza
     */
    public int whichType(){
        if (ordinale)
            return 0;
        else if (preferenza)
            return 2;
        return 1;
    }

    public String toString(){
        StringBuilder s = new StringBuilder("Votazione per " + descrizione + "\n");
        switch (whichType()) {
            case 0 -> s.append("Votazione ordinale\n");
            case 1 -> s.append("Votazione categorica non preferenziale\n");
            case 2 -> s.append("Votazione categorica preferenziale\n");
        }
        s.append(getScadenza());
        return s.toString();
    }

    @Override
    public Risultati getRisultati() {
        return null;
    }
}