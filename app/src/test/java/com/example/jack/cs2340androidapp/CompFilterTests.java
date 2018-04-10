package com.example.jack.cs2340androidapp;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Runs tests for the runFilters method.
 * Created by Jack on 4/9/18.
 */

public class CompFilterTests {

    private Shelter shelter;
    private Shelter shelter2;

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
        shelter = new Shelter("testShelter", "234", "8289894242", "Male", "Any", 10, 1);
        shelter2 = new Shelter("testShelter2", "-12", "fourteen", "arg", "4", -5, 3);
    }


    @Test(timeout = TIMEOUT)
    /**
     * Execute test of newbornFilters function
     */
    public void testNormal() {
        //Test defaults
        Application.setFilter(new String[]{"", "", ""});
        assertTrue(Application.runFilters(shelter));
        assertTrue(Application.runFilters(shelter2));

        //Test some specific cases to ensure all filters are operating
        //Filters: name, gender, restrictions
        //Test name filter
        Application.setFilter(new String[]{"test", "", ""});
        assertTrue(Application.runFilters(shelter));
        //Test restrictions filter
        Application.setFilter(new String[]{"test2", "", "Seventeen Fourteen"});
        assertFalse(Application.runFilters(shelter));
        Application.setFilter(new String[]{"", "", "Male"});
        assertTrue(Application.runFilters(shelter));
        //Test gender filter
        Application.setFilter(new String[]{"testShelter2", "Male", ""});
        assertTrue(Application.runFilters(shelter2));
        //Run edge case tests
        Application.setFilter(new String[]{"test2", "", ""});
        assertFalse(Application.runFilters(shelter2));
        Application.setFilter(new String[]{"testShelter2", "", "arg"});
        assertTrue(Application.runFilters(shelter2));
    }
}
