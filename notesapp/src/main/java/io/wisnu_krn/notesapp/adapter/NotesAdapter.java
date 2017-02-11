package io.wisnu_krn.notesapp.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import io.wisnu_krn.notesapp.R;

/**
 * Created by wisnu on 11/02/2017.
 */

public class NotesAdapter extends CursorAdapter {
    public static String FIELD_TITLE = "title";
    public static String FIELD_DESCRIPTION = "description";
    public static String FIELD_DATE = "date";

    public NotesAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
        return view;

    }

    @Override
    public Cursor getCursor() {
        return super.getCursor();
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        if (cursor != null){
            TextView tvTitle = (TextView) view.findViewById(R.id.tv_item_title);
            TextView tvDate = (TextView) view.findViewById(R.id.tv_item_date);
            TextView tvDescription = (TextView) view.findViewById(R.id.tv_item_description);

            tvTitle.setText(cursor.getString(cursor.getColumnIndexOrThrow(FIELD_TITLE)));
            tvDescription.setText(cursor.getString(cursor.getColumnIndexOrThrow(FIELD_DESCRIPTION)));
            tvDate.setText(cursor.getString(cursor.getColumnIndexOrThrow(FIELD_DATE)));
        }
    }
}
