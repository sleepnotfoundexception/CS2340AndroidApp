package com.example.jack.cs2340androidapp;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Runs tests for the newbornFilters method.
 * Created by calla on 4/9/18.
 */

public class newbornTests {

    private Shelter shelter;

    /**
     * JUnit specific constant
     */
    public static final int TIMEOUT = 200;
    /**
     * JUnit specific constant
     */
    public static final boolean CHECK_EXCEPTION_MESSAGES = false;

    @Before
    /**
     * Prepare to execute JUnit test by creating shelter to pass in
     */
    public void setUp() {
        shelter = new Shelter("Aardvark", "500", "num", "n/a", "newborns", 10, 1);
    }


    @Test(timeout = TIMEOUT)
    /**
     * Execute test of newbornFilters function
     */
    public void testNormal() {
        //Reset filter to blank
        Application.setFilter(new String[]{"", "", ""});
        //test normal cases
        assertTrue(Application.newbornFilters(shelter));
        shelter.setRestrictions("none");
        assertTrue(Application.newbornFilters(shelter));

        //test capital letters
        Application.setFilter(new String[]{"", "", "Families w/ newborns"});
        shelter.setRestrictions("families w/ newborns");
        assertTrue(Application.newbornFilters(shelter));
        shelter.setRestrictions("NewBorns");
        assertTrue(Application.newbornFilters(shelter));
    }
}
