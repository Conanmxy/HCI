package com.example.s1.DiaryActivity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.s1.MainActivity;
import com.example.s1.R;
import com.example.s1.Utils.ImageUtils;
import com.example.s1.Utils.ScreenUtils;
import com.example.s1.entity.DiaryText;
import com.example.s1.rxjava.RxBus2;

import org.litepal.crud.DataSupport;

import java.sql.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DiaryDetailActivity extends AppCompatActivity {

    public static final int CHOOSE_PHOTO = 2;

    //添加的两张图
    private ImageView addPic;
    private ImageView hideSoftInput;

    private Toolbar toolbar;
    ImageButton left;
    ImageButton right;
    TextView title;
    EditText diary_edit;
    String input;//要显示的字符串
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_detail);
        initialView();
        initialEvent();
        show(input);
    }
    //region
    private void initialView()
    {
        left=(ImageButton) findViewById(R.id.title_left);
        right=(ImageButton) findViewById(R.id.title_right);
        title=(TextView)findViewById(R.id.title_text);
        diary_edit=(EditText)findViewById(R.id.diary_content);
        title.setText("日记详情");
        left.setImageResource(R.mipmap.back_white);
        right.setImageResource(R.mipmap.delete_white);
        input=getIntent().getStringExtra("existed_text");
        id=getIntent().getIntExtra("current_id",0);

        //
        addPic = (ImageView) findViewById(R.id.diary_detail_add_pic_ib);
        hideSoftInput=(ImageView)findViewById(R.id.diary_detail_hideSoftInput_ib);

    }


    //监听手机返回键
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DiaryText diaryText = new DiaryText();
        int currentId = getIntent().getIntExtra("current_id", 0);
        diaryText.setDiaryText(diary_edit.getText().toString());
        diaryText.updateAll("id=?", String.valueOf(currentId));
        RxBus2.getDefault().post(diaryText);
    }

    private void initialEvent()
    {
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DiaryText diaryText = new DiaryText();
                int currentId = getIntent().getIntExtra("current_id", 0);
                diaryText.setDiaryText(diary_edit.getText().toString());
                diaryText.updateAll("id=?", String.valueOf(currentId));
                RxBus2.getDefault().post(diaryText);
                finish();
            }
        });



        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new  AlertDialog.Builder(DiaryDetailActivity.this)
                        .setTitle("提示" )
                        .setMessage("确定删除吗？" )
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //删除功能
                                int currentId = getIntent().getIntExtra("current_id", 0);
                                DataSupport.deleteAll(DiaryText.class, "id=?", String.valueOf(currentId));
                                diary_edit.setText("");
                                DiaryText diaryText = new DiaryText();
                                RxBus2.getDefault().post(diaryText);
                                finish();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();

            }
        });


        diary_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //单击可编辑
                diary_edit.setFocusable(true);
                diary_edit.setFocusableInTouchMode(true);
                diary_edit.requestFocus();
                diary_edit.requestFocusFromTouch();

                //删除编程保存
                right.setImageResource(R.mipmap.finish_white);
                //更改标题
                title.setText("编辑");
                //重新定义监听事件
                right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (title.getText().toString().equals("新建")) {
                            //此时插入数据库
                            SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                            editor.putString("diary", diary_edit.getText().toString());
                            editor.apply();
                            // SimpleDateFormat formatter =  new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss");
                            Date date = new Date(System.currentTimeMillis());
                            DiaryText diaryText1 = new DiaryText();
                            String diaryText=diary_edit.getText().toString();
                            diaryText1.setDiaryText(diaryText);
                            diaryText1.setDate(date.toString());
                            Log.d("Diary_Text....:",diaryText);
                            diaryText1.save();
                            RxBus2.getDefault().post(diaryText1);
                            finish();
                        }
                        else if (title.getText().toString().equals("编辑"))
                        {//修改的功能
                            DiaryText diaryText = new DiaryText();
                            int currentId = getIntent().getIntExtra("current_id", 0);

                            diaryText.setDiaryText(diary_edit.getText().toString());
                            diaryText.updateAll("id=?", String.valueOf(currentId));
                            RxBus2.getDefault().post(diaryText);
                            finish();
                        }


                    }
                });

            }
        });






        //插入图片部分
        addPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(DiaryDetailActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(DiaryDetailActivity.this, new
                            String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                }
            }
        });


        //隐藏键盘部分
        hideSoftInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("hideinput","hide");
                InputMethodManager inputMethodManager=(InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(diary_edit.getWindowToken(),0);
            }
        });
    }
    private void show(String input)
     {
         //input 是获取将被解析的字符串
         //将图片那一串字符串解析出来，即<img src="xxx"/>
         Pattern p=Pattern.compile("\\<img src=\".*?\"\\/>");
         Matcher m=p.matcher(input);

         SpannableString spannable=new SpannableString(input);
         while(m.find())
         {
             Log.d("rgex",m.group());
             String s=m.group();
             int start=m.start();
             int end=m.end();
             //path是去<img rsc=/>的中间的图片路径
             String path=s.replaceAll("\\<img src=\"|\"\\/>","").trim();
             Log.d("eliminte",path);

             //利用spannableString 和 imageSpan来替换掉这些图片
             int width=ScreenUtils.getScreenWidth(DiaryDetailActivity.this);
             int height=ScreenUtils.getScreenHeight(DiaryDetailActivity.this);

             Bitmap bitmap= ImageUtils.getSmallBitmap(path,width,480);
             ImageSpan imageSpan=new ImageSpan(this,bitmap);
             spannable.setSpan(imageSpan,start,end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
         }
         diary_edit.setText(spannable);
         diary_edit.append("\n");
         Log.d("last",diary_edit.getText().toString());
     }

    Html.ImageGetter imageGetter=new Html.ImageGetter() {
        @Override
        public Drawable getDrawable(String source) {
            int width= ScreenUtils.getScreenWidth(DiaryDetailActivity.this);
            int height=ScreenUtils.getScreenHeight(DiaryDetailActivity.this);
            Bitmap bitmap= ImageUtils.getSmallBitmap(source,width,200);
            Drawable drawable=new BitmapDrawable(bitmap);
            drawable.setBounds(0,0,width,height);
            return drawable;
        }
    };


    //endregion





























    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);//打开相册
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        //4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    } else {
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果是document 类型的uri 则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                //如果是content类型的URI，则使用普通方式处理
                imagePath = getImagePath(uri, null);
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                //如果是file类型的URI，则直接或路径
                imagePath = uri.getPath();
            }
            //displayImage(imagePath);//根据图片路径显示图片
            Log.d("路径1",imagePath);
            insertImg(imagePath);
        }
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        //displayImage(imagePath);
        Log.d("路径2",imagePath);
        insertImg(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过URI和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }


    private void insertImg(String path)
    {

        //为图片路径加上<img>标签
        String tagPath="<img src=\""+path+"\"/>";
        Bitmap bitmap= BitmapFactory.decodeFile(path);
        if(bitmap!=null)
        {
            SpannableString ss=getBitmapMime2(path,tagPath);
            insertPhotoToEditText(ss);
            diary_edit.append("\n");
            Log.d("YYPT",diary_edit.getText().toString());
        }

    }

    private SpannableString getBitmapMime2(String path,String tagPath)
    {
        SpannableString ss=new SpannableString(tagPath);

        int width=ScreenUtils.getScreenWidth(DiaryDetailActivity.this);
        int height= ScreenUtils.getScreenHeight(DiaryDetailActivity.this);

        Bitmap bitmap= ImageUtils.getSmallBitmap(path,width,480);
        ImageSpan imageSpan=new ImageSpan(this,bitmap);
        ss.setSpan(imageSpan,0,tagPath.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    private void insertPhotoToEditText(SpannableString ss)
    {
        Editable et=diary_edit.getText();
        int start=diary_edit.getSelectionStart();
        et.insert(start,ss);
        diary_edit.setText(et);
        //  editText.setText(Html.fromHtml(et,imageGetter,null));
        diary_edit.setSelection(start+ss.length());
        diary_edit.setFocusableInTouchMode(true);
        diary_edit.setFocusable(true);
    }

    //图片缩放
    public Bitmap resizeImage(Bitmap bitmaporg,int widthNew,int heightNew)
    {
        int widthOld=bitmaporg.getWidth();
        int heightOld=bitmaporg.getHeight();
        float scaleWidht=(float)widthNew/widthOld;
        float scaleHeight=(float)heightNew/heightOld;
        Matrix matrix=new Matrix();
        matrix.postScale(scaleWidht,scaleHeight);
        Bitmap bitmapNew=Bitmap.createBitmap(bitmaporg,0,0,widthOld,heightOld,matrix,true);
        return bitmapNew;
    }
}
