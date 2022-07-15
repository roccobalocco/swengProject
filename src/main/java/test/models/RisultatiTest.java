package test.models; 

import models.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/** 
* Risultati Tester. 
* 
* @author <Authors name> 
* @since <pre>lug 11, 2022</pre> 
* @version 1.0 
*/ 
public class RisultatiTest { 

    private Risultati rc1, rc2, rr3, rr4;
    private Map<Gruppo, Integer> mg;
    private Map<Persona, Integer> mp;

    @Before
    public void before() {
        Classica c1 = new Classica("Amministrative Milano 20xx", LocalDate.of(2022, 6, 20), false, true, 1, true); //Cat pref
        Classica c2 = new Classica("Amministrative Milano 20xx", LocalDate.of(2022, 6, 20), true, true, 1, true); //Ordinale

        mg = new HashMap<>();
        mg.put(new Gruppo(1, "PD"), 250000); mg.put(new Gruppo(2, "PCI"), -2500000);

        mp = new HashMap<>();
        mp.put(new Persona(1, "Enrico Letta", 1), 95000); mp.put(new Persona(2, "Valentina Cuppi", 1), -65000);
        mp.put(new Persona(3, "Mauro Alboresi", 2), 125000); mp.put(new Persona(4, "Maria Carla Baroni", 2), 650000);

        Referendum r1 = new Referendum("Cannabis legale 20xx", LocalDate.of(2021, 5, 20), false, 1);
        Referendum r2 = new Referendum("Eutanasia legale 20xx", LocalDate.of(2020, 5, 20), true, 1);

        rc1 = new Risultati(c1, mg, mp);
        rc2 = new Risultati(c2, mg);

        rr3 = new Risultati(r1);
        rr4 = new Risultati(r2);
    }

    @After
    public void after() {
        System.out.println("\nTest Risultati.java eseguito con successo!");
    }

    /**
    *
    * Method: setRef(int si, int no, int bianca)
    *
    */
    @Test
    public void testSetRef() {
    //TODO: Test goes here...
        Assert.assertTrue("Inserendo voti", rr3.setRef(153834, 9012, 390));
        Assert.assertFalse("Inserendo voti (Errati)", rr3.setRef(-153834, 9012, 390));

        Assert.assertTrue("Inserendo voti",rr4.setRef(1053834, 90102, 3902));
        Assert.assertFalse("Inserendo voti (Errati)",rr4.setRef(1053834, -90102, -3902));

        Assert.assertFalse("Inserendo voti in Classica (Errato)", rc1.setRef(1,1,1));
    }

    /**
    *
    * Method: setVoti(Gruppo g, Integer voti)
    *
    */
    @Test
    public void testSetVotiForGVoti() {
    //TODO: Test goes here...
        mg.forEach((g, i) -> {
            if(i < 0)
                Assert.assertFalse("Inserendo voti per gruppi", rc1.setVoti(g, i));
            else
                Assert.assertTrue("Inserendo voti per gruppi", rc1.setVoti(g, i));
        });
        mg.forEach((g, i) -> {
            if(i < 0)
                Assert.assertFalse("Inserendo voti per gruppi", rc2.setVoti(g, i));
            else
                Assert.assertTrue("Inserendo voti per gruppi", rc2.setVoti(g, i));
        });

        Assert.assertFalse("Inserendo voti in referendum (Errato)", rr3.setVoti(new Gruppo(1, "PD"), 100));

    }

    /**
    *
    * Method: setVoti(Persona p, Integer voti)
    *
    */
    @Test
    public void testSetVotiForPVoti() {
    //TODO: Test goes here...
        mp.forEach((p, i) -> {
            if(i > 0)
                Assert.assertTrue("Inserendo voti per persone", rc1.setVoti(p, i));
            else
                Assert.assertFalse("Inserendo voti per persone", rc1.setVoti(p, i));
        });

        Assert.assertFalse("Inserendo voti in referendum (Errato)",
                rr3.setVoti(new Persona(1, "PD", 1), 100));

    }

} 
