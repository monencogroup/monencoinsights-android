package com.monenco.insights;

public class Settings {

    public static String CLIENT_ID;
    public static String CLIENT_SECRET;
    public static String DOMAIN;
    public static String API_URL;
    public static String PLATFORM;
    public static String VERSION;
    public static int DEFAULT_ZOOM;
    public static boolean CAFE_BAZAR = true;
    public static String base64EncodedPublicKey = "MIHNMA0GCSqGSIb3DQEBAQUAA4G7ADCBtwKBrwDIWdRC3r+QRvP2WqQMlgKxDex7yWudre5+i0CTEiMYSSzM1vCmw9vhQQpGaIwpWUrV9zUOnrAh5AzFU5DY75+Ljlw0zJkA2/OVyz74G47O8/4i2wl9Uv5Q6OSGIaTYSU/Klivk6ZSyckJRsMOSSLpm6Fk3Ky8i1JvZGCTURbusbXYpSyIcY2TAJ38L0QVAgqEYtjfSjL0P/aV9cYKpi543c3L0uqpIkcd9IQr7a48CAwEAAQ==";


    static {
        initialize();
    }

    private static final int LOCAL = 1;
    private static final int PRODUCT = 2;

    public static void initialize() {
        int type = LOCAL;
        PLATFORM = "Android";
        VERSION = "1";
        API_URL = "api/v1/";
        DEFAULT_ZOOM = 15;
        switch (type) {
            case LOCAL:
                CLIENT_ID = "aVL0WN8hPkgHPXef1MCnKwzzz5IJpqWYsBAKvFYC";
                CLIENT_SECRET = "PidFXzCLy4fiULNlhAjZerwBZU2l4iApyYF4NFjMmCbdXaqdBtm6qILNLxSOm5rwROEnajtVe69IUEOLofoxchL6FHheFVa3dPyWGEGnJd1bIFYPfDiSFTVqsqQqDGfJ";
                DOMAIN = "http://192.168.43.116:8000/";
                break;
            case PRODUCT:
                CLIENT_ID = "aVL0WN8hPkgHPXef1MCnKwzzz5IJpqWYsBAKvFYC";
                CLIENT_SECRET = "PidFXzCLy4fiULNlhAjZerwBZU2l4iApyYF4NFjMmCbdXaqdBtm6qILNLxSOm5rwROEnajtVe69IUEOLofoxchL6FHheFVa3dPyWGEGnJd1bIFYPfDiSFTVqsqQqDGfJ";
                DOMAIN = "http://5.253.26.156/";
                break;
        }
    }
}
