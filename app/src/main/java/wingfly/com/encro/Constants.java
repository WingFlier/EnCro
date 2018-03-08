package wingfly.com.encro;

import java.util.Random;

public class Constants
{
    private static Random random;

    static
    {
        System.loadLibrary("native-lib");
    }

    public static final String KEY = "euGUAIEgufGyylZy";
    public static final String VECTOR = "txSYZUsrvkxvlUZw";
    public static final String NDK_KEY = stringFromJNI();

    public static native String stringFromJNI();

    public static int random(int min, int max)
    {
        if (random == null)
            random = new Random();
        return random.nextInt(max - min) + min;
    }

    public static String randomStr()
    {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 16; i++)
        {
            int[] arr = new int[2];
            arr[0] = random(65, 91);
            arr[1] = random(97, 123);
            int num = arr[random(0, 2)];
            char ch = (char) num;
            builder.append(ch);
        }
        return builder.toString();
    }
}