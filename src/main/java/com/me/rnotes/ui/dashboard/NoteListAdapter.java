package com.me.rnotes.ui.dashboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.me.rnotes.MainActivity;
import com.me.rnotes.R;
import com.me.rnotes.RecordedNote;

import java.util.ArrayList;

public class NoteListAdapter extends ArrayAdapter<String> {

    private Context context;
    private int resource_id;
    private ArrayList<String> data;

    NoteListAdapter(@NonNull Context context, int resource, ArrayList<String> data) {
        super(context, resource, data);
        this.context = context;
        this.resource_id = resource;
        this.data = data;
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return super.getItem(position);
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View row;
        LayoutInflater inflater = LayoutInflater.from(context);
        row = inflater.inflate(resource_id, parent, false);

        TextView textView = row.findViewById(R.id.nlr_tv);
        textView.setText(data.get(position));

        return row;
    }
}
