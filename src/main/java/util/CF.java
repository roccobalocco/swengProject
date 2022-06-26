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
        if(cf.length() != 16) {
            System.out.println("A quanto pare '" + cf + "' non ha 16 caratteri ma ne ha " + cf.length());
            return false;
        }
        //TODO recupera da vecchio progetto per controllo
        return true;
    }

}