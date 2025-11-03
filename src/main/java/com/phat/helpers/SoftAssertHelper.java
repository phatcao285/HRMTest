package com.phat.helpers;

import org.testng.asserts.SoftAssert;

public class SoftAssertHelper {
    private static ThreadLocal<SoftAssert> softAssert = ThreadLocal.withInitial(SoftAssert::new);

    public static SoftAssert getSoftAssert() {
        return softAssert.get();
    }
    public static void resetSoftAssert() {
        softAssert.set(new SoftAssert());
    }
}