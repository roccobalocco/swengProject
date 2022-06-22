package data;

import models.Admin;

import java.util.*;

/**
 * 
 */
public interface AdminDAO {

    /**
     * @param cf
     * @param psw
     */
    public Admin getAdmin(String cf, String psw);

    public boolean isAdmin(String cf, String psw);
}