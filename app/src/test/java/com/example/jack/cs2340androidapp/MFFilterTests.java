package com.example.jack.cs2340androidapp;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;


//Tests made by Jose A. Ballestero

/**
 * Runs tests on the MFFilter class.
 */
public class MFFilterTests {
    private Shelter test;

    private static final int TIMEOUT = 200;

    /**
     * Sets up test
     */
    @Before
    public void setUp() {
        test = new Shelter("Definitely a test", "5", "7864511611", "nada", "none :D");
    }

    /**
     * Runs test
     */
    @Test(timeout = TIMEOUT)
    public void test() {
        //Creates a blank filter
        Application.setFilter(new String[]{"", "", ""});
        assertTrue(Application.MFFilter(test));
        test.setRestrictions("n/a");
        assertTrue(Application.MFFilter(test));


    }

    /**
     * Another test
     */
    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void testNullRestrictions() {

        //Test for null restriction

        test.setRestrictions(null);
        Application.MFFilter(test);


    }

    /**
     * Third test fuck JavaDocs
     */
    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void testNull() {

        //Test for null restriction
        test = null;
        Application.MFFilter(test);


    }

}
