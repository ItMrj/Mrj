package com.example.a49829.easydemo;

import android.app.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {


    private ImageView imageView;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageView4;
    public static boolean isForeground = false;
    public static final String MESSAGE_RECEIVED_ACTION = "RESUME";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    private List<LocalMedia> selectList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.iv1);
        imageView2 = findViewById(R.id.iv2);
        imageView3 = findViewById(R.id.iv3);
        imageView4 = findViewById(R.id.iv4);


//        GlideUtils.loadCirleAvatar(this, R.mipmap.ic_launcher, imageView);
        Glide.with(MainActivity.this)
                .asBitmap()
                .load(R.mipmap.ic_launcher)
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(MainActivity.this.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PictureSelector.create(MainActivity.this).openGallery(PictureMimeType.ofImage())
                        .compress(true)
                        .isCamera(true)
                        .imageFormat(PictureMimeType.PNG)
                        .maxSelectNum(1)// 最小选择数量 int
                        .enableCrop(true)// 是否裁剪 true or false
                        .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
//                        .compressMode(PictureConfig.LUBAN_COMPRESS_MODE)
                        .selectionMode(PictureConfig.SINGLE)
                        .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                        .circleDimmedLayer(false)// 是否圆形裁剪 true or false
//                        .compressMaxKB(100)
                        .minimumCompressSize(100)
                        .rotateEnabled(true) // 裁剪是否可旋转图片 true or false
                        .scaleEnabled(true) // 裁剪是否可放大缩小图片 true or false
                        .forResult(PictureConfig.CHOOSE_REQUEST);
            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (WindowsUitlity.isShown) {
                    WindowsUitlity.hidePopupWindow();
                } else {
                    WindowsUitlity.showPopupWindow(MainActivity.this, "体验金，24小时内有效！");
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (WindowsUitlity.isShown) {
                            WindowsUitlity.hidePopupWindow();
                        }
                    }
                }, 2000);
            }
        });

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (WindowsUitlity.isShown) {
                    WindowsUitlity.hidePopupWindow();
                } else {
                    WindowsUitlity.showPopupWindow(MainActivity.this, "体验金，24小时内有效！");
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PictureConfig.CHOOSE_REQUEST:
                // 图片选择结果回调
                selectList = PictureSelector.obtainMultipleResult(data);
                if (selectList != null && selectList.size() > 0) {
//                    showProgressDialog("正在上传中..");
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    LocalMedia media = selectList.get(0);
                    File file;
                    String path = "";
                    if (media.isCut() && !media.isCompressed()) {
                        // 裁剪过
                        path = media.getCutPath();
                        file = new File(path);
                    } else if (media.isCompressed() || (media.isCut() && media.isCompressed())) {
                        // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                        path = media.getCompressPath();
                        file = new File(path);
                    } else {
                        // 原图
                        path = media.getPath();
                        file = new File(path);
                    }
//                    GlideUtils.loadCirleAvatar(MainActivity.this, file, imageView2);
//                    Glide.with(MainActivity.this).asBitmap().load(file).into(imageView2);
                    Log.d("marj", "onActivityResult: " + file.getAbsolutePath());
                    Log.d("marj", "onActivityResult: " + file.getName());
                    Glide.get(this).setMemoryCategory(MemoryCategory.HIGH);
                    Glide.with(MainActivity.this)
                            .asBitmap()
                            .load(file)
                            .into(new BitmapImageViewTarget(imageView2) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(MainActivity.this.getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    imageView2.setImageDrawable(circularBitmapDrawable);
                                }
                            });
//                    upLoadImageModel = new UpLoadImagePresenter(AccountManagerActivity.this, mContext);
//                    upLoadImageModel.upLoadImage(sesionid, file);
                } else {
//                    showToast("图片为空");
                }
                break;
        }
    }
}