package com.example.user.downloader;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Material {

    private Context mContext;
    private String Name = "SaveToImage_Iwamoto";

    public Material(Context context){
        this.mContext = context;
    }

    //ファイルネーム生成
    private String getFileName(){
        Date mDate = new Date();
        SimpleDateFormat fileNameFormat = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
        String saveName = fileNameFormat.format(mDate);
        return saveName;
    }

    //ディレクトリ取得とファイル生成の処理
    public void drectoryM(Bitmap bmp){
        String savePath = Environment.getExternalStorageDirectory().getPath() + "/" + Name;  //親と子のファイルパスを指定してあげる

        /*
            指定されたパス名文字列を抽象パス名に変換して、新しいFileインスタンスを作成します。
            抽象パス...OSに依存しないパスのこと
        */
        File file = new File(savePath);

        //exists()...ファイルが存在するかの確認
        if (!file.exists()){
            //今回はファイルが存在しなかった場合、ファイル作成
            file.mkdirs();
        }

        //絶対パスを生成
        String imagePath = savePath + '/' + "IMG_" + getFileName() + ".jpg"; //ファイル名取得

        try{
            FileOutputStream fos = new FileOutputStream(imagePath);
            //指定された大きさの圧縮された画像が保存されっる
            bmp.compress(Bitmap.CompressFormat.JPEG,100,fos);
            fos.flush(); //キャッシュ管理
            fos.close();

        } catch (Exception e) {
            e.getMessage();
        }finally {
            pushGallery(imagePath);
        }
    }

    //更新プログラム
    private void pushGallery(String getFileName){
        try {
            //ContentProviderの更新
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.Images.Media.TITLE, getFileName()); //ファイル名取得
            values.put("_data", getFileName);

            ContentResolver resolver = mContext.getContentResolver();
            resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        }catch (Exception e){
            Log.e("Error", "Error :" + e);
        }
    }
}
