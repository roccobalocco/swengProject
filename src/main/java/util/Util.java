package util;

import data.*;
import models.Admin;
import models.Elettore;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author Piemme
 */
public class Util {

    private static Util uniqueInstance;

    private Util(){}

    public static Util getInstance(){
        if(uniqueInstance == null)
            uniqueInstance = new Util();
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

    public static LocalDate toDateTime(Date date) {
        return date.toLocalDate();
    }

    public static void addElettoreObs(){
        Observer o  = Elettore.getInstance();
        CandidatoDAOImpl.getInstance().subscribe(o);
        ClassicaDAOImpl.getInstance().subscribe(o);
        ElettoreDAOImpl.getInstance().subscribe(o);
        ReferendumDAOImpl.getInstance().subscribe(o);
    }
    public static void addAdminObs(){
        Observer o  = Admin.getInstance();
        CandidatoDAOImpl.getInstance().subscribe(o);
        ClassicaDAOImpl.getInstance().subscribe(o);
        ElettoreDAOImpl.getInstance().subscribe(o);
        ReferendumDAOImpl.getInstance().subscribe(o);
    }

    public static String welcome(){
        int ora = LocalDateTime.now().getHour();
        if(ora > 19 && ora < 23)
            return "Buonasera";
        else if(ora >= 23 || ora <= 5)
            return "Salve";
        else if(ora < 13)
            return "Buongiorno";
        return "Buon pomeriggio";
    }

}