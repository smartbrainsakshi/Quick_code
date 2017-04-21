package com.vrs.circular_ripple_lollipop;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Toolbar toolbar;
    boolean hidden=true;
CollapsingToolbarLayout col;
    LinearLayout mRevealView;
    ImageButton ib_gallery,ib_contacts,ib_location;
    ImageButton ib_video,ib_audio,ib_camera;
    private String strBase64;
    ArrayList<DataModel> dataModels;
    ListView listView;
    TextView tv;
   // Imageutils imageutils;
    private static CustomAdapter adapter;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
     //   boolean result=Utility.checkPermission(MainActivity.this);
tv=(TextView)findViewById(R.id.textView);
        tv.setVisibility(tv.INVISIBLE);
col=(CollapsingToolbarLayout)findViewById(R.id.collapsing_tool_bar_layout);
        col.isTitleEnabled();
        col.setTitle("Your Requirement");

        // setCollapsingToolbarLayoutTitle("Your Requirement");
        listView=(ListView)findViewById(R.id.listView);
       // toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar_1);
        setSupportActionBar(toolbar);
        col.setTitleEnabled(false);
        toolbar.setTitle("Your Requirement");
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//toolbar.setTitleTextColor(Integer.parseInt("#fff"));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_keyboard_arrow_left_black_24dp, null));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Your Requirement");
     //   imageutils =new Imageutils(this);


        mRevealView = (LinearLayout) findViewById(R.id.reveal_items);
        ib_audio=(ImageButton)findViewById(R.id.audio);

        ib_gallery=(ImageButton)findViewById(R.id.gallery);
    //    ib_location=(ImageButton)findViewById(R.id.location);
        ib_video=(ImageButton)findViewById(R.id.video);

        dataModels= new ArrayList<>();

        ib_audio.setOnClickListener(this);

        ib_gallery.setOnClickListener(this);
     //   ib_location.setOnClickListener(this);
        ib_video.setOnClickListener(this);

        setSupportActionBar(toolbar);
        mRevealView.setVisibility(View.INVISIBLE);

    }



    // imagebutton click events

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.audio:
               // Snackbar.make(v, "Audio Clicked", Snackbar.LENGTH_SHORT).show();
                mRevealView.setVisibility(View.INVISIBLE);
                hidden=true;
               cameraIntent();
                break;


            case R.id.video:
               // Snackbar.make(v, "Video Clicked", Snackbar.LENGTH_SHORT).show();
                mRevealView.setVisibility(View.INVISIBLE);
                hidden=true;
                galleryIntent();
                break;

            case R.id.gallery:
               // Snackbar.make(v, "Gallery Clicked", Snackbar.LENGTH_SHORT).show();
                mRevealView.setVisibility(View.INVISIBLE);


               hidden=true;
                galleryIntent();
                break;

        }

    }

    private void setCollapsingToolbarLayoutTitle(String title) {
        col.setTitle(title);
        col.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        col.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
      col.setExpandedTitleTextAppearance(R.style.ExpandedAppBarPlus1);
      col.setCollapsedTitleTextAppearance(R.style.CollapsedAppBarPlus1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        // attachment icon click event
        else if(id== R.id.action_attachment)
        {


            // finding X and Y co-ordinates
            int cx = (mRevealView.getLeft() + mRevealView.getRight());
            int cy = (mRevealView.getTop());

            // to find  radius when icon is tapped for showing layout
            int startradius=0;
            int endradius = Math.max(mRevealView.getWidth(), mRevealView.getHeight());


            // performing circular reveal when icon will be tapped
            Animator animator = ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, startradius, endradius);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(400);

            //reverse animation
            // to find radius when icon is tapped again for hiding layout


            //  starting radius will be the radius or the extent to which circular reveal animation is to be shown
            int reverse_startradius = Math.max(mRevealView.getWidth(),mRevealView.getHeight());
            //endradius will be zero
            int reverse_endradius=0;


            // performing circular reveal for reverse animation
            Animator animate = ViewAnimationUtils.createCircularReveal(mRevealView,cx,cy,reverse_startradius,reverse_endradius);


            if(hidden) {

                // to show the layout when icon is tapped
                mRevealView.setVisibility(View.VISIBLE);
                animator.start();
                hidden = false;
            }
            else {

                mRevealView.setVisibility(View.VISIBLE);

                // to hide layout on animation end
                animate.addListener(new AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mRevealView.setVisibility(View.INVISIBLE);
                        hidden = true;

                    }
                });
                animate.start();

            }



            return true;

        }

        return super.onOptionsItemSelected(item);
    }
    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }
    private void videoIntent()
    {
      galleryIntent();    }
    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/* video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        final Uri selectedImageUri = data.getData();
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = managedQuery(selectedImageUri, projection, null, null,
                null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        String selectedImagePath = cursor.getString(column_index);
        Bitmap bitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        File file = new File(String.valueOf(selectedImageUri));
        // File file1=new File(selectedImageUri);

        String imageName = file.getName();
        long fileSizeInBytes = file.length();
        float fileSizeInKB = fileSizeInBytes / 1024;
        // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
        float fileSizeInMB = fileSizeInKB / 1024;

        String calString=Float.toString(fileSizeInMB);
        Log.e("size",calString);
        Log.e("name",imageName);
        //calString="24kb";
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        dataModels.add(new DataModel(imageName,calString));
        adapter= new CustomAdapter(dataModels,getApplicationContext());
        tv.setVisibility(tv.VISIBLE);
        listView.setAdapter(adapter);
      //  ivImage.setImageBitmap(bm);
    }
    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
String name=destination.getName();
        long fileSizeInBytes = destination.length();
        float fileSizeInKB = fileSizeInBytes / 1024;
        // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
        float fileSizeInMB = fileSizeInKB / 1024;

        String calString=Float.toString(fileSizeInMB);

        Log.e("name",name);
        Log.e("namesize",calString);
       // calString="24kb";
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        dataModels.add(new DataModel(name,calString));
        adapter= new CustomAdapter(dataModels,getApplicationContext());
        tv.setVisibility(tv.VISIBLE);
        listView.setAdapter(adapter);
        //ivImage.setImageBitmap(thumbnail);
    }






}