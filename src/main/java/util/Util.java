package util;

import data.*;
import models.Admin;
import models.Elettore;
import models.Gruppo;
import models.Persona;

import java.awt.*;
import java.io.File;
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

    /**
     * Bonifica le stringhe contro SQLInjection
     * @param s != null
     * @return stringa senza ' ne "
     */
    public static String bonify(String s) {
        return s.replaceAll("['\"]", "`");
    }

    /**
     * Bonifica le stringhe da SQLInjection e per la scrittura in PDF
     * @param s != null
     * @return stringa senza accenti e senza ? ! - ' ` ", inoltre sostituisce  gli spazi e la punteggiatura con _
     */
    public static String bonify2(String s){
        return s.replaceAll("[-`'\"?! ]", "_").replaceAll("[óò]", "o")
                .replaceAll("[éè]", "e").replaceAll("[úù]", "u")
                .replaceAll("[áà]", "a").replaceAll("[íì]", "i");
    }

    /**
     * Controlla la validitá del codice fiscale/codice admin
     * @param cf != null
     * @return true se cf ha lunghezza 16
     */
    public static boolean check(String cf) {
        if (cf == null)
            return false;
        if (cf.length() != 16) {
            System.out.println("A quanto pare '" + cf + "' non ha 16 caratteri ma ne ha " + cf.length());
            return false;
        }
        return true;
    }

    /**
     *
     * @param date
     * @return date convertito il LocalDate
     */
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

    /**
     *
     * @return stringa di benvenuto in base all'orario attuale
     */
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

    /**
     *
     * @param lg != null
     * @return una mappa con i voti corrispondenti ad una votazione ordinale
     */
    public static Map<Gruppo, Integer> getVotiOrdinale(List<Gruppo> lg) {
        int n = lg.size();
        Map<Gruppo, Integer> mg = new HashMap<>();
        for (int i = 0; i < lg.size(); i++)
            mg.put(lg.get(i), n - i);
        return mg;
    }

    /**
     *
     * @param lp != null
     * @return una mappa con i voti corrispondenti ad una votazione categorica preferenziale, per i candidati dei partiti
     */
    public static Map<Persona, Integer> getVotiPreferenziale(List<Persona> lp) {
        int n = lp.size();
        Map<Persona, Integer> mp = new HashMap<>();
        for (int i = 0; i < lp.size(); i++)
            mp.put(lp.get(i), n - i);
        return mp;
    }



    public static void showResult(String path) {
        try {
            File pdfFile = new File(path);
            System.out.print("File da aprire-->" + path);
            if (pdfFile.exists())
                if (Desktop.isDesktopSupported())
                    Desktop.getDesktop().open(pdfFile);
                else
                    System.out.println("Awt Desktop is not supported!");
            else
                System.out.println("File is not exists!");
            System.out.println("Done");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}