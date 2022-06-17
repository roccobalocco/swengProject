package data;

import models.Elettore;
import models.Votazione;

import java.util.*;

/**
 * 
 */
public interface ElettoreDAO {

    /**
     * @param user 
     * @param psw
     */
    public Elettore getElettore(String user, String psw);

    /**
     *
     * @param user
     * @param psw
     * @return
     */
    public boolean haVotato(String user, String psw, Votazione v);

}