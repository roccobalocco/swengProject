package data;

import models.Admin;

import java.io.IOException;

/**
 * 
 */
public interface AdminDAO {

    /**
     */
    Admin getAdmin(String cf, String psw) throws IOException;

    boolean isAdmin(String cf, String psw) throws IOException;
}