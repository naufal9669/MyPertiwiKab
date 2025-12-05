package com.example.mypertiwikab.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
// PENTING: Import ini wajib untuk fitur rotasi
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.OpenableColumns;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {

    public static File getFile(Context context, Uri uri) throws IOException {
        String fileName = queryName(context, uri);
        File destinationFile = new File(context.getCacheDir(), fileName);

        try (InputStream inputStream = context.getContentResolver().openInputStream(uri)) {
            // 1. Decode Gambar dari Galeri
            Bitmap originalBitmap = BitmapFactory.decodeStream(inputStream);

            if (originalBitmap != null) {
                // --- 2. PERBAIKI ROTASI (Agar Tegak Lurus) ---
                // Kita panggil fungsi khusus di bawah untuk cek miring atau tidak
                Bitmap rotatedBitmap = rotateImageIfRequired(context, originalBitmap, uri);

                // --- 3. RESIZE (Kecilkan Ukuran) ---
                int maxWidth = 800;
                // Hitung tinggi baru agar proporsional
                int maxHeight = (int) (rotatedBitmap.getHeight() * (800.0 / rotatedBitmap.getWidth()));

                // Kalau gambar asli sudah kecil, jangan dibesarkan
                if (rotatedBitmap.getWidth() <= 800) {
                    maxWidth = rotatedBitmap.getWidth();
                    maxHeight = rotatedBitmap.getHeight();
                }

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(rotatedBitmap, maxWidth, maxHeight, true);

                // --- 4. KOMPRES (Agar Ringan Uploadnya) ---
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 60, bos); // Kualitas 60%
                byte[] bitmapdata = bos.toByteArray();

                // Simpan ke File Sementara
                FileOutputStream fos = new FileOutputStream(destinationFile);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return destinationFile;
    }

    // --- FUNGSI PENDUKUNG: CEK EXIF DAN PUTAR ---
    private static Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) throws IOException {
        InputStream input = context.getContentResolver().openInputStream(selectedImage);
        ExifInterface ei;

        // Baca data orientasi gambar
        if (Build.VERSION.SDK_INT > 23)
            ei = new ExifInterface(input);
        else
            ei = new ExifInterface(selectedImage.getPath());

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img; // Tidak perlu diputar
        }
    }

    // Fungsi Putar Bitmap
    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
    }

    // Fungsi Ambil Nama File
    private static String queryName(Context context, Uri uri) {
        Cursor returnCursor = context.getContentResolver().query(uri, null, null, null, null);
        if (returnCursor != null) {
            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            returnCursor.moveToFirst();
            String name = returnCursor.getString(nameIndex);
            returnCursor.close();
            return name;
        }
        return "temp_img_" + System.currentTimeMillis() + ".jpg";
    }
}