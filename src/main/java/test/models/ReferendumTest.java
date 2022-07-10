package test.models; 

import models.Referendum;
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
    public void before() throws Exception {
        //Referendum con quorum [ERRATO per DESCRIZIONE] [Solleva eccezione]
            //r1 = new Referendum("", LocalDate.of(2000, 11, 04), true, 1);
        //Referendum con quorum [CORRETTO]
            r1 = new Referendum("Eutanasia Legale", LocalDate.of(2000, 11, 04), true, 1);

        //Referendum con quorum [ERRATO per SCADENZA] [Solleva eccezione]
        //r2 = new Referendum("Si al Nucleare", null, false, 2);
        //Referendum con quorum [CORRETTO]
        r2 = new Referendum("Si al Nucleare", LocalDate.of(2025, 11, 04), false, 2);

        //Referendum con quorum [CORRETTO]
        r3 = new Referendum("Cannabis Legale", LocalDate.of(2023, 9, 04), true, 3);
    }

    @After
    public void after() throws Exception {
    }

    /**
    *
    * Method: hasQuorum()
    *
    */
    @Test
    public void testHasQuorum() throws Exception {
    //TODO: Test goes here...
        System.out.println("Risultato corretto r1 --> true");
        System.out.println("Risultato test r1 --> " + r1.hasQuorum());
        System.out.println("Risultato corretto r2 --> false");
        System.out.println("Risultato test r2 --> " + r2.hasQuorum());
        System.out.println("Risultato corretto r3 --> true");
        System.out.println("Risultato test r3 --> " + r3.hasQuorum());
    }


} 
