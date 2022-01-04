package com.weavesocks.api.core;

import org.testng.Assert;

public class Assertions {
    public Assertions() {
    }

    public static void assertEquals(String actual, String expected, String message, Boolean ignoreCase) {
        if (expected == null) {
            expected = "";
        } else if (ignoreCase) {
            expected = expected.toLowerCase();
        }

        if (actual == null) {
            actual = "";
        } else if (ignoreCase) {
            actual = actual.toLowerCase();
        }

        String status = "PASSED";
        String actualMessage = "";

        try {
            Assert.assertEquals(actual.trim(), expected.trim(), message);
            actualMessage = actualMessage + "Actual: " + actual + " == Expected: " + expected;
        } catch (AssertionError var10) {
            status = "FAILED";
            actualMessage = actualMessage + "Actual: " + actual + " != Expected: " + expected;
            throw new RuntimeException(var10);
        } finally {
            log(message, status, actualMessage);
        }

    }

    public static void assertEquals(String actual, String expected, String message) {
        assertEquals(actual, expected, message, false);
    }

    public static void assertNotEquals(String one, String two, String message) {
        if (one == null) {
            one = "";
        }

        if (two == null) {
            two = "";
        }

        String status = "PASSED";
        String actual = "";

        try {
            Assert.assertNotEquals(one.trim(), two.trim(), message);
            actual = actual + "First: " + one + " != Second: " + two;
        } catch (AssertionError var9) {
            status = "FAILED";
            actual = actual + "First: " + one + " == Second: " + two;
            throw new RuntimeException(var9);
        } finally {
            log(message, status, actual);
        }

    }

    public static void assertNotNull(Object value, String message) {
        String status = "PASSED";

        try {
            Assert.assertNotNull(value, message);
        } catch (AssertionError var7) {
            status = "FAILED";
            throw new RuntimeException(var7);
        } finally {
            log(message, status);
        }

    }

    public static void assertNull(Object value, String message) {
        String status = "PASSED";

        try {
            Assert.assertNull(value, message);
        } catch (AssertionError var7) {
            status = "FAILED";
            throw new RuntimeException(var7);
        } finally {
            log(message, status);
        }

    }

    public static void assertTrue(Boolean value, String message) {
        String status = "PASSED";

        try {
            Assert.assertTrue(value, message);
        } catch (AssertionError var7) {
            status = "FAILED";
            throw new RuntimeException(var7);
        } finally {
            log(message, status);
        }

    }

    public static void assertFalse(Boolean value, String message) {
        String status = "PASSED";

        try {
            Assert.assertFalse(value, message);
        } catch (AssertionError var7) {
            status = "FAILED";
            throw new RuntimeException(var7);
        } finally {
            log(message, status);
        }

    }

    public static void assertContains(String container, String textToMatch, String message) {
        assertContains(container, textToMatch, message, false);
    }

    public static void assertContains(String container, String textToMatch, String message, Boolean ignoreCase) {
        if (ignoreCase) {
            container = container.toLowerCase();
            textToMatch = textToMatch.toLowerCase();
        }

        String status = "PASSED";
        String actual = "";

        try {
            Assert.assertTrue(container.contains(textToMatch), message);
            actual = "actual text: " + container + " contains text to match: " + textToMatch;
        } catch (AssertionError var10) {
            status = "FAILED";
            actual = "actual text: " + container + " does not contain text to match: " + textToMatch;
            throw new RuntimeException(var10);
        } finally {
            log(message, status, actual);
        }

    }

    private static void log(String message, String status, String actual) {
        String log = "Validation for " + message + " completed with status of " + status;
        if (actual != null && !actual.isEmpty()) {
            (new StringBuilder()).append(log).append(" and actual results: ").append(actual).toString();
        }

    }

    private static void log(String message, String status) {
        log(message, status, (String)null);
    }
}
