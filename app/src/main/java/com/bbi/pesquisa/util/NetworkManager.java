package com.bbi.pesquisa.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.embarcadero.javaandroid.ConnectionFactory;
import com.bbi.pesquisa.model.NetworkConfiguration;
import com.embarcadero.javaandroid.DSProxy;
import com.embarcadero.javaandroid.DSRESTConnection;

public class NetworkManager extends SQLiteOpenHelper {
    public static DSProxy.TSvrMethod method;

    private static final int VERSION = 1;
    private static final String DBNAME = "db";

    private static final String TABLE = "tbl_autenticacao";
    private static final String COL_ID = "id";
    private static final String COL_IP = "ip";
    private static final String COL_SSID = "ssid";
    private static final String COL_PORT = "port";

    public NetworkManager(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql =
                "create table " + TABLE + "("
                        + COL_ID + " integer primary key,"
                        + COL_IP + " text, "
                        + COL_SSID + " text, "
                        + COL_PORT + " integer);";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists " + TABLE);
        onCreate(db);
    }


    public void insertConfiguration(String ip, int port)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_ID, 1);
        values.put(COL_IP, ip);
        values.put(COL_PORT, port);

//        connect(ip, port);

        db.insert(TABLE, null, values);
    }

    public void updateConfiguration(String ip, int port)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put( COL_IP, ip );
        values.put( COL_PORT, port );

        String[] args = {String.valueOf(1)};

        db.update(TABLE, values,COL_ID + "=?", args);

        connect(ip, port);

        Log.d("DbManager", "Update Success");
    }

    public NetworkConfiguration getConfiguration()
    {
        String[] id = new String[]{String.valueOf(1)};
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = new String[]{COL_ID, COL_IP, COL_PORT, COL_SSID};

        Cursor cursor = db.query(TABLE, columns, COL_ID + "=?", id,
                                null, null, null, null);

        NetworkConfiguration network = new NetworkConfiguration();

        if (cursor.moveToFirst())
        {
            network.setId(cursor.getInt(cursor.getColumnIndex(COL_ID)));
            network.setIp(cursor.getString(cursor.getColumnIndex(COL_IP)));
            network.setPort(cursor.getInt(cursor.getColumnIndex(COL_PORT)));
            network.setSsid(cursor.getString(cursor.getColumnIndex(COL_SSID)));
        }

        connect(network.getIp(), network.getPort());
        return network;
    }

    private void connect(String ip, int port) {

        ConnectionFactory.sIp = ip;
        ConnectionFactory.sPorta = port;

        DSRESTConnection conn = ConnectionFactory.getConnection();
        method = new DSProxy.TSvrMethod(conn);
    }

    public void delete() {
        String[] id = new String[]{String.valueOf(1)};
        SQLiteDatabase db = this.getReadableDatabase();

        db.delete(TABLE, COL_ID + "=?", id);
    }

}