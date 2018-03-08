package wingfly.com.encro;

public class Constants
{
    static
    {
        System.loadLibrary("native-lib");
    }

    public static final String KEY = "euAPWIEgufGyylZy";
    public static final String VECTOR = "txSYZUsrvkxvlUZw";
    public static final String NDK_KEY = stringFromJNI();

    public static native String stringFromJNI();
}