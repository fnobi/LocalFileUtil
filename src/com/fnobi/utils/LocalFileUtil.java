package com.fnobi.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;

public class LocalFileUtil {

    static public File create(Context context, String tmpFileName, Uri contentUri) {
        String tmpFilePath = getLocalFilePath(context, tmpFileName);

        File file;
        try {
            InputStream is = context.getContentResolver().openInputStream(contentUri);
            file = new File(tmpFilePath);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
            fileOutputStream.close();
            
            return file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    static public File create(Context context, String tmpFileName, Bitmap bitmap) {
        String tmpFilePath = getLocalFilePath(context, tmpFileName);

        File file = new File(tmpFilePath);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(CompressFormat.JPEG, 100, fos);
            fos.close();
            return file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    static public String getLocalFilePath(Context context, String tmpFileName) {
        String dataDir = appDirectory(context);
        if (dataDir == null) {
            return null;
        }
        String tmpFilePath = dataDir + "/" + tmpFileName;
        return tmpFilePath;
    }
    
    static public String appDirectory(Context context) {
        String dataDir = null;
        {
            PackageManager m = context.getPackageManager();
            String s = context.getPackageName();
            try {
                PackageInfo p = m.getPackageInfo(s, 0);
                dataDir = p.applicationInfo.dataDir;
            } catch (NameNotFoundException e) {
                
            }
        }
        return dataDir;
    }
}
