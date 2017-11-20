package gacglc.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import gacglc.app.Gare;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by guigo on 23/10/2017.
 */

public class DbHelper extends OrmLiteSqliteOpenHelper {

    private static String DB_NAME = "myGares";
    private static int DB_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Gare.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }


    public RuntimeExceptionDao<Gare, String> getDataDao() {
        return getRuntimeExceptionDao(Gare.class);
    }

}