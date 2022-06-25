package data;

import models.Admin;

/**
 * 
 */
public interface AdminDAO {

    /**
     * @param cf
     * @param psw
     */
    Admin getAdmin(String cf, String psw);

    boolean isAdmin(String cf, String psw);
}