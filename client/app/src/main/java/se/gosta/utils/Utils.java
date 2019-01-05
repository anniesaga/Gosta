package se.gosta.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import se.gosta.storage.Company;

/**
 * Helper class for handling and creating ImageFiles for a company's logo
 */

public class Utils {

    // String tag for logging
    private static final String LOG_TAG = Utils.class.getCanonicalName();

    // String constants for files and dirs
    private static final String FILE_SEP = "/" ;
    private static final String FIELD_SEP = "_" ;
    private static final Object AVATAR_DIR = "logos";
    private static final Object AVATAR_SUFFIX = ".png";

    // no objects needed, so keep the constructor private
    private Utils() {};

    /**
     * Creates a file for a Company given a bitmap
     * @param context used to find the app's directory
     * @param company
     * @param bitmap
     * @return
     * @throws IOException
     */
    public static File createImageFile(Context context, Company company, Bitmap bitmap) throws IOException {
        // Directory
        String dirName = fileDir(context);
        File dir = new File(dirName);
        if(!dir.exists()) {
            dir.mkdirs();
        }

        // Fileame
        String fileName = completeFileName(context, company);

        // Write to file
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream (fileName);
            bitmap.compress(Bitmap.CompressFormat.PNG, 85, outputStream);
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Log.d(LOG_TAG, "Created file: " + fileName);

        return new File(fileName);
    }

    // returns the directory name for an app using the Context
    private static String fileDir(Context c) {
        return c.getFilesDir().getAbsoluteFile()+
                FILE_SEP + AVATAR_DIR + FILE_SEP;
    }

    // returns the file name from a Company
    private static String fileName(Company company) {
        return (company.name() + FIELD_SEP + company.email()).replace(" ", FIELD_SEP) +
                AVATAR_SUFFIX;
    }

    // returns the complete file name from a Company and Context
    private static String completeFileName(Context c, Company company) {
        return fileDir(c) + FILE_SEP + fileName(company);
    }

    // returns the File from a Company and Context
    private static File completeFile(Context c, Company company) {
        return new File(completeFileName(c, company));
    }

    /**
     * Checks if a logo file for a company exists
     * @param c context used to get the app's directory
     * @param company - company which file to check for presence
     * @return true if the file exists, false otherwise
     */
    public static boolean avatarExists(Context c, Company company) {
        return completeFile(c, company).exists();
    }

    /**
     * Reads the content of the Company file and creates a bitmap from that.
     * @param c context used to get the app's directory
     * @param company - company which file to read from
     * @return bitmap or null if something failed
     */
    public static Bitmap avatarBitmap(Context c, Company company) {
        if (! avatarExists(c, company)) {
            return null;
        }
        return BitmapFactory.decodeFile(completeFileName(c,company));
    }

}