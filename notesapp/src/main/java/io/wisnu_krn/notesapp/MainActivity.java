package io.wisnu_krn.notesapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import io.wisnu_krn.notesapp.adapter.NotesAdapter;
import io.wisnu_krn.notesapp.model.NoteItem;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener{
    private NotesAdapter notesAdapter;
    private ListView lvNotes;
    private final int LOAD_NOTES_ID = 110;

    private static final String AUTHORITY = "io.wisnu_krn.samplesqlite";
    private static final String BASE_PATH = "notes";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Notes App");
        lvNotes = (ListView) findViewById(R.id.lv_notes);
        notesAdapter = new NotesAdapter(this, null, true);
        lvNotes.setAdapter(notesAdapter);
        lvNotes.setOnItemClickListener(this);
        getSupportLoaderManager().initLoader(LOAD_NOTES_ID, null, this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(LOAD_NOTES_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        notesAdapter.swapCursor(data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSupportLoaderManager().destroyLoader(LOAD_NOTES_ID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add){
            Intent intent = new Intent(MainActivity.this, FormActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        notesAdapter.swapCursor(null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor = (Cursor) notesAdapter.getItem(position);
        NoteItem noteItem = new NoteItem();
        noteItem.setId(cursor.getInt(cursor.getColumnIndexOrThrow("_id")));
        noteItem.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
        noteItem.setDate(cursor.getString(cursor.getColumnIndexOrThrow("date")));
        noteItem.setDascription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
        Intent intent = new Intent(MainActivity.this, FormActivity.class);
        intent.putExtra(FormActivity.EXTRA_NOTE, noteItem);
        startActivity(intent);
    }
}
