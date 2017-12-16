package com.elf.net;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author bnevins
 */
public class NetUtilsTest {
    @Test
    public void foo() throws UnknownHostException {
        String s = InetAddress.getLocalHost().getHostName();
        System.out.println("*** GetLocalHost().getHostName() ***" +  NetUtils_old.getInfo(s));
    }


    @Test
    public void goo() throws UnknownHostException {
        System.out.println("*** GetLocalHost() ***" + NetUtils_old.getInfo(InetAddress.getLocalHost()));
    }


    @Test
    public void ioo() throws UnknownHostException {



        System.out.println("*** GetLocalHost() ***" + NetUtils_old.getInfo(InetAddress.getLocalHost()));
    }



    @Test
    public void testGetInfo_0args() {
        System.out.println("********   getInfo() ***********");
        String result = NetUtils_old.getInfo();
        System.out.println(result);
    }

    /**
     * Test of getInfo method, of class NetUtils_old.
     */
    @Test
    public void testGetInfo_StringArr() {
        System.out.println("********   getInfo(String[]) ***********");
        String result = NetUtils_old.getInfo(hostnames);
        System.out.println(result);
    }
    private static final String[] hostnames = new String[]{
        "LOCALHOST",
        "localhost",
        "127.0.0.1",
        "0.0.0.0",
        };
}
