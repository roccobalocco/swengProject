package test.models; 

import models.Classica;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;

import java.time.LocalDate;

/** 
* Classica Tester. 
* 
* @author <Authors name> 
* @since <pre>lug 10, 2022</pre> 
* @version 1.0 
*/ 
public class ClassicaTest { 
    private Classica c1, c2, c3;
    @Before
    public void before() throws Exception {
        //Categorica non preferenziale e a maggioranza assoluta [CORRETTA]
        c1 = new Classica("Amministrative Milano", LocalDate.of(2023, 8, 8), false, false, 1, true);

        //Ordinale senza maggioranza assoluta [ERRATA per DATA] [Solleva eccezione...]
            //c2 = new Classica("Amministrative Prata", null, true, false, 2, false);
        //Ordinale senza maggioranza assoluta [CORRETTA]
            c2 = new Classica("Amministrative Prata", LocalDate.of(2022, 11, 7), true, true, 2, false);

        //Categorica preferenziale a maggioranza assoluta [ERRATA per DESCRIZIONE] [Solleva eccezione...]
            //c3 = new Classica("", LocalDate.of(2023, 8, 8), false, true, 3, true);
        //Categorica preferenziale a maggioranza assoluta [CORRETTA]
            c3 = new Classica("I like JUnit4", LocalDate.of(2023, 8, 8), false, true, 3, true);
    }

    @After
    public void after() throws Exception {
        System.out.println("\nTest Classica.java eseguito con successo!");
    }

    /**
    *
    * Method: isAssoluta()
    *
    */
    @Test
    public void testIsAssoluta() throws Exception {
    //TODO: Test goes here...
        System.out.println("Risultato corretto c1 --> true");
        System.out.println("Risultato test c1 --> " + c1.isAssoluta());
        System.out.println("Risultato corretto c2 --> false");
        System.out.println("Risultato test c2 --> " + c2.isAssoluta());
        System.out.println("Risultato corretto c3 --> true");
        System.out.println("Risultato test c3 --> " + c3.isAssoluta());
    }

    /**
    *
    * Method: whichType()
    *
    */
    @Test
    public void testWhichType() throws Exception {
    //TODO: Test goes here...
        System.out.println("Risultato corretto c1 --> 1");
        System.out.println("Risultato test c1 --> " + c1.whichType());
        System.out.println("Risultato corretto c2 --> 0");
        System.out.println("Risultato test c2 --> " + c2.whichType());
        System.out.println("Risultato corretto c3 --> 2");
        System.out.println("Risultato test c3 --> " + c3.whichType());
    }



} 
