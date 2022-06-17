package models;

import java.util.*;

/**
 * @author Piemme
 */
public class Classica extends Votazione {

    /**
     * Default constructor
     */
    public Classica(String desc, Date scad, boolean o) {
        super(desc, scad);
        ordinale = o;
    }

    /**
     * 
     */
    public Boolean ordinale;

    /**
     * @return
     */
    public Boolean isOrdinale() {
        // TODO implement here
        return null;
    }

}