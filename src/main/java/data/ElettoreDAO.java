package data;

import models.Elettore;

import java.util.*;

/**
 * 
 */
public interface ElettoreDAO {

    /**
     * @return
     */
    public List getAllElettori();

    /**
     * @param user 
     * @param psw
     */
    public Elettore getElettore(String user, String psw);

}