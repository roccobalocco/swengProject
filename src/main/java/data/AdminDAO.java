package data;

import models.Admin;

/**
 * 
 */
public interface AdminDAO {

    /**
     */
    Admin getAdmin(String cf, String psw);

    boolean isAdmin(String cf, String psw);
}