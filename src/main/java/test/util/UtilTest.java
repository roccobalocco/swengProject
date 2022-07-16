package test.util; 

import models.Gruppo;
import models.Persona;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;
import util.Util;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/** 
* Util Tester. 
* 
* @author <Authors name> 
* @since <pre>lug 16, 2022</pre> 
* @version 1.0 
*/ 
public class UtilTest { 
    private List<Gruppo> lg;
    private final Map<Gruppo, Integer> mg = new HashMap<>();
    private List<Persona> lp;
    private final Map<Persona, Integer> mp = new HashMap<>();

    @Before
    public void before() throws Exception {
        lg = new LinkedList<>();
        lg.add(new Gruppo(1, "PD"));
        lg.add(new Gruppo(2, "PSI"));
        lg.add(new Gruppo(3, "PDL"));
        lg.add(new Gruppo(4, "FDI"));
        mg.put(lg.get(0), 4); mg.put(lg.get(1), 3); mg.put(lg.get(2), 2); mg.put(lg.get(3), 1);

        lp = new LinkedList<>();
        lp.add(new Persona(3, "Mirco", 4));
        lp.add(new Persona(4, "Piero", 4));
        lp.add(new Persona(5, "Gigi", 4));
        lp.add(new Persona(6, "Alessio", 4));
        mp.put(lp.get(0), 4); mp.put(lp.get(1), 3); mp.put(lp.get(2), 2); mp.put(lp.get(3), 1);
    }

    @After
    public void after() throws Exception { System.out.println("\nTest Util.java eseguito con successo!"); }

    /**
    *
    * Method: bonify(String s)
    *
    */
    @Test
    public void testBonify() throws Exception {
        Assert.assertEquals("Stringa ['DELETE FROM votazione;]", "`DELETE FROM votazione;",
                Util.bonify("'DELETE FROM votazione;"));
    }

    /**
    *
    * Method: bonify2(String s)
    *
    */
    @Test
    public void testBonify2() throws Exception {
        Assert.assertEquals("Stringa ['`\"?DELETE FROM vótázíóné;!]",
                "____DELETE_FROM_votazione;_",
                Util.bonify2("'`\"?DELETE FROM vótázíóné;!"));
    }

    /**
    *
    * Method: check(String cf)
    *
    */
    @Test
    public void testCheck() throws Exception {
        Assert.assertFalse(Util.check(null));
        Assert.assertFalse(Util.check("MSLPTR00S07C623TT"));
        Assert.assertTrue(Util.check("MSLPTR00S07C623T"));
    }

    /**
    *
    * Method: getVotiOrdinale(List<Gruppo> lg)
    *
    */
    @Test
    public void testGetVotiOrdinale() throws Exception {
        Util.getVotiOrdinale(lg).forEach((g, i) -> {
            Assert.assertEquals(i, mg.get(g));
        });
    }

    /**
    *
    * Method: getVotiPreferenziale(List<Persona> lp)
    *
    */
    @Test
    public void testGetVotiPreferenziale() throws Exception {
        Util.getVotiPreferenziale(lp).forEach((p, i) -> {
            Assert.assertEquals(i, mp.get(p));
        });
    }

} 
