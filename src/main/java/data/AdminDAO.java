package data;

import models.Admin;

import java.util.*;

/**
 * 
 */
public interface AdminDAO {

    /**
     * @param user 
     * @param psw
     */
    public Admin getAdmin(String user, String psw);

}