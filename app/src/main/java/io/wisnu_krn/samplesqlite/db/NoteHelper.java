package io.wisnu_krn.samplesqlite.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import io.wisnu_krn.samplesqlite.entity.Note;

/**
 * Created by wisnu on 09/02/2017.
 */

public class NoteHelper {
    private static String DATABASE_TABLE = DatabaseHelper.TABLE_NAME;
    private Context context;
    private DatabaseHelper databaseHelper;

    private SQLiteDatabase database;

    public NoteHelper(Context context) {
        this.context = context;
    }

    public NoteHelper open() throws SQLException {
        databaseHelper = new DatabaseHelper(context);
        database = databaseHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        databaseHelper.close();
    }

    public Cursor searchQuery(String title) {
        return database.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE " + DatabaseHelper.FIELD_TITLE + " LIKE '%" + title + "%'", null);
    }

    public ArrayList<Note> getSearchResult(String keyword) {
        ArrayList<Note> noteArrayList = new ArrayList<>();
        Cursor cursor = searchQuery(keyword);
        cursor.moveToFirst();

        Note note;
        if (cursor.getCount() > 0) {
            do {
                note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_TITLE)));
                note.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_DESCRIPTION)));
                note.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_DATE)));

                noteArrayList.add(note);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return noteArrayList;
    }

    public String getData(String word) {
        String result = "";
        Cursor cursor = searchQuery(word);
        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            result = cursor.getString(2);
            for (; !cursor.isAfterLast(); cursor.moveToNext()) {
                result = cursor.getString(2);
            }
        }
        cursor.close();
        return result;
    }

    public Cursor queryAllData() {
        return database.rawQuery("SELECT * FROM " + DATABASE_TABLE + " ORDER BY " + DatabaseHelper.FIELD_ID + " DESC", null);
    }

    public ArrayList<Note> getAllData() {
        ArrayList<Note> noteArrayList = new ArrayList<>();
        Cursor cursor = queryAllData();
        cursor.moveToFirst();

        Note note;
        if (cursor.getCount() > 0) {
            do {
                note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_TITLE)));
                note.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_DESCRIPTION)));
                note.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_DATE)));

                noteArrayList.add(note);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return noteArrayList;
    }

    public long insert(Note note) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(DatabaseHelper.FIELD_TITLE, note.getTitle());
        initialValues.put(DatabaseHelper.FIELD_DESCRIPTION, note.getDescription());
        initialValues.put(DatabaseHelper.FIELD_DATE, note.getDate());
        return database.insert(DATABASE_TABLE, null, initialValues);
    }

    public long update(Note note) {
        ContentValues args = new ContentValues();
        args.put(DatabaseHelper.FIELD_TITLE, note.getTitle());
        args.put(DatabaseHelper.FIELD_DESCRIPTION, note.getDescription());
        args.put(DatabaseHelper.FIELD_DATE, note.getDate());
        return database.update(DATABASE_TABLE, args, DatabaseHelper.FIELD_ID + "= '" + note.getId() + "'", null);
    }

    public void delete(int id){
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.FIELD_ID + " = '" + id + "'", null);
    }
}
