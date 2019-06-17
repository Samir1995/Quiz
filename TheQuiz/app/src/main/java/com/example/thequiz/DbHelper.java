package com.example.thequiz;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;

import com.example.thequiz.QuizContract.*;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MyAwesomeQuiz.db";
    private static final int DATABASE_VERSION = 1;

    public static DbHelper instance;

    public static SQLiteDatabase db;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DbHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        // Create Tables for db
        final String SQL_CREATE_CATEGORIE_TABEL = "CREATE TABLE " +
                CategorieTabel.TABLE_NAME + "( " +
                CategorieTabel._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CategorieTabel.COLUMN_NAME + " TEXT " +
                ")";

        final String SQL_CREATE_VRAGEN_TABEL = "CREATE TABLE " +
                VragenTabel.TABLE_NAME + " ( " +
                VragenTabel._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                VragenTabel.COLUMN_VRAAG + " TEXT, " +
                VragenTabel.COLUMN_OPTIE1 + " TEXT, " +
                VragenTabel.COLUMN_OPTIE2 + " TEXT, " +
                VragenTabel.COLUMN_OPTIE3 + " TEXT, " +
                VragenTabel.COLUMN_ANTW_NR + " INTEGER, " +
                VragenTabel.COLUMN_MOEILIJKHEID + " TEXT, " +
                VragenTabel.COLUMN_CATEGORIE_ID + " INTEGER, " +
                "FOREIGN KEY(" + VragenTabel.COLUMN_CATEGORIE_ID + ") REFERENCES " +
                CategorieTabel.TABLE_NAME + "(" + CategorieTabel._ID + ")" + "ON DELETE CASCADE" +
                ")";

        db.execSQL(SQL_CREATE_CATEGORIE_TABEL);
        db.execSQL(SQL_CREATE_VRAGEN_TABEL);
        vulCategorieTable();
        VulVraagTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CategorieTabel.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + VragenTabel.TABLE_NAME);
        onCreate(db);
    }
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }
    //fill Categorie table
    private void vulCategorieTable() {
        Categorie c1 = new Categorie("Programmeren");
        insertCategorie(c1);
        Categorie c2 = new Categorie("Aardrijkskunde");
        insertCategorie(c2);
        Categorie c3 = new Categorie("Wiskunde");
        insertCategorie(c3);
    }
    //insert categories in db
    private void insertCategorie(Categorie categorie) {
        ContentValues cv = new ContentValues();
        cv.put(CategorieTabel.COLUMN_NAME, categorie.getName());
        db.insert(CategorieTabel.TABLE_NAME, null, cv);
    }

    //fill Vragen table
    private void VulVraagTable() {
        Vragen q1 = new Vragen("Wat is Java?",
                "Een Programeertaal ", "Eten", "Een land", 1,
                Vragen.MOEILIJKHEID_MAK, Categorie.PROGRAMMING);
        insertVraag(q1);
        Vragen q2 = new Vragen("Waar ligt Beverwijk?",
                "Frankrijk", "Nederland", "Duitsland", 2,
                Vragen.MOEILIJKHEID_GEM, Categorie.GEOGRAPHY);
        insertVraag(q2);
        Vragen q3 = new Vragen("Wat is 2345 + 5896?",
                "6780", "5678", "8241", 3,
                Vragen.MOEILIJKHEID_MOE, Categorie.MATH);
        insertVraag(q3);
        Vragen q4 = new Vragen("Wat is 2 * 2?",
                "4", "8", "2", 1,
                Vragen.MOEILIJKHEID_MAK, Categorie.MATH);
        insertVraag(q4);
        Vragen q5 = new Vragen("Waar ligt Amsterdam?",
                "Nederland", "USA", "Duitsland", 1,
                Vragen.MOEILIJKHEID_MAK, Categorie.GEOGRAPHY);
        insertVraag(q5);
        Vragen q6 = new Vragen("Wat doet System.out.println?",
                "Het doet niks", "Print parameter in tekstterminal", "Het verwijderd alles", 2,
                Vragen.MOEILIJKHEID_GEM, Categorie.PROGRAMMING);
        insertVraag(q6);
    }

    //Add new question
    public void addVraag(Vragen vraag) {
        db = getWritableDatabase();
        insertVraag(vraag);
    }
    //insert new question in db
    private void insertVraag(Vragen question) {
        ContentValues cv = new ContentValues();
        cv.put(VragenTabel.COLUMN_VRAAG, question.getVraag());
        cv.put(VragenTabel.COLUMN_OPTIE1, question.getOptie1());
        cv.put(VragenTabel.COLUMN_OPTIE2, question.getOptie2());
        cv.put(VragenTabel.COLUMN_OPTIE3, question.getOptie3());
        cv.put(VragenTabel.COLUMN_ANTW_NR, question.getAntwoordNr());
        cv.put(VragenTabel.COLUMN_MOEILIJKHEID, question.getMoeilijkheid());
        cv.put(VragenTabel.COLUMN_CATEGORIE_ID, question.getCategorieID());
        db.insert(VragenTabel.TABLE_NAME, null, cv);
    }

    //get all categories from db
    public List<Categorie> getAllCategories() {
        List<Categorie> categoryList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + CategorieTabel.TABLE_NAME, null);

        //get new vragenobject with it's values
        if (c.moveToFirst()) {
            do {
                Categorie categorie = new Categorie();
                categorie.setId(c.getInt(c.getColumnIndex(CategorieTabel._ID)));
                categorie.setName(c.getString(c.getColumnIndex(CategorieTabel.COLUMN_NAME)));
                categoryList.add(categorie);
            } while (c.moveToNext());
        }

        c.close();
        return categoryList;
    }

    //get all categories from db
    public List<Vragen> getAllVragen() {
        List<Vragen> vragenList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + VragenTabel.TABLE_NAME, null);

        //get new vragenobject with it's values
        if (c.moveToFirst()) {
            do {
                Vragen vraag = new Vragen();
                vraag.setId(c.getInt(c.getColumnIndex(VragenTabel._ID)));
                vraag.setVraag(c.getString(c.getColumnIndex(VragenTabel.COLUMN_VRAAG)));
                vraag.setOptie1(c.getString(c.getColumnIndex(VragenTabel.COLUMN_OPTIE1)));
                vraag.setOptie2(c.getString(c.getColumnIndex(VragenTabel.COLUMN_OPTIE2)));
                vraag.setOptie3(c.getString(c.getColumnIndex(VragenTabel.COLUMN_OPTIE3)));
                vraag.setAntwoordNr(c.getInt(c.getColumnIndex(VragenTabel.COLUMN_ANTW_NR)));
                vraag.setMoeilijkheid(c.getString(c.getColumnIndex(VragenTabel.COLUMN_MOEILIJKHEID)));
                vraag.setCategorieID(c.getInt(c.getColumnIndex(VragenTabel.COLUMN_CATEGORIE_ID)));
                vraag.setCategorieID(c.getInt(c.getColumnIndex(VragenTabel.COLUMN_CATEGORIE_ID)));
                vragenList.add(vraag);
            } while (c.moveToNext());
        }

        c.close();
        return vragenList;
    }

    //get questions with right categories wn diff from db
    public ArrayList<Vragen> getVraag(int categorieID, String moeilijkheid) {
        ArrayList<Vragen> vraagList = new ArrayList<>();
        db = getReadableDatabase();

        String selectie = VragenTabel.COLUMN_CATEGORIE_ID + " = ? " +
                " AND " + VragenTabel.COLUMN_MOEILIJKHEID + " = ? ";
        String[] selectieArgs = new String[]{String.valueOf(categorieID), moeilijkheid};

        Cursor c = db.query(
                VragenTabel.TABLE_NAME,
                null,
                selectie,
                selectieArgs,
                null,
                null,
                null
        );

        if (c.moveToFirst()) {
            do {
                Vragen vraag = new Vragen();
                vraag.setId(c.getInt(c.getColumnIndex(VragenTabel._ID)));
                vraag.setVraag(c.getString(c.getColumnIndex(VragenTabel.COLUMN_VRAAG)));
                vraag.setOptie1(c.getString(c.getColumnIndex(VragenTabel.COLUMN_OPTIE1)));
                vraag.setOptie2(c.getString(c.getColumnIndex(VragenTabel.COLUMN_OPTIE2)));
                vraag.setOptie3(c.getString(c.getColumnIndex(VragenTabel.COLUMN_OPTIE3)));
                vraag.setAntwoordNr(c.getInt(c.getColumnIndex(VragenTabel.COLUMN_ANTW_NR)));
                vraag.setMoeilijkheid(c.getString(c.getColumnIndex(VragenTabel.COLUMN_MOEILIJKHEID)));
                vraagList.add(vraag);
            } while (c.moveToNext());
        }

        c.close();
        return vraagList;
    }
}
