package base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by chengjie on 17-8-25.
 */

public class MyDataBase extends SQLiteOpenHelper {
    private Context context;
    public static final String CREATE_TEA = "create table tea(\n" +
            "\tid integer primary key autoincrement,\n" +
            "\tname  text,\n" +
            "\tphone text,\n" +
            "\tld    text,\n" +
            "\tsd    text\n" +
            ")";

    public MyDataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TEA);

        System.out.println("创建成功！");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists tea");
        onCreate(sqLiteDatabase);
    }
}
