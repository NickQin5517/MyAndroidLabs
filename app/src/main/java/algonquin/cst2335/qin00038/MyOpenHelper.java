package algonquin.cst2335.qin00038;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyOpenHelper extends SQLiteOpenHelper {

    public static final String name = "TheDatabase";
    public static final int version = 1;
    public static final String TABLE_NAME = "Messages";
    public static final String col_id = "_id";
    public static final String col_message = "Message";
    public static final String col_send_receive = "SendOrReceive";
    public static final String col_time_sent = "TimeSent";

    public MyOpenHelper(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( String.format( " Create table %s ( %s INTEGER PRiMARY KEY AUTOINCREMENT, "
                   + " %s TEXT, "
                   + " %s INTEGER, %s TEXT );", TABLE_NAME, col_id, col_message, col_send_receive, col_time_sent));

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(" drop table if exists " + TABLE_NAME);
        onCreate(db);

    }
}
