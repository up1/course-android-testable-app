package up1.mynote.notes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import up1.mynote.R;
import up1.mynote.addnote.AddNoteActivity;
import up1.mynote.data.Note;
import up1.mynote.data.NoteRepository;
import up1.mynote.notedetail.NoteDetailActivity;

import static com.google.common.base.Preconditions.checkNotNull;

public class NoteFragment extends Fragment {
    private static final int REQUEST_ADD_NOTE = 1;

    private NoteAdapter mListAdapter;

    public NoteFragment() {
    }

    public static NoteFragment newInstance() {
        return new NoteFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListAdapter = new NoteAdapter(new ArrayList<Note>(0), mItemListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadNotes();
    }

    private void loadNotes() {
        swipeRefreshLayout.setRefreshing(true);
        List<Note> allNotes = NoteRepository.getInstance().getAllNotes();
        mListAdapter.replaceData(allNotes);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_ADD_NOTE == requestCode && Activity.RESULT_OK == resultCode) {
            Snackbar.make(getView(), getString(R.string.successfully_saved_note_message),
                    Snackbar.LENGTH_SHORT).show();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_note, container, false);
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.notes_list);
        recyclerView.setAdapter(mListAdapter);

        int numColumns = getContext().getResources().getInteger(R.integer.num_notes_columns);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numColumns));

        // Set up floating action button
        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_add_notes);

        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddNoteActivity.class);
                startActivityForResult(intent, REQUEST_ADD_NOTE);
            }
        });

        // Pull-to-refresh
        swipeRefreshLayout =
                (SwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                loadNotes();
            }
        });

        return root;
    }

    SwipeRefreshLayout swipeRefreshLayout;

    private void hideRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }

    NoteItemListener mItemListener = new NoteItemListener() {
        @Override
        public void onNoteClick(Note clickedNote) {
            checkNotNull(clickedNote, "requestedNote cannot be null!");
            Intent intent = new Intent(getContext(), NoteDetailActivity.class);
            intent.putExtra(NoteDetailActivity.EXTRA_NOTE_ID, clickedNote.getId());
            startActivity(intent);
        }
    };

    private static class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

        private List<Note> mNotes;
        private NoteItemListener mItemListener;

        public NoteAdapter(List<Note> notes, NoteItemListener itemListener) {
            setList(notes);
            mItemListener = itemListener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View noteView = inflater.inflate(R.layout.item_note, parent, false);

            return new ViewHolder(noteView, mItemListener);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            Note note = mNotes.get(position);

            viewHolder.title.setText(note.getTitle());
            viewHolder.description.setText(note.getDescription());
        }

        public void replaceData(List<Note> notes) {
            setList(notes);
            notifyDataSetChanged();
        }

        private void setList(List<Note> notes) {
            mNotes = checkNotNull(notes);
        }

        @Override
        public int getItemCount() {
            return mNotes.size();
        }

        public Note getItem(int position) {
            return mNotes.get(position);
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public TextView title;

            public TextView description;
            private NoteItemListener mItemListener;

            public ViewHolder(View itemView, NoteItemListener listener) {
                super(itemView);
                mItemListener = listener;
                title = (TextView) itemView.findViewById(R.id.note_detail_title);
                description = (TextView) itemView.findViewById(R.id.note_detail_description);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                Note note = getItem(position);
                mItemListener.onNoteClick(note);
            }
        }
    }

    public interface NoteItemListener {

        void onNoteClick(Note clickedNote);
    }
}
