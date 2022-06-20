package models;

import java.util.*;

/**
 * @author Piemme
 */
public class Classica extends Votazione {

    /**
     * Default constructor
     */
    public Classica(String desc, Date scad, boolean o, boolean p) {
        super(desc, scad);
        ordinale = o;
        preferenza = p;
    }

    private boolean ordinale = false;
    private boolean preferenza = false;

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
}