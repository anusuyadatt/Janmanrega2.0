package nic.hp.ccmgnrega.common;

public class Helper {
    private static Helper helper;

    static {
        System.loadLibrary("ccmgnrega");
    }




    public static Helper getInstance() {
        if (helper == null) {
            helper = new Helper();
        }

        return helper;
    }

    public static native String getBaseURL();


}
