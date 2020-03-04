package com.yagi.android.taskalarm;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.yagi.android.taskalarm.Adapter.SmallTaskAdapter;
import com.yagi.android.taskalarm.Dialog.DatePickerDialogFragment2;
import com.yagi.android.taskalarm.Dialog.TimePickerDialogFragment2;
import com.yagi.android.taskalarm.Model.SmallTask;
import com.yagi.android.taskalarm.Model.Task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;

import static android.graphics.Color.rgb;
import static com.yagi.android.taskalarm.TaskActivity.idDate;

public class SmallTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    public Realm realm;

    private Toolbar toolbar;

    private EditText titleEditText;
    private LinearLayout deadlineLayout;
    private LinearLayout passLayout;
    private LinearLayout safeLayout;
    private TextView deadlineTextView;
    private TextView passTextView;
    private TextView safeTextView;
    private TextView deadlineHourTextView;
    private TextView passHourTextView;
    private TextView safeHourTextView;

    private String id;
    private String title;

    private int date;
    private int time;
    private int newJudge;
    public static int reuse;
    private int change;

    private SmallTask smallTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_small_task);

        realm = Realm.getDefaultInstance();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        titleEditText = (EditText) findViewById(R.id.title);
        deadlineLayout = (LinearLayout) findViewById(R.id.layout_deadline);
        passLayout = (LinearLayout) findViewById(R.id.layout_pass);
        safeLayout = (LinearLayout) findViewById(R.id.layout_safe);
        deadlineTextView = (TextView) findViewById(R.id.time_deadline_ymd);
        passTextView = (TextView) findViewById(R.id.time_pass_ymd);
        safeTextView = (TextView) findViewById(R.id.time_safe_ymd);
        deadlineHourTextView = (TextView) findViewById(R.id.time_deadline_hm);
        passHourTextView = (TextView) findViewById(R.id.time_pass_hm);
        safeHourTextView = (TextView) findViewById(R.id.time_safe_hm);

        Intent i = getIntent();
        id = i.getStringExtra("idDate");
        newJudge = i.getIntExtra("new",0);
        reuse = i.getIntExtra("reuse",0);
        change = 0;
        title = "";

        //Log.d("judge",String.valueOf(newJudge));

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        final TypedArray styledAttributes = getTheme().obtainStyledAttributes(new int[] { android.R.attr.actionBarSize });
        int actionBarSize = (int) styledAttributes.getDimension(0, 0);
        int fitSize = (int)(actionBarSize * 0.5);
        styledAttributes.recycle();
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.back, null);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        Drawable newdrawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, fitSize, fitSize, true));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setHomeAsUpIndicator(newdrawable);
        actionBar.setTitle(Html.fromHtml("<font color='#FFFFFF'>小タスクを追加</font>"));

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if (newJudge != 1) {
            showData();
            title = smallTask.title;
            setBackground(smallTask.deadline, smallTask.passline, smallTask.safeline);
        }



    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        String str = String.format(Locale.US, "%d年%d月%d日",year, monthOfYear+1, dayOfMonth);

        if (date == 1){
            deadlineTextView.setText(str);
        } else if (date == 2){
            passTextView.setText(str);
        } else {
            safeTextView.setText(str);
        }

        if (deadlineTextView.getText().toString().equals("日付の設定") || passTextView.getText().toString().equals("日付の設定") || safeTextView.getText().toString().equals("日付の設定")
                || deadlineHourTextView.getText().toString().equals("時刻の設定") || passHourTextView.getText().toString().equals("時刻の設定") || safeHourTextView.getText().toString().equals("時刻の設定")) {

        } else {
            setBackground(deadlineTextView.getText().toString() + deadlineHourTextView.getText().toString(),
                    passTextView.getText().toString() + passHourTextView.getText().toString(),
                    safeTextView.getText().toString() + safeHourTextView.getText().toString());

        }


        change = 1;

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        String str = String.format(Locale.US, "%d:%02d", hourOfDay, minute);

        if (time == 1){
            deadlineHourTextView.setText(str);
        } else if (time == 2){
            passHourTextView.setText(str);
        } else {
            safeHourTextView.setText(str);
        }

        if (deadlineTextView.getText().toString().equals("日付の設定") || passTextView.getText().toString().equals("日付の設定") || safeTextView.getText().toString().equals("日付の設定")
                || deadlineHourTextView.getText().toString().equals("時刻の設定") || passHourTextView.getText().toString().equals("時刻の設定") || safeHourTextView.getText().toString().equals("時刻の設定")) {

        } else {
            setBackground(deadlineTextView.getText().toString() + deadlineHourTextView.getText().toString(),
                    passTextView.getText().toString() + passHourTextView.getText().toString(),
                    safeTextView.getText().toString() + safeHourTextView.getText().toString());

        }

        change = 1;

    }

    public void deadline (View v){
        DatePickerDialogFragment2 datePicker = new DatePickerDialogFragment2();
        datePicker.show(getSupportFragmentManager(), "datePicker");
        date = 1;
    }

    public void deadlineh (View v){
        TimePickerDialogFragment2 timePicker = new TimePickerDialogFragment2();
        timePicker.show(getSupportFragmentManager(), "timePicker");
        time = 1;
    }

    public void pass (View v){
        DatePickerDialogFragment2 datePicker = new DatePickerDialogFragment2();
        datePicker.show(getSupportFragmentManager(), "datePicker");
        date = 2;
    }

    public void passh (View v){
        TimePickerDialogFragment2 timePicker = new TimePickerDialogFragment2();
        timePicker.show(getSupportFragmentManager(), "timePicker");
        time = 2;
    }

    public void safe (View v){
        DatePickerDialogFragment2 datePicker = new DatePickerDialogFragment2();
        datePicker.show(getSupportFragmentManager(), "datePicker");
        date = 3;
    }

    public void safeh (View v){
        TimePickerDialogFragment2 timePicker = new TimePickerDialogFragment2();
        timePicker.show(getSupportFragmentManager(), "timePicker");
        time = 3;
    }

    //データをRealmに保存
    public void save(final String title, final String deadline, final String passLine, final String safeLine, final String updateDate, final String id, final String achieve) {

        //メモを保存する
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                SmallTask smallTask = realm.createObject(SmallTask.class);
                smallTask.title = title;
                smallTask.deadline = deadline;
                smallTask.passline = passLine;
                smallTask.safeline = safeLine;
                smallTask.updateDate = updateDate;
                smallTask.id = id;
                smallTask.achieve = achieve;
            }

        });
    }

    public void showData() {

        smallTask = realm.where(SmallTask.class).equalTo("updateDate", getIntent().getStringExtra("updateDate")).findFirst();

        int cala = smallTask.deadline.indexOf("日");
        int calb = smallTask.passline.indexOf("日");
        int calc = smallTask.safeline.indexOf("日");

        titleEditText.setText(smallTask.title);
        deadlineTextView.setText(smallTask.deadline.substring(0,cala+1));
        deadlineHourTextView.setText(smallTask.deadline.substring(cala+1));
        passTextView.setText(smallTask.passline.substring(0,calb+1));
        passHourTextView.setText(smallTask.passline.substring(calb+1));
        safeTextView.setText(smallTask.safeline.substring(0,calc+1));
        safeHourTextView.setText(smallTask.safeline.substring(calc+1));

    }

    public void extraction (String line, Calendar calendar){
        int cala = line.indexOf("年");
        int calb = line.indexOf("月");
        int calc = line.indexOf("日");
        int cald = line.indexOf(":");
        calendar.set(Integer.parseInt(line.substring(0,cala)), Integer.parseInt(line.substring(cala+1,calb)) - 1, Integer.parseInt(line.substring(calb+1,calc)),Integer.parseInt(line.substring(calc+1,cald)), Integer.parseInt(line.substring(cald+1)),00);
    }

    public void setBackground(String deadline, String passline, String safeline){
        Calendar cal2 = Calendar.getInstance();
        cal2.getTime();

        Calendar cal3 = Calendar.getInstance();
//        Log.d("deadline", smallTask.deadline);
        extraction (deadline, cal3);

        Calendar cal4 = Calendar.getInstance();
        extraction(passline, cal4);

        Calendar cal5 = Calendar.getInstance();
        extraction(safeline, cal5);

        // 2つの日時を比較し、結果によってメッセージを変えます
        int diff = cal2.compareTo(cal3);
        int diff2 = cal2.compareTo(cal4);
        int diff3 = cal2.compareTo(cal5);

        if (newJudge == 1){
            if (diff > 0) {
                deadlineLayout.setBackgroundColor(rgb(255, 192, 203));
//            passLayout.setBackgroundColor(rgb(255, 192, 203));
//            safeLayout.setBackgroundColor(rgb(255, 192, 203));
            } else {
                deadlineLayout.setBackgroundColor(rgb(255, 255, 255));
            }

            if (diff2 > 0) {
                passLayout.setBackgroundColor(rgb(255, 192, 203));
//            safeLayout.setBackgroundColor(rgb(255, 192, 203));
            } else {
                passLayout.setBackgroundColor(rgb(255, 255, 255));
            }

            if (diff3 > 0) {
                safeLayout.setBackgroundColor(rgb(255, 192, 203));
            } else {
                safeLayout.setBackgroundColor(rgb(255, 255, 255));
            }
        } else {
            if (diff > 0 && smallTask.achieve.equals(String.valueOf(0))) {
                deadlineLayout.setBackgroundColor(rgb(255, 192, 203));
//            passLayout.setBackgroundColor(rgb(255, 192, 203));
//            safeLayout.setBackgroundColor(rgb(255, 192, 203));
            } else {
                deadlineLayout.setBackgroundColor(rgb(255, 255, 255));
            }

            if (diff2 > 0 && smallTask.achieve.equals(String.valueOf(0))) {
                passLayout.setBackgroundColor(rgb(255, 192, 203));
//            safeLayout.setBackgroundColor(rgb(255, 192, 203));
            } else {
                passLayout.setBackgroundColor(rgb(255, 255, 255));
            }

            if (diff3 > 0 && smallTask.achieve.equals(String.valueOf(0))) {
                safeLayout.setBackgroundColor(rgb(255, 192, 203));
            } else {
                safeLayout.setBackgroundColor(rgb(255, 255, 255));
            }
        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_small_task, menu);
        if (newJudge == 1) {
            MenuItem menuItem1 = menu.findItem(R.id.delete);
            menuItem1.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (titleEditText.getText().toString().equals(title)){

                } else {
                    change = 1;
//                    Log.d("change",title);
//                    Log.d("edit", titleEditText.getText().toString());
                }
                if (change == 1) {
                    AlertDialog.Builder alertDlg1 = new AlertDialog.Builder(this);
                    alertDlg1.setTitle("確認");
                    alertDlg1.setMessage("編集内容を破棄します。よろしいですか？");
                    alertDlg1.setPositiveButton(
                            "破棄する",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // OK ボタンクリック処理
                                    finish();
                                }
                            });
                    alertDlg1.setNegativeButton(
                            "キャンセル",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Cancel ボタンクリック処理
                                }
                            });

                    // 表示
                    alertDlg1.create().show();
                } else {
                    finish();
                }

                break;
            case R.id.save /*2131427478*/:

                if (deadlineTextView.getText().toString().equals("日付の設定") || passTextView.getText().toString().equals("日付の設定") || safeTextView.getText().toString().equals("日付の設定")
                        || deadlineHourTextView.getText().toString().equals("時刻の設定") || passHourTextView.getText().toString().equals("時刻の設定") || safeHourTextView.getText().toString().equals("時刻の設定")){

                    AlertDialog.Builder alertDlg1 = new AlertDialog.Builder(this);
                    alertDlg1.setTitle("日時と時刻がセットされていません。");
                    alertDlg1.setMessage("全ての日時と時刻を入力してください。");
                    alertDlg1.setPositiveButton(
                            "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // OK ボタンクリック処理

                                }
                            });

                    // 表示
                    alertDlg1.create().show();

                } else {

                    if (newJudge == 1) {
                        //タイトル取得
                        String title = titleEditText.getText().toString();

                        //日付取得
                        Date date = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.JAPANESE);
                        String updateDate = sdf.format(date);

                        //内容取得
                        String deadline = deadlineTextView.getText().toString() + deadlineHourTextView.getText().toString();
                        String passLine = passTextView.getText().toString() + passHourTextView.getText().toString();
                        String safeLine = safeTextView.getText().toString() + safeHourTextView.getText().toString();

                        String achieve = String.valueOf(0);

//                Log.d("id",id);

                        //出力
                        //check(title,updateDate,content);

                        //保存する
                        save(title, deadline, passLine, safeLine, updateDate, id, achieve);

                    } else {
                        smallTask = realm.where(SmallTask.class).equalTo("updateDate", getIntent().getStringExtra("updateDate")).findFirst();

                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                smallTask.title = titleEditText.getText().toString();
                                smallTask.deadline = deadlineTextView.getText().toString() + deadlineHourTextView.getText().toString();
                                smallTask.passline = passTextView.getText().toString() + passHourTextView.getText().toString();
                                smallTask.safeline = safeTextView.getText().toString() + safeHourTextView.getText().toString();
                            }
                        });
                    }

                    TaskActivity.change = 1;
                    TaskActivity.smallTaskSave = 1;

                    Toast.makeText(SmallTaskActivity.this, "保存しました", Toast.LENGTH_SHORT).show();

                    finish();

                }
                break;

            case R.id.delete:
                if (newJudge != 1){
                    AlertDialog.Builder alertDlg = new AlertDialog.Builder(this);
                    alertDlg.setTitle("本当に削除しますか？");
                    alertDlg.setMessage("一度削除すると元には戻りません");
                    alertDlg.setPositiveButton(
                            "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // OK ボタンクリック処理
                                    realm.executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            smallTask = realm.where(SmallTask.class).equalTo("updateDate", getIntent().getStringExtra("updateDate")).findFirst();
                                            smallTask.deleteFromRealm();
                                        }
                                    });
                                    Toast.makeText(SmallTaskActivity.this,"削除しました",Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                    alertDlg.setNegativeButton(
                            "Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Cancel ボタンクリック処理
                                }
                            });

                    // 表示
                    alertDlg.create().show();

                }
                break;

        }
        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            // 戻るボタンの処理
            if (titleEditText.getText().toString().equals(title)){

            } else {
                change = 1;
//                Log.d("change",title);
//                Log.d("edit", titleEditText.getText().toString());
            }
            if (change == 1) {
                AlertDialog.Builder alertDlg1 = new AlertDialog.Builder(this);
                alertDlg1.setTitle("確認");
                alertDlg1.setMessage("編集内容を破棄します。よろしいですか？");
                alertDlg1.setPositiveButton(
                        "破棄する",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // OK ボタンクリック処理
                                finish();
                            }
                        });
                alertDlg1.setNegativeButton(
                        "キャンセル",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Cancel ボタンクリック処理
                            }
                        });

                // 表示
                alertDlg1.create().show();
            } else {
                finish();
            }
            return super.onKeyDown(keyCode, event);
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

}