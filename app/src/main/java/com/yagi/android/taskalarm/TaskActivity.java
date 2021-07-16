package com.yagi.android.taskalarm;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.yagi.android.taskalarm.Adapter.SmallTaskAdapter;
import com.yagi.android.taskalarm.Dialog.DatePickerDialogFragment;
import com.yagi.android.taskalarm.Dialog.TimePickerDialogFragment;
import com.yagi.android.taskalarm.Model.SmallTask;
import com.yagi.android.taskalarm.Model.Task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

import static android.graphics.Color.rgb;

public class TaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    public Realm realm;

    private SmallTaskAdapter smallTaskAdapter;

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
    public static ListView detailListView;

    private int date;
    private int time;
    private int newJudge;
    public static int reuse;
    public static int change;
    public static int smallTaskSave;

//    public String id = "1";
    public static String idDate;
    private String title;

    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        realm = Realm.getDefaultInstance();

        //setTitle( "タスクを作る" );

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        TextView emptyTextView = (TextView) findViewById(R.id.emptyTextView);
        detailListView = (ListView) findViewById(R.id.small_task_list);

        Intent i = getIntent();
        title = "";//= i.getStringExtra("title");
        newJudge = i.getIntExtra("new",0);
        reuse = i.getIntExtra("reuse",0);
        //titleEditText.setText(title);
        change = 0;
        smallTaskSave = 0;

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        final TypedArray styledAttributes = getTheme().obtainStyledAttributes(new int[] { android.R.attr.actionBarSize });
        int actionBarSize = (int) styledAttributes.getDimension(0, 0);
        int fitSize = (int)(actionBarSize * 0.5);
        styledAttributes.recycle();
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.back, null);
        assert drawable != null;
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        Drawable newdrawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, fitSize, fitSize, true));
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setHomeAsUpIndicator(newdrawable);
        actionBar.setTitle(Html.fromHtml("<font color='#FFFFFF'>タスクを作る</font>"));

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        if (newJudge == 1){
            idDate = i.getStringExtra("idDate");
        } else {
            showData();
            title = task.title;
            setBackground(task.deadline, task.passline, task.safeline);
        }

        detailListView.setAdapter(smallTaskAdapter);

        detailListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                SmallTask smallTask = (SmallTask) adapterView.getItemAtPosition(position);
                Intent intent = new Intent(TaskActivity.this, SmallTaskActivity.class);
                intent.putExtra("idDate", idDate);
                intent.putExtra("updateDate", smallTask.updateDate);
                startActivity(intent);
            }
        });


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

    @Override
    protected void onResume() {
        super.onResume();

        setMemoList();
    }

    public void deadline (View v){
        DatePickerDialogFragment datePicker = new DatePickerDialogFragment();
        datePicker.show(getSupportFragmentManager(), "datePicker");
        date = 1;
    }

    public void deadlineh (View v){
        TimePickerDialogFragment timePicker = new TimePickerDialogFragment();
        timePicker.show(getSupportFragmentManager(), "timePicker");
        time = 1;
    }

    public void pass (View v){
        DatePickerDialogFragment datePicker = new DatePickerDialogFragment();
        datePicker.show(getSupportFragmentManager(), "datePicker");
        date = 2;
    }

    public void passh (View v){
        TimePickerDialogFragment timePicker = new TimePickerDialogFragment();
        timePicker.show(getSupportFragmentManager(), "timePicker");
        time = 2;
    }

    public void safe (View v){
        DatePickerDialogFragment datePicker = new DatePickerDialogFragment();
        datePicker.show(getSupportFragmentManager(), "datePicker");
        date = 3;
    }

    public void safeh (View v){
        TimePickerDialogFragment timePicker = new TimePickerDialogFragment();
        timePicker.show(getSupportFragmentManager(), "timePicker");
        time = 3;
    }

    public void create (View v){
        Intent i = new Intent(this,SmallTaskActivity.class);
        i.putExtra("idDate",idDate);
        i.putExtra("new",1);
        //Log.d("id1",id);
        startActivity(i);
    }

    //データをRealmに保存
    public void save(final String title, final String deadline, final String passLine, final String safeLine, final String updateDate, final String id, final String achieve ,final RealmList items) {

        //SmallTask SmallTask = realm.createObject(SmallTask.class);
        //final RealmResults<SmallTask> results = realm.where(SmallTask.class).equalTo("id",idDate).findAll();

        //メモを保存する
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                Task Task = realm.createObject(Task.class);
                Task.title = title;
                Task.deadline = deadline;
                Task.passline = passLine;
                Task.safeline = safeLine;
                Task.updateDate = updateDate;
                Task.id = id;
                Task.achieve = achieve;

                //SmallTask SmallTask = realm.createObject(SmallTask.class);
                Task.SmallTasks = items;

            }

        });
    }

    public void setMemoList() {

        //realmから読み取る
        RealmResults<SmallTask> results = realm.where(SmallTask.class).equalTo("id",idDate).findAll();
        results = results.sort("updateDate");

        //RealmResults<SmallTask> results = realm.where(SmallTask.class).findAll();
        List<SmallTask> items = realm.copyFromRealm(results);

//        if (newJudge !=)
//
//        RealmList<SmallTask> items1 = task.SmallTasks;
        Log.d("results",String.valueOf(results));

        SmallTaskAdapter adapter = new SmallTaskAdapter(this, R.layout.layout_task, items);

        detailListView.setAdapter(adapter);



    }

    public void showData() {

        task = realm.where(Task.class).equalTo("updateDate", getIntent().getStringExtra("updateDate")).findFirst();

        int cala = task.deadline.indexOf("日");
        int calb = task.passline.indexOf("日");
        int calc = task.safeline.indexOf("日");

        titleEditText.setText(task.title);
        deadlineTextView.setText(task.deadline.substring(0,cala+1));
        deadlineHourTextView.setText(task.deadline.substring(cala+1));
        passTextView.setText(task.passline.substring(0,calb+1));
        passHourTextView.setText(task.passline.substring(calb+1));
        safeTextView.setText(task.safeline.substring(0,calc+1));
        safeHourTextView.setText(task.safeline.substring(calc+1));

        Log.d("Task", String.valueOf(task.SmallTasks));

        idDate = task.id;

    }

    public void extraction (String line, Calendar calendar){
        int cala = line.indexOf("年");
        int calb = line.indexOf("月");
        int calc = line.indexOf("日");
        int cald = line.indexOf(":");
//        Log.d("年", "[" + line.substring(0,cala) + "]");
//        Log.d("月", "[" + line.substring(cala+1,calb) + "]");
//        Log.d("日", "[" + line.substring(calb+1,calc) + "]");
//        Log.d("時", "[" + line.substring(calc+1,cald) + "]");
//        Log.d("分", "[" + line.substring(cald+1) + "]");
        calendar.set(Integer.parseInt(line.substring(0,cala)), Integer.parseInt(line.substring(cala+1,calb)) - 1, Integer.parseInt(line.substring(calb+1,calc)),Integer.parseInt(line.substring(calc+1,cald)), Integer.parseInt(line.substring(cald+1)),00);
    }

    public void setBackground(String deadline, String passline, String safeline) {
        Calendar cal2 = Calendar.getInstance();
        cal2.getTime();

        Calendar cal3 = Calendar.getInstance();
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
            if (diff > 0 && task.achieve.equals(String.valueOf(0))) {
                deadlineLayout.setBackgroundColor(rgb(255, 192, 203));
//            passLayout.setBackgroundColor(rgb(255, 192, 203));
//            safeLayout.setBackgroundColor(rgb(255, 192, 203));
            } else {
                deadlineLayout.setBackgroundColor(rgb(255, 255, 255));
            }

            if (diff2 > 0 && task.achieve.equals(String.valueOf(0))) {
                passLayout.setBackgroundColor(rgb(255, 192, 203));
//            safeLayout.setBackgroundColor(rgb(255, 192, 203));
            } else {
                passLayout.setBackgroundColor(rgb(255, 255, 255));
            }

            if (diff3 > 0 && task.achieve.equals(String.valueOf(0))) {
                safeLayout.setBackgroundColor(rgb(255, 192, 203));
            } else {
                safeLayout.setBackgroundColor(rgb(255, 255, 255));
            }
        }
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_task, menu);
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
                    //Log.d("change",title);
                    //Log.d("edit", titleEditText.getText().toString());
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
                                    if (newJudge == 1) {
                                        realm.executeTransaction(new Realm.Transaction() {
                                            @Override
                                            public void execute(Realm realm) {
                                                RealmResults<SmallTask> results = realm.where(SmallTask.class).equalTo("id", idDate).findAll();
                                                results.deleteAllFromRealm();
                                            }
                                        });
                                        Toast.makeText(TaskActivity.this, "変更を破棄しました", Toast.LENGTH_SHORT).show();
                                    } else if (smallTaskSave == 1){
                                        Toast.makeText(TaskActivity.this,"変更を破棄しました(保存した詳細タスクはそのままです)",Toast.LENGTH_SHORT).show();
//                                        realm.executeTransaction(new Realm.Transaction() {
//                                            @Override
//                                            public void execute(Realm realm) {
//                                                task = realm.where(Task.class).equalTo("updateDate", getIntent().getStringExtra("updateDate")).findFirst();
//                                                RealmResults<SmallTask> results = realm.where(SmallTask.class).equalTo("id", task.id).findAll();
//                                                results.deleteAllFromRealm();
//                                                task.deleteFromRealm();
//                                            }
//                                        });
                                    } else {
                                        Toast.makeText(TaskActivity.this, "変更を破棄しました", Toast.LENGTH_SHORT).show();
                                    }
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

                } else if (reuse == 1){

                    AlertDialog.Builder alertDlg1 = new AlertDialog.Builder(this);
                    alertDlg1.setTitle("確認");
                    alertDlg1.setMessage("タスクを保存して再利用します。よろしいですか？");
                    alertDlg1.setPositiveButton(
                            "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // OK ボタンクリック処理
                                    final Task task = realm.where(Task.class).equalTo("updateDate", getIntent().getStringExtra("updateDate")).findFirst();

                                    RealmResults<SmallTask> results = realm.where(SmallTask.class).equalTo("id",task.id).findAll();
                                    final RealmList<SmallTask> items = new RealmList<>();
                                    items.addAll(results);

                                    realm.executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            task.title = titleEditText.getText().toString();
                                            task.deadline = deadlineTextView.getText().toString() + deadlineHourTextView.getText().toString();
                                            task.passline = passTextView.getText().toString() + passHourTextView.getText().toString();
                                            task.safeline = safeTextView.getText().toString() + safeHourTextView.getText().toString();
                                            task.achieve = String.valueOf(0);
                                            task.SmallTasks = items;
                                        }
                                    });

                                    Toast.makeText(TaskActivity.this, "保存しました", Toast.LENGTH_SHORT).show();

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

                        RealmResults<SmallTask> results = realm.where(SmallTask.class).equalTo("id",idDate).findAll();
                        //RealmResults<SmallTask> results = realm.where(SmallTask.class).findAll();
                        RealmList<SmallTask> items = new RealmList<>();
                        items.addAll(results);
                                //= realm.copyFromRealm(results);


                        //final RealmList<SmallTask> items = realm.where(SmallTask.class).equalTo("id",idDate).findAll();

                        //出力
                        //check(title,updateDate,content);

                        //保存する
                        save(title, deadline, passLine, safeLine, updateDate, idDate, achieve, items);
                    } else {
                        final Task task = realm.where(Task.class).equalTo("updateDate", getIntent().getStringExtra("updateDate")).findFirst();


                        RealmResults<SmallTask> results = realm.where(SmallTask.class).equalTo("id",task.id).findAll();
                        final RealmList<SmallTask> items = new RealmList<>();
                        items.addAll(results);

                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                task.title = titleEditText.getText().toString();
                                task.deadline = deadlineTextView.getText().toString() + deadlineHourTextView.getText().toString();
                                task.passline = passTextView.getText().toString() + passHourTextView.getText().toString();
                                task.safeline = safeTextView.getText().toString() + safeHourTextView.getText().toString();
                                task.SmallTasks = items;
                            }
                        });
                    }

                    Toast.makeText(TaskActivity.this, "保存しました", Toast.LENGTH_SHORT).show();

                    finish();

                }

                break;

            case R.id.delete:
                //task.deleteFromRealm();
                if (newJudge != 1){
                    AlertDialog.Builder alertDlg = new AlertDialog.Builder(this);
                    alertDlg.setTitle("本当に削除しますか？");
                    alertDlg.setMessage("一度削除すると元には戻ません");
                    alertDlg.setPositiveButton(
                            "削除する",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // OK ボタンクリック処理
                                    realm.executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            task = realm.where(Task.class).equalTo("updateDate", getIntent().getStringExtra("updateDate")).findFirst();
                                            RealmResults<SmallTask> results = realm.where(SmallTask.class).equalTo("id", task.id).findAll();
                                            results.deleteAllFromRealm();
                                            task.deleteFromRealm();
                                        }
                                    });
                                    Toast.makeText(TaskActivity.this,"削除しました",Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                    alertDlg.setNegativeButton(
                            "キャンセル",
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
                                if (newJudge == 1) {
                                    realm.executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            RealmResults<SmallTask> results = realm.where(SmallTask.class).equalTo("id", idDate).findAll();
                                            results.deleteAllFromRealm();
                                        }
                                    });
                                    Toast.makeText(TaskActivity.this, "変更を破棄しました", Toast.LENGTH_SHORT).show();
                                } else if (smallTaskSave == 1){
                                    Toast.makeText(TaskActivity.this,"変更を破棄しました(保存した詳細タスクはそのままです)",Toast.LENGTH_SHORT).show();
//                                        realm.executeTransaction(new Realm.Transaction() {
//                                            @Override
//                                            public void execute(Realm realm) {
//                                                task = realm.where(Task.class).equalTo("updateDate", getIntent().getStringExtra("updateDate")).findFirst();
//                                                RealmResults<SmallTask> results = realm.where(SmallTask.class).equalTo("id", task.id).findAll();
//                                                results.deleteAllFromRealm();
//                                                task.deleteFromRealm();
//                                            }
//                                        });
                                } else {
                                    Toast.makeText(TaskActivity.this, "変更を破棄しました", Toast.LENGTH_SHORT).show();
                                }
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
