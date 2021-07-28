package com.me.rnotes.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.me.rnotes.MainActivity;
import com.me.rnotes.NoteActivity;
import com.me.rnotes.R;
import com.me.rnotes.RecordedNote;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    private ListView listView;
    private NoteListAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // Get the notes list View form the room
        listView = root.findViewById(R.id.notes_list);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Enter the rows to the ListView
        ArrayList<String> items = new ArrayList<>();

        for (RecordedNote rn : MainActivity.RecordedNotes) {
            items.add(rn.getTitle());
        }

        adapter = new NoteListAdapter(getContext(), R.layout.note_list_item, items);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getContext(), NoteActivity.class);
                intent.putExtra("index", position);

                startActivity(intent);

            }
        });
    }
}