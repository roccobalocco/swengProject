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
        this.quorum = q;
    }

    /**
     * 
     */
    public boolean quorum;

    /**
     * @return
     */
    public boolean hasQuorum() { return quorum; }

}