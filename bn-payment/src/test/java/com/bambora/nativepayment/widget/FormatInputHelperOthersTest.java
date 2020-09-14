package com.bambora.nativepayment.widget;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class FormatInputHelperOthersTest {

    @Test
    public void clearNonDigitsTest(){
        String input="88.66.99";
        Assert.assertEquals("886699",FormInputHelper.clearNonDigits(input));
    }

    @Test
    public void getDeletedCharsTest(){
        String input="abcdefg";
        Assert.assertEquals("cdef",FormInputHelper.getDeletedChars(input,2,4));
    }

    @Test
    public void formatNumberSequenceTest(){
        char SPACE = ' ';
        Assert.assertEquals("4242 4242 4242 4242 ",FormInputHelper.formatNumberSequence("4242424242424242", Arrays.asList(4, 4, 4, 4),SPACE).toString());
    }



}
