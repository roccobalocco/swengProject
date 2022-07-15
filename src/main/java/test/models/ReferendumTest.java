package test.models; 

import models.Classica;
import models.Referendum;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;


import java.time.LocalDate;

/** 
* Referendum Tester. 
* 
* @author <Authors name> 
* @since <pre>lug 10, 2022</pre> 
* @version 1.0 
*/ 
public class ReferendumTest {

    private Referendum r1, r2, r3;

    @Before
    public void before() {
        //Referendum con quorum [ERRATO per DESCRIZIONE] [Solleva eccezione]
            //r1 = new Referendum("", LocalDate.of(2000, 11, 04), true, 1);
        //Referendum con quorum [CORRETTO]
            r1 = new Referendum("Eutanasia Legale", LocalDate.of(2000, 11, 4), true, 1);

        //Referendum con quorum [ERRATO per SCADENZA] [Solleva eccezione]
        //r2 = new Referendum("Si al Nucleare", null, false, 2);
        //Referendum con quorum [CORRETTO]
        r2 = new Referendum("Si al Nucleare", LocalDate.of(2025, 11, 4), false, 2);

        //Referendum con quorum [CORRETTO]
        r3 = new Referendum("Cannabis Legale", LocalDate.of(2023, 9, 4), true, 3);

    }

    @After
    public void after() {
        System.out.println("\nTest Referendum.java eseguito con successo!");
    }

    /**
    *
    * Method: hasQuorum()
    *
    */
    @Test
    public void testHasQuorum() {
    //TODO: Test goes here...
        System.out.println("Risultato corretto r1 --> true");
        Assert.assertTrue("r1 ha il quorum", r1.hasQuorum());

        System.out.println("Risultato corretto r2 --> false");
        Assert.assertFalse("r2 non ha il quorum", r2.hasQuorum());

        System.out.println("Risultato corretto r3 --> true");
        Assert.assertTrue("r3 ha il quorum", r3.hasQuorum());

    }


} 
