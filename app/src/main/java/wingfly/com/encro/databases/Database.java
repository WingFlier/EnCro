package wingfly.com.encro.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;

import wingfly.com.encro.Constants;
import wingfly.com.encro.POJO_s.Friend;
import wingfly.com.encro.encryption.Encryptor;

public class Database extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "friendList";

    public static final String TABLE_FRIENDS = "friends";
    public static final String NAME = "name", KEY = "key", ID = "id";

    public static final String CREATE_TABLE_FRIENDS =
            "CREATE TABLE " + TABLE_FRIENDS + " ("
                    + ID + " INTEGER PRIMARY KEY, " + NAME + " TEXT, " + KEY + " TEXT)";

    public Database(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL(CREATE_TABLE_FRIENDS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIENDS);
        onCreate(sqLiteDatabase);
    }

    public void add(Friend friend)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, friend.getName());
        values.put(KEY, friend.getKey());
        db.insert(TABLE_FRIENDS, null, values);
        db.close();
    }

    public String[] getFriendNames()
    {
        LinkedList<String> linkedList = new LinkedList<>();
        String query = "SELECT %s FROM " + TABLE_FRIENDS;
        Cursor cursor = this.getReadableDatabase().rawQuery(String.format(query, NAME), null);
        if (cursor.moveToFirst())
        {
            do
            {
                String string = cursor.getString(0);
                linkedList.add(string);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return linkedList.toArray(new String[]{});
    }

    public String[] getFriendKeys()
    {
        LinkedList<String> linkedList = new LinkedList<>();
        String query = "SELECT %s FROM " + TABLE_FRIENDS;
        Cursor cursor = this.getReadableDatabase().rawQuery(String.format(query, KEY), null);
        if (cursor.moveToFirst())
        {
            do
            {
                String string = Encryptor.decrypt(Constants.NDK_KEY, cursor.getString(0));
                linkedList.add(string);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return linkedList.toArray(new String[]{});
    }
}