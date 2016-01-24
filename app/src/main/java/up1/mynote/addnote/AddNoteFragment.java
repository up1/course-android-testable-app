package up1.mynote.addnote;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.text.SimpleDateFormat;
import java.util.Date;

import up1.mynote.R;
import up1.mynote.data.Note;
import up1.mynote.data.NoteRepository;
import up1.mynote.util.ImageFile;

import static com.google.common.base.Preconditions.checkState;

public class AddNoteFragment extends Fragment {

    private static final int REQUEST_CODE_IMAGE_CAPTURE = 0x1001;

    private TextView mTitle;
    private TextView mDescription;
    private ImageView mImageThumbnail;

    private ImageFile imageFile = new ImageFile();

    public static AddNoteFragment newInstance() {
        return new AddNoteFragment();
    }

    public AddNoteFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_add_notes);
        fab.setImageResource(R.drawable.ic_done);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote(mTitle.getText().toString(), mDescription.getText().toString());
            }
        });
    }

    private void saveNote(String title, String description) {
        String imageUrl = null;
        if( imageFile.exists() ) {
            imageUrl = imageFile.getPath();
        }
        Note newNote = new Note(title, description, imageUrl);
        NoteRepository noteRepository = NoteRepository.getInstance();
        noteRepository.saveNote(newNote);

        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_addnote, container, false);
        mTitle = (TextView) root.findViewById(R.id.add_note_title);
        mDescription = (TextView) root.findViewById(R.id.add_note_description);
        mImageThumbnail = (ImageView) root.findViewById(R.id.add_note_image_thumbnail);

        setHasOptionsMenu(true);
        setRetainInstance(true);
        return root;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.take_picture:
                try {
                    takePicture();
                } catch (Exception ioe) {
                    if (getView() != null) {
                        Snackbar.make(getView(), getString(R.string.take_picture_error),
                                Snackbar.LENGTH_LONG).show();
                    }
                }
                return true;
        }
        return false;
    }

    private void takePicture() throws Exception {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        imageFile = new ImageFile();
        imageFile.create(imageFileName, ".jpg");

        // Open the camera to take a picture.
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Check if there is a camera app installed to handle our Intent
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.parse(imageFile.getPath()));
            startActivityForResult(takePictureIntent, REQUEST_CODE_IMAGE_CAPTURE);
        } else {
            Snackbar.make(mTitle, getString(R.string.cannot_connect_to_camera_message),
                    Snackbar.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_addnote_options_menu_actions, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // If an image is received, display it on the ImageView.
        if (REQUEST_CODE_IMAGE_CAPTURE == requestCode && Activity.RESULT_OK == resultCode) {
//            mActionListener.imageAvailable();
            previewImage();
        } else {
//            mActionListener.imageCaptureFailed();
        }
    }

    private void previewImage() {
        if (imageFile.exists()) {
            String imageUrl = imageFile.getPath();
            checkState(!TextUtils.isEmpty(imageUrl), "imageUrl cannot be null or empty!");
            mImageThumbnail.setVisibility(View.VISIBLE);
            // This app uses Glide for image loading
            Glide.with(this)
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(new GlideDrawableImageViewTarget(mImageThumbnail) {
                        @Override
                        public void onResourceReady(GlideDrawable resource,
                                                    GlideAnimation<? super GlideDrawable> animation) {
                            super.onResourceReady(resource, animation);
                        }
                    });
        }
    }

}
