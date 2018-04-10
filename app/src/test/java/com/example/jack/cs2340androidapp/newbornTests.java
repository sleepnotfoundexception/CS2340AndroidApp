package com.example.jack.cs2340androidapp;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Runs tests for the newbornFilters method.
 * Created by calla on 4/9/18.
 */

public class newbornTests {

    private Shelter shelter;

    /**
     * JUnit specific constant
     */
    private static final int TIMEOUT = 200;

    /**
     * Sets up test
     */
    @Before
    public void setUp() {
        shelter = new Shelter("new shelter", "500", "num",
                "n/a", "n/a");
    }

    /**
     * Runs test
     */
    @Test(timeout = TIMEOUT)
    public void testNormal() {
        //Reset filter to blank
        Application.setFilter(new String[]{"", "", ""});

        //test base cases
        assertTrue(Application.newbornFilters(shelter));
        shelter.setRestrictions("families with newborns");
        assertTrue(Application.newbornFilters(shelter));

        ///testing variations of families with newborns
        Application.setFilter(new String[]{"", "", "Families with Newborns"});
        shelter.setRestrictions("families with newborns");
        assertTrue(Application.newbornFilters(shelter));
        shelter.setRestrictions("families w/ newborns");
        assertTrue(Application.newbornFilters(shelter));
        shelter.setRestrictions("families, newborns");
        assertTrue(Application.newbornFilters(shelter));
        shelter.setRestrictions("families newborns");
        assertTrue(Application.newbornFilters(shelter));
        shelter.setRestrictions("newborns families");
        assertTrue(Application.newbornFilters(shelter));
        shelter.setRestrictions("none");
        assertFalse(Application.newbornFilters(shelter));
        shelter.setRestrictions("newborn babies without a family");
        assertFalse(Application.newbornFilters(shelter));
        secondTestBracket();

    }

    //This only exists because the style checker was complaining the first method was too long ;=;
    private void secondTestBracket() {
        //test capital letters
        shelter.setRestrictions("Families With Newborns");
        assertTrue(Application.newbornFilters(shelter));
        shelter.setRestrictions("FamiLieS W/ NewbOrNs");
        assertTrue(Application.newbornFilters(shelter));

        //test trim
        shelter.setRestrictions("    families with newborns   ");
        assertTrue(Application.newbornFilters(shelter));
        shelter.setRestrictions("    families newborns   ");
        assertTrue(Application.newbornFilters(shelter));

        //not all conditions met
        shelter.setRestrictions("families only");
        assertFalse(Application.newbornFilters(shelter));
        shelter.setRestrictions("newborns only");
        assertFalse(Application.newbornFilters(shelter));
    }

}
