package up1.mynote.notedetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import up1.mynote.R;
import up1.mynote.data.Note;
import up1.mynote.data.NoteRepository;

public class NoteDetailFragment extends Fragment {
    public static final String ARGUMENT_NOTE_ID = "NOTE_ID";

    private TextView mDetailTitle;
    private TextView mDetailDescription;
    private ImageView mDetailImage;

    public static NoteDetailFragment newInstance(String noteId) {
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_NOTE_ID, noteId);
        NoteDetailFragment fragment = new NoteDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_note_detail, container, false);
        mDetailTitle = (TextView) root.findViewById(R.id.note_detail_title);
        mDetailDescription = (TextView) root.findViewById(R.id.note_detail_description);
        mDetailImage = (ImageView) root.findViewById(R.id.note_detail_image);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        String noteId = getArguments().getString(ARGUMENT_NOTE_ID);
        openNote(noteId);
    }

    private void openNote(String noteId) {
        Note note = NoteRepository.getInstance().getNote(noteId);
        String title = note.getTitle();
        String description = note.getDescription();
        String imageUrl = note.getImageUrl();

        mDetailTitle.setText(title);
        mDetailDescription.setText(description);
        if (imageUrl != null && !imageUrl.isEmpty()) {
            mDetailImage.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(new GlideDrawableImageViewTarget(mDetailImage) {
                        @Override
                        public void onResourceReady(GlideDrawable resource,
                                                    GlideAnimation<? super GlideDrawable> animation) {
                            super.onResourceReady(resource, animation);
                        }
                    });
        }
    }
}
