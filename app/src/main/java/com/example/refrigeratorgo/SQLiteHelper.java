package com.example.refrigeratorgo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "RefrigeratorGo";


    //db 생성
    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, version);
    }

    //db 생성시점
    public void queryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    //카테고리 별 음식 추가 insert
    public void insertDataFood(String name, String date, byte[] image, String category, String memo){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO FOOD VALUES (NULL, ?, ?, ?, ?, ?)";

        SQLiteStatement statement = database.compileStatement(sql) ;
        statement.clearBindings();

        statement.bindString(1, name);
        statement.bindString(2, date);
        statement.bindBlob(3, image);
        statement.bindString(4, category);
        statement.bindString(5, memo);

        statement.executeInsert();
    }

    //레시피 내 스크랩 insert
    public void insertDataRecipes(String name, String scrap, String detailurl, String imgurl) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO RECIPES VALUES (NULL, ?, ?, ?, ?)";

        SQLiteStatement statement = database.compileStatement(sql) ;
        statement.clearBindings();

        statement.bindString(1, name);
        statement.bindString(2, scrap);
        statement.bindString(3, detailurl);
        statement.bindString(4, imgurl);

        statement.executeInsert();
    }

    //체크리스트 추가 insert
    public void insertDataCheck(String name){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO CHECKLIST VALUES (NULL, ?)";

        SQLiteStatement statement = database.compileStatement(sql) ;
        statement.clearBindings();

        statement.bindString(1, name);

        statement.executeInsert();
    }

    //알람 내 insert
    public void insertDataAlarm(String name, String date) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO ALARM VALUES (NULL, ?, ?)";

        SQLiteStatement statement = database.compileStatement(sql) ;
        statement.clearBindings();

        statement.bindString(1, name);
        statement.bindString(2, date);

        statement.executeInsert();
    }

    //음식추가 수정에서 업데이트
    public void updateDataFood(String name, String date, byte[] image, String category, String memo, int id){
        SQLiteDatabase database = getWritableDatabase();

        String sql = "UPDATE FOOD SET name = ?, date = ?, image = ?, category = ?, memo = ? WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindString(1, name);
        statement.bindString(2, date);
        statement.bindBlob(3, image);
        statement.bindString(4, category);
        statement.bindString(5, memo);
        statement.bindDouble(6, (double)id);

        statement.executeInsert();
        database.close();
    }

    //음식추가 데이터 삭제
    public  void deleteDataFood(int id) {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "DELETE FROM FOOD WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, (double)id);

        statement.execute();
        database.close();
    }

    //레시피 데이터 삭제
    public void deleteDataRecipes(int id) {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "DELETE FROM RECIPES WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, (double)id);

        statement.execute();
        database.close();
    }

    //체크리스트 데이터 삭제
    public  void deleteDataCheck(int id) {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "DELETE FROM CHECKLIST WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, (double)id);

        statement.execute();
        database.close();
    }

    //알람 데이터 삭제
    public void deleteDataAlarm(int id) {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "DELETE FROM ALARM WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, (double)id);

        statement.execute();
        database.close();
    }

    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }




}
