package io.wisnu_krn.samplesqlite.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;

import io.wisnu_krn.samplesqlite.CustomOnItemClickListener;
import io.wisnu_krn.samplesqlite.FormAddUpdateActivity;
import io.wisnu_krn.samplesqlite.R;
import io.wisnu_krn.samplesqlite.entity.Note;

/**
 * Created by wisnu on 09/02/2017.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private LinkedList<Note> noteLinkedList;
    private Activity activity;

    public NoteAdapter(Activity activity) {
        this.activity = activity;
    }

    public LinkedList<Note> getNoteLinkedList(){
        return noteLinkedList;
    }

    public void setNoteLinkedList(LinkedList<Note> noteLinkedList){
        this.noteLinkedList = noteLinkedList;
    }

    @Override
    public NoteAdapter.NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);

        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NoteAdapter.NoteViewHolder holder, int position) {
        holder.tvTitle.setText(getNoteLinkedList().get(position).getTitle());
        holder.tvDate.setText(getNoteLinkedList().get(position).getDate());
        holder.tvDesciption.setText(getNoteLinkedList().get(position).getDescription());


        holder.cvNote.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                Intent intent = new Intent(activity, FormAddUpdateActivity.class);
                intent.putExtra(FormAddUpdateActivity.EXTRA_POSITION, position);
                intent.putExtra(FormAddUpdateActivity.EXTRA_NOTE, getNoteLinkedList().get(position));
                activity.startActivityForResult(intent, FormAddUpdateActivity.REQUEST_UPDATE);
                Toast.makeText(activity, getNoteLinkedList().get(position).getTitle(), Toast.LENGTH_SHORT).show();

            }
        }));
    }

    @Override
    public int getItemCount() {
        return getNoteLinkedList().size();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDesciption, tvDate;
        CardView cvNote;

        public NoteViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_item_title);
            tvDate = (TextView) itemView.findViewById(R.id.tv_item_date);
            tvDesciption = (TextView) itemView.findViewById(R.id.tv_item_description);
            cvNote = (CardView) itemView.findViewById(R.id.cv_item_note);
        }
    }
}
