package com.elf.args;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author bnevins
 */
public class ArgProcessorTest {
    public ArgProcessorTest() {
    }

    @Before
    public void setUp() {
        regRequiredWithDefault = new Arg("hasDefault", "r", "xyz", "desc here!");
        regRequiredWithNoDefault = new Arg("isReq", "q", true, "desc here!");
        regNotRequired = new Arg("notReq", null, false, "desc here!");
        boolDefaultFalse = new BoolArg("deffalse", null, false, "desc here!");
        boolDefaultTrue = new BoolArg("deftrue", null, true, "desc here!");
    }

    @After
    public void tearDown() {
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRequiredArgsNo() {
        Arg[] myArgs = new Arg[]{regNotRequired, regRequiredWithNoDefault, regRequiredWithDefault};
        assertNull(regRequiredWithNoDefault.value);
        new ArgProcessor(myArgs, null);  // KABOOM!
    }

    @Test
    public void testRequiredArgsYes() {
        Arg arg = regRequiredWithNoDefault;
        Arg[] myArgs = new Arg[]{arg};
        String[] cmdline = new String[]{"--" + arg.longName, "thang"};
        assertNull(arg.value);
        assertNull(arg.defaultValue);
        new ArgProcessor(myArgs, cmdline);
        assertEquals(arg.value, "thang");
    }

    @Test
    public void testRequiredArgsWithDefault() {
        Arg arg = regRequiredWithDefault;
        Arg[] myArgs = new Arg[]{arg};
        String[] cmdline = new String[0];
        assertNull(arg.value);
        assertNotNull(arg.defaultValue);
        new ArgProcessor(myArgs, cmdline);
        assertEquals(arg.value, arg.defaultValue);
    }

    @Test
    public void testBoolArgTrue() {
        Arg arg = boolDefaultTrue;
        Arg[] myArgs = new Arg[]{arg};
        String[] cmdline = new String[0];
        assertNull(arg.value);
        assertNotNull(arg.defaultValue);
        new ArgProcessor(myArgs, cmdline);
        assertEquals(arg.value, arg.defaultValue);
        assertEquals(arg.value, "true");
    }

    @Test
    public void testBoolArgFalse() {
        Arg arg = boolDefaultFalse;
        Arg[] myArgs = new Arg[]{arg};
        String[] cmdline = new String[0];
        assertNull(arg.value);
        assertNotNull(arg.defaultValue);
        new ArgProcessor(myArgs, cmdline);
        assertEquals(arg.value, arg.defaultValue);
        assertEquals(arg.value, "false");
    }

    public void testBoolArgTrueToggle() {
        Arg arg = boolDefaultTrue;
        Arg[] myArgs = new Arg[]{arg};
        String[] cmdline = new String[]{Arg.NOT + arg.longName};
        assertNull(arg.value);
        assertNotNull(arg.defaultValue);

        new ArgProcessor(myArgs, cmdline);

        assertEquals(arg.value, "false");
    }

    public void testBoolArgFalseToggle() {
        Arg arg = boolDefaultFalse;
        Arg[] myArgs = new Arg[]{arg};
        String[] cmdline = new String[]{Arg.NOT + arg.longName};
        assertNull(arg.value);
        assertNotNull(arg.defaultValue);

        new ArgProcessor(myArgs, cmdline);

        assertEquals(arg.value, "false");
    }
    private Arg regRequiredWithDefault;
    private Arg regRequiredWithNoDefault;
    private Arg regNotRequired;
    private Arg boolDefaultFalse;
    private Arg boolDefaultTrue;
    //new BoolArg("bool2",    "c",    true,   "xxxxxxx"),
}
