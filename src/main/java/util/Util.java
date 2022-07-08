package util;

import data.*;
import models.Admin;
import models.Elettore;
import models.Gruppo;
import models.Persona;
import org.codehaus.plexus.interpolation.RegexBasedInterpolator;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Piemme
 */
public class Util {

    public static String bonify(String s) {
        return s.replaceAll("['\"]", "`");
    }

    public static boolean check(String cf) {
        if (cf == null)
            return false;
        if (cf.length() != 16) {
            System.out.println("A quanto pare '" + cf + "' non ha 16 caratteri ma ne ha " + cf.length());
            return false;
        }
        return true;
    }

    public static LocalDate toDateTime(Date date) {
        return date.toLocalDate();
    }

    public static void addElettoreObs() {
        Observer o = Elettore.getInstance();
        ElettoreDAOImpl.getInstance().subscribe(o);
        CandidatoDAOImpl.getInstance().subscribe(o);
        ClassicaDAOImpl.getInstance().subscribe(o);
        ElettoreDAOImpl.getInstance().subscribe(o);
        ReferendumDAOImpl.getInstance().subscribe(o);
    }

    public static void addAdminObs() {
        Observer o = Admin.getInstance();
        AdminDAOImpl.getInstance().subscribe(o);
        CandidatoDAOImpl.getInstance().subscribe(o);
        ClassicaDAOImpl.getInstance().subscribe(o);
        ElettoreDAOImpl.getInstance().subscribe(o);
        ReferendumDAOImpl.getInstance().subscribe(o);
    }

    public static String welcome() {
        int ora = LocalDateTime.now().getHour();
        if (ora > 19 && ora < 23)
            return "Buonasera";
        else if (ora >= 23 || ora <= 5)
            return "Salve";
        else if (ora < 13)
            return "Buongiorno";
        return "Buon pomeriggio";
    }

    public static Map<Gruppo, Integer> getVotiOrdinale(List<Gruppo> lg) {
        int n = lg.size();
        Map<Gruppo, Integer> mg = new HashMap<>();
        for (int i = 0; i < lg.size(); i++)
            mg.put(lg.get(i), n - i);
        return mg;
    }

    public static Map<Persona, Integer> getVotiPreferenziale(List<Persona> lp) {
        int n = lp.size();
        Map<Persona, Integer> mp = new HashMap<>();
        for (int i = 0; i < lp.size(); i++)
            mp.put(lp.get(i), n - i);
        return mp;
    }
}