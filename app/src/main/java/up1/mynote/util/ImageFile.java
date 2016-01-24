package up1.mynote.util;

import android.net.Uri;
import android.os.Environment;
import android.support.annotation.VisibleForTesting;

import java.io.File;
import java.io.IOException;

public class ImageFile {

    @VisibleForTesting
    File imageFile;

    public void create(String name, String extension) throws IOException {
        File storageDirectory = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);

        imageFile = File.createTempFile(
                name,
                extension,
                storageDirectory
        );
    }

    public boolean exists() {
        return null != imageFile && imageFile.exists();
    }

    public void delete() {
        imageFile = null;
    }

    public String getPath() {
        return Uri.fromFile(imageFile).toString();
    }
}
