package io.wisnu_krn.samplesqlite;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.LinkedList;

import io.wisnu_krn.samplesqlite.adapter.NoteAdapter;
import io.wisnu_krn.samplesqlite.db.NoteHelper;
import io.wisnu_krn.samplesqlite.entity.Note;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private RecyclerView rvNotes;
    private ProgressBar progressBar;
    private FloatingActionButton fabAdd;

    private LinkedList<Note> noteLinkedList;
    private NoteAdapter noteAdapter;
    private NoteHelper noteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Notes");

        rvNotes = (RecyclerView) findViewById(R.id.rv_notes);
        rvNotes.setHasFixedSize(true);
        rvNotes.setLayoutManager(new LinearLayoutManager(this));

        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        fabAdd = (FloatingActionButton) findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(this);

        noteHelper = new NoteHelper(this);
        noteHelper.open();

        noteLinkedList = new LinkedList<>();

        noteAdapter = new NoteAdapter(this);
        noteAdapter.setNoteLinkedList(noteLinkedList);
        rvNotes.setAdapter(noteAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new LoadNoteAsync().execute();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab_add){
            Intent intent = new Intent(MainActivity.this, FormAddUpdateActivity.class);
            startActivityForResult(intent, FormAddUpdateActivity.REQUEST_ADD);
        }
    }

    private class LoadNoteAsync extends AsyncTask<Void, Void, ArrayList<Note>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            if (noteLinkedList.size() > 0){
                noteLinkedList.clear();
            }
        }

        @Override
        protected ArrayList<Note> doInBackground(Void... params) {
            return noteHelper.getAllData();
        }

        @Override
        protected void onPostExecute(ArrayList<Note> notes) {
            super.onPostExecute(notes);
            progressBar.setVisibility(View.GONE);
            noteLinkedList.addAll(notes);
            noteAdapter.setNoteLinkedList(noteLinkedList);
            noteAdapter.notifyDataSetChanged();
            if (noteLinkedList.size() == 0){
                showSnackbarMessage("Tidak ada data saat ini");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FormAddUpdateActivity.REQUEST_ADD) {
            if (resultCode == FormAddUpdateActivity.RESULT_ADD){
                rvNotes.getLayoutManager().smoothScrollToPosition(rvNotes, new RecyclerView.State(), 0);
            }
        }

        if (requestCode == FormAddUpdateActivity.REQUEST_UPDATE){
            if (resultCode == FormAddUpdateActivity.RESULT_UPDATE){
                int position = data.getIntExtra(FormAddUpdateActivity.EXTRA_POSITION, 0);
                rvNotes.getLayoutManager().smoothScrollToPosition(rvNotes, new RecyclerView.State(), position);

                showSnackbarMessage("Satu item berhasil diubah");
            }
        }

        if (resultCode == FormAddUpdateActivity.RESULT_DELETE){
            int position = data.getIntExtra(FormAddUpdateActivity.EXTRA_POSITION, 0);
            noteLinkedList.remove(position);
            noteAdapter.setNoteLinkedList(noteLinkedList);
            noteAdapter.notifyDataSetChanged();

            showSnackbarMessage("Satu item berhasil dihapus");
        }
    }

    private void showSnackbarMessage(String message) {
        Snackbar.make(rvNotes, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (noteHelper != null){
            noteHelper.close();
        }
    }


}






















