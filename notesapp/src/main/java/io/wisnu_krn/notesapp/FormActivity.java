package io.wisnu_krn.notesapp;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.wisnu_krn.notesapp.model.NoteItem;

public class FormActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText edtTitle, edtDescription;
    private Button btnSubmit;

    public static String EXTRA_NOTE = "note";
    private NoteItem noteItem;
    private boolean isUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        edtDescription = (EditText) findViewById(R.id.edt_description);
        edtTitle = (EditText) findViewById(R.id.edt_title);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(this);
        noteItem = getIntent().getParcelableExtra(EXTRA_NOTE);
        String actionBarTitle = null;
        String btnSubmitTitle = null;

        if (noteItem != null) {
            isUpdate = true;

            actionBarTitle = "Update";
            btnSubmitTitle = "Simpan";
            edtTitle.setText(noteItem.getTitle());
            edtDescription.setText(noteItem.getDascription());
        } else {
            actionBarTitle = "Tambah baru";
            btnSubmitTitle = "Submit";
        }

        btnSubmit.setText(btnSubmitTitle);
        getSupportActionBar().setTitle(actionBarTitle);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_submit){
            String title = edtTitle.getText().toString().trim();
            String description = edtDescription.getText().toString().trim();
            boolean isEmptyField = false;
            if (TextUtils.isEmpty(title)){
                isEmptyField = true;
                edtTitle.setError("Field tak boleh kosong");
            }

            if (!isEmptyField){
                ContentValues contentValues = new ContentValues();
                contentValues.put("title", title);
                contentValues.put("description", description);
                contentValues.put("date", getCurrentDate());

                if (isUpdate){
                    String selectionClauses = "_id=?";
                    String[] selectionArgs = {String.valueOf(noteItem.getId())};
                    getContentResolver().update(MainActivity.CONTENT_URI, contentValues, selectionClauses, selectionArgs);
                    Toast.makeText(this, "brhasl di update", Toast.LENGTH_SHORT).show();

                } else {
                    getContentResolver().insert(MainActivity.CONTENT_URI, contentValues);
                    Toast.makeText(this, "berhasil di input", Toast.LENGTH_SHORT).show();
                }

                finish();
            }

        }
    }

    private String getCurrentDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        return dateFormat.format(date);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isUpdate){
            getMenuInflater().inflate(R.menu.menu_form, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        if (item.getItemId() == R.id.action_delete){
            showDeleteAlertDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteAlertDialog() {
        String dialogMessage = "Apakah anda yakin ingin menghaspu item ini?";
        String dialogTitle = "Hapus Note";

        AlertDialog.Builder alBuilder = new AlertDialog.Builder(this);

        alBuilder.setTitle(dialogTitle);
        alBuilder.setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selectionClauses = "_id=?";
                        String[] selectionArgs = new String[]{String.valueOf(noteItem.getId())};
                        getContentResolver().delete(MainActivity.CONTENT_URI, selectionClauses, selectionArgs);
                        Toast.makeText(FormActivity.this, "brhasl di hapus", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alBuilder.create();
        alertDialog.show();

    }
}
