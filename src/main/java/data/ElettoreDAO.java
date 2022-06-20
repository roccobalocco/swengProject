package data;

import models.Elettore;
import models.Votazione;

import java.util.*;

/**
 * 
 */
public interface ElettoreDAO {

    /**
     * @param cf
     * @param psw
     */
    public Elettore getElettore(String cf, String psw);

    /**
     *
     * @param cf
     * @param psw
     * @return
     */
    public boolean haVotato(String cf, String psw, Votazione v);

}