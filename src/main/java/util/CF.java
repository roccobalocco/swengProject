package util;

/**
 * @author Piemme
 */
public class CF {

    private static CF uniqueInstance;

    private CF(){}

    public static CF getInstance(){
        if(uniqueInstance == null)
            uniqueInstance = new CF();
        return uniqueInstance;
    }

    public static boolean check(String cf){
        if(cf == null)
            return false;
        if(cf.length() != 16)
            return false;
        //TODO recupera da vecchio progetto per controllo
        return true;
    }

}