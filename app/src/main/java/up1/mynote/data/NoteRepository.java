package up1.mynote.data;

import android.support.v4.util.ArrayMap;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class NoteRepository {

    private static NoteRepository instance = null;
    private List<Note> allNotes;

    //Mock data
    private final static ArrayMap<String, Note> DATA;
    static {
        DATA = new ArrayMap(2);
        addNote("Hello Android", "I love youe", null);
        addNote("Code Smell", "Try to refactor it !!", null);
    }
    private static void addNote(String title, String description, String imageUrl) {
        Note newNote = new Note(title, description, imageUrl);
        DATA.put(newNote.getId(), newNote);
    }

    private NoteRepository() {}

    public static NoteRepository getInstance() {
        if(instance == null) {
            instance = new NoteRepository();
        }
        return instance;
    }

    public void saveNote(Note newNote) {
        checkNotNull(newNote);
        DATA.put(newNote.getId(), newNote);
    }

    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>(DATA.values());
        return ImmutableList.copyOf(notes);
    }


    public Note getNote(String noteId) {
        checkNotNull(noteId);
        return DATA.get(noteId);
    }
}
