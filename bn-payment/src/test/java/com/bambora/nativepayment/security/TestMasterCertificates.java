package com.bambora.nativepayment.security;

import org.junit.Assert;
import org.junit.Test;


public class TestMasterCertificates {

    @Test
    public void testGetMasterCertificates(){
        Assert.assertEquals(2,MasterCertificates.getMasterCertificates().size());
    }


}
