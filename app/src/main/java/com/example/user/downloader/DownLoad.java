package com.example.user.downloader;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;

public class DownLoad extends AppCompatActivity {

    final static private int GALLERY = 1000;

    Button Clear;
    EditText Texts;
    Button Starts;
    ImageView Views;
    DownLoadAsyncTask task;

    Toast no;
    Toast ok;

    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_load);

            no = Toast.makeText(this, "画像取得に失敗", Toast.LENGTH_LONG);
            ok = Toast.makeText(this, "ダウンロードが完了しました", Toast.LENGTH_LONG);

            //紐づけ
            Texts = findViewById(R.id.texts);
            Starts = findViewById(R.id.starts);
            Views = findViewById(R.id.views);
            Clear = findViewById(R.id.clear);


            //ダウンロード開始ボタンタップ時
            Starts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    url = Texts.getText().toString();

                    if (url.length() != 0) {
                        //EditTextに文字が入っている場合
                        task = new DownLoadAsyncTask();
                        //Lisntenerを設定
                        task.setListener(createListener());
                        task.execute(url);
                    } else {
                        no.show();

                    }
                }
            });


            //画像とテキストのクリアー
            Clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Texts.setText("");
                    Views.setImageDrawable(null);
                }
            });

        (findViewById(R.id.g_intent)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,GALLERY);
            }
        });
        }

//    @Override
//    protected void onDestroy() {
//        task.setListener(null);
//        super.onDestroy();
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY && resultCode == RESULT_OK){
           try {
               //選択した画像からbitmapを生成
               InputStream in = getContentResolver().openInputStream(data.getData());
               Bitmap bpm = BitmapFactory.decodeStream(in);
               in.close();

               //画像表示
               Views.setImageBitmap(bpm);
           }catch (Exception e){
               Log.e("Error", ":" + e);
           }
        }
    }

    private DownLoadAsyncTask.Listener createListener () {
            return new DownLoadAsyncTask.Listener() {
                @Override
                public void onSuccess(Bitmap bpm) {
                    Views.setImageBitmap(bpm);
                    if (bpm == null) {
                        no.show();
                    } else {
                        ok.show();
                        setSaved();
                    }
                    Log.d("BPMの中身", String.valueOf(bpm));
                }
            };
        }

        //保存用のクラスに値渡し
        private void setSaved () {
            Bitmap bitmapImage = ((BitmapDrawable) Views.getDrawable()).getBitmap();
            Material material = new Material(this);

            try {
                material.drectoryM(bitmapImage);
            } catch (Error e) {
                Log.e("setSave", "Error:" + e);
                Toast.makeText(DownLoad.this, "保存できません", Toast.LENGTH_SHORT).show();
            }finally {
                Toast.makeText(DownLoad.this, "保存できました", Toast.LENGTH_SHORT).show();
            }
        }
}
