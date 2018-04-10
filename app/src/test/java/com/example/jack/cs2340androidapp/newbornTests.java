package com.example.jack.cs2340androidapp;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Created by calla on 4/9/18.
 */

public class newbornTests {

    private Shelter shelter;


    public static final int TIMEOUT = 200;

    public static final boolean CHECK_EXCEPTION_MESSAGES = false;

    @Before
    public void setUp() {
        shelter = new Shelter("Aardvark", "500", "num", "n/a", "newborns", 10, 1);
    }


    @Test(timeout = TIMEOUT)
    public void testNormal() {
        //test normal cases
        assertTrue(Application.newbornFilters(shelter));
        shelter.setRestrictions("none");
        assertFalse(Application.newbornFilters(shelter));

        //test capital letters
        shelter.setRestrictions("NewBorns");
        assertTrue(Application.newbornFilters(shelter));
        shelter.setRestrictions("NewBorns");
        assertTrue(Application.newbornFilters(shelter));
    }
}
