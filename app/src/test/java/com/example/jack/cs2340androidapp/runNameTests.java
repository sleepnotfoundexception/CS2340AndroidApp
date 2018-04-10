package com.example.jack.cs2340androidapp;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;


/**
 * Run name testing
 * Created by sidac on 4/10/2018.
 */

public class runNameTests {
    private Shelter shelter;
    private static final int TIMEOUT = 200;

    /**
     * Setup test
     */
    @Before
    public void setUp() {
        shelter = new Shelter("random shelter", "500", "num", "n/a", "none");
    }

    /**
     * Run test
     */
    @Test(timeout = TIMEOUT)
    public void testNormal() {
        //Reset filter to blank
        Application.setFilter(new String[]{"", "", ""});
        
        //test normal cases
        assertTrue(Application.runNameFilters(shelter));

        //test capital letters
        Application.setFilter(new String[]{"RANdom ShELter", "", ""});
        assertTrue(Application.runNameFilters(shelter));

        //test trim
        Application.setFilter(new String[]{"   random shelter   ", "", ""});
        assertTrue(Application.runNameFilters(shelter));

        //test for loop
        Application.setFilter(new String[]{"random shelter extra", "", ""});
        assertFalse(Application.runNameFilters(shelter));

        Application.setFilter(new String[]{"random shelters", "", ""});
        assertFalse(Application.runNameFilters(shelter));

        Application.setFilter(new String[]{"random shelter", "", ""});
        assertTrue(Application.runNameFilters(shelter));
    }

}
