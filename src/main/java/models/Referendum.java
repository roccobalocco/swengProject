package models;

import java.util.*;

/**
 * @author Piemme
 */
public class Referendum extends Votazione {

    /**
     * Default constructor
     */
    public Referendum(String desc, Date scad, boolean q) {
        super(desc, scad);
        quorum = q;
    }

    /**
     * 
     */
    public Boolean quorum;

    /**
     * @return
     */
    public Boolean hasQuorum() {
        // TODO implement here
        return null;
    }

}