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
    private static final int TIMEOUT = 200;

    /**
     * Sets up test
     */
    @Before
    public void setUp() {
        shelter = new Shelter("testShelter", "234", "8289894242", "Male", "Any");
        shelter2 = new Shelter("testShelter2", "-12", "fourteen", "arg", "4");
    }

    /**
     * Runs test
     */
    @Test(timeout = TIMEOUT)
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
