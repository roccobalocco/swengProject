package data;

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
    public void getElettore(String user, String psw);

}