package wingfly.com.encro.databases;

import android.content.Context;

/**
 * Created by Tiko on 3/8/2018.
 */

public class DbHandler
{
    static Database database;

    private DbHandler()
    {
    }

    public static Database newInstance(Context context)
    {
        return database == null ? new Database(context) : database;
    }
}
