package zhu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daoshengwanwu.android.tourassistant.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PersonalDataActivity extends AppCompatActivity {
    private static final String EXTRA_USER_NAME = "SecondActivity.EXTRA_USER_NAME";
    private Bitmap bitMap;
    private boolean hasImage;
    private ImageView imageView;
    private Button button;
    private String pathString;
    private RelativeLayout head;
    private RelativeLayout nickname;
    private RelativeLayout password;
    private RelativeLayout sex;
    private TextView name;
    private TextView usersex;
    private EditText editName;
    private String[] sexs=new String[2];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhu_activity_personaldata);

        //头像图片
        pathString= Environment.getExternalStorageDirectory()+"\\picture";
        File file=new File(pathString);
        if(!file.exists())
        {
            file.mkdir();
        }
        findView();
        setListener();

    }
    public static Intent newIntent(Context packageContext, String userName) {
        Intent i = new Intent(packageContext, PersonalDataActivity.class);
        i.putExtra(EXTRA_USER_NAME, userName);
        return i;
    }

    private void setListener() {
        MyListener myListener=new MyListener();
        head.setOnClickListener(myListener);
        nickname.setOnClickListener(myListener);
        password.setOnClickListener(myListener);
        sex.setOnClickListener(myListener);
    }

    private void findView() {
        head=(RelativeLayout)findViewById(R.id.rl_set_head);
        nickname=(RelativeLayout)findViewById(R.id.rl_set_nickname);
        password=(RelativeLayout)findViewById(R.id.rl_set_password);
        sex=(RelativeLayout)findViewById(R.id.rl_set_sex);
        imageView=(ImageView)findViewById(R.id.iv_set_img);
        usersex=(TextView)findViewById(R.id.tv_set_sex);
        name=(TextView)findViewById(R.id.tv_set_nickname);
        editName=(EditText)findViewById(R.id.et_namedialog_edit);
        sexs[0]="男";
        sexs[1]="女";
    }

    private class MyListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.rl_set_head:
                    new AlertDialog.Builder(PersonalDataActivity.this).
                            setTitle("上传照片").
                            setItems(new String[]{"拍照上传","本地上传"},new DialogInterface.OnClickListener()
                            {

                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    // TODO Auto-generated method stub
                                    switch (which)
                                    {
                                        case 0:
                                            Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                            startActivityForResult(cameraIntent, 1001);
                                            break;
                                        case 1:
                                            Intent intent = new Intent(Intent.ACTION_PICK, null);
                                            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                                            startActivityForResult(intent, 1000);
                                            break;
                                        default:

                                            break;
                                    }
                                }
                            }).show();
                    break;
                case R.id.rl_set_nickname:
                    LinearLayout nameDialog=(LinearLayout)getLayoutInflater().inflate(R.layout.zhu_activity_namedialog,null);
                    final EditText e = new EditText(PersonalDataActivity.this);
                    new AlertDialog.Builder(PersonalDataActivity.this)
                            .setTitle("昵称")
                            .setView(e)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    name.setText(e.getText().toString());
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .create()
                            .show();
                    break;
                case R.id.rl_set_password:

                    break;
                case R.id.rl_set_sex:
                    new AlertDialog.Builder(PersonalDataActivity.this).
                            setTitle("性别").
                            setSingleChoiceItems(sexs, 0, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    usersex.setText(sexs[i]);
                                }
                            }).show();
                    break;
            }
        }
    }

    private void startCrop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");//调用Android系统自带的一个图片剪裁页面,
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");//进行修剪
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 500);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 1002);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // TODO Auto-generated method stub
        switch (requestCode)
        {
            case 1000:
                if (data == null) {
                    return;
                }
                startCrop(data.getData());
                break;
            case 1002:
                if (bitMap != null && !bitMap.isRecycled()) {
                    bitMap.recycle();
                }
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.PNG, 75, stream);//(0-100)压缩文件
                    //此处可以把Bitmap保存到sd卡中，具体请看：http://www.cnblogs.com/linjiqin/archive/2011/12/28/2304940.html
                    imageView.setImageBitmap(photo); //把图片显示在ImageView控件上
                }
                break;
            case 1001:
                Bundle bundle = data.getExtras();
                bitMap = (Bitmap)bundle.get("data");
                if(bitMap!=null)
                    bitMap.recycle();
                bitMap = (Bitmap) data.getExtras().get("data");
                File file2=new File(pathString, System.currentTimeMillis()+".jpg");
                try
                {
                    FileOutputStream fos = new FileOutputStream(file2);
                    byte[] buffer = new byte[1024];
                    bitMap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.write(buffer);
                    fos.flush();
                    fos.close();
                } catch (FileNotFoundException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                imageView.setImageBitmap(bitMap);
                hasImage=true;
                break;

            default:
                break;
        }
//    	super.onActivityResult(requestCode, resultCode, data);
    }
}
