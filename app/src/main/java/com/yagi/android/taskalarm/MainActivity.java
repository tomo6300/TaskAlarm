package com.yagi.android.taskalarm;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.support.v7.widget.PopupMenu;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.yagi.android.taskalarm.Adapter.TaskAdapter;
import com.yagi.android.taskalarm.Model.SmallTask;
import com.yagi.android.taskalarm.Model.Task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    private TaskAdapter taskAdapter;
    public static ListView taskList;
    private Realm realm;
    private Toolbar toolbar;
    public static TextView emptyTextView;
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Realm.init(getApplicationContext());
        realm = Realm.getDefaultInstance();

        //taskAdapter = new TaskAdapter(this);
        //taskAdapter.setList(Task.findAll());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        taskList = (ListView) findViewById(R.id.category_list);

        emptyTextView = (TextView) findViewById(R.id.emptyTextView);

        taskList.setAdapter(taskAdapter);

        taskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Task task = (Task) adapterView.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, TaskActivity.class);
                intent.putExtra("updateDate", task.updateDate);
                //Log.d("position",task.updateDate);
                //intent.putExtra("card_id", task.getId());
                startActivity(intent);
            }
        });

//        taskList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
//                final Task task = (Task) adapterView.getItemAtPosition(position);
//                PopupMenu popup = new PopupMenu(MainActivity.this, view);
//                if (task.achieve.equals(String.valueOf(0))) {
//
//                    popup.getMenuInflater().inflate(R.menu.menu_over_flow, popup.getMenu());
//                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                        public boolean onMenuItemClick(final MenuItem item) {
//                            switch (item.getItemId()) {
//                                case R.id.achieve:
//                                    //task = realm.where(Task.class).equalTo("updateDate", task.updateDate).findFirst();
//
//                                    realm.executeTransaction(new Realm.Transaction() {
//                                        @Override
//                                        public void execute(Realm realm) {
//                                            //task = getItem(position);
//                                            //task = realm.where(Task.class).equalTo("updateDate", task.updateDate).findFirst();
//                                            task.achieve = String.valueOf(1);
//                                        }
//                                    });
//                                    taskList.setAdapter(taskAdapter);
//                                    setMemoList();
////                                    RealmResults<Task> results = realm.where(Task.class).equalTo("achieve", String.valueOf(0)).findAll();
////                                    List<Task> items = realm.copyFromRealm(results);
////                                    TaskAdapter adapter = new TaskAdapter(MainActivity.this, R.layout.layout_task, items);
////                                    MainActivity.taskList.setAdapter(adapter);
//                                    //MainActivity.taskList.setAdapter(TaskAdapter.this);
//
//                                    AlertDialog.Builder alertDlg = new AlertDialog.Builder(MainActivity.this);
//                                    alertDlg.setTitle("タスク達成！");
//                                    alertDlg.setMessage("おめでとうございます！");
//                                    alertDlg.setPositiveButton(
//                                            "OK",
//                                            new DialogInterface.OnClickListener() {
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    // OK ボタンクリック処理
//
//                                                }
//                                            });
//                                    alertDlg.setNegativeButton(
//                                            "元に戻す",
//                                            new DialogInterface.OnClickListener() {
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    // Cancel ボタンクリック処理
//                                                    realm.executeTransaction(new Realm.Transaction() {
//                                                        @Override
//                                                        public void execute(Realm realm) {
//                                                            //task = getItem(position);
//                                                            //task = realm.where(Task.class).equalTo("updateDate", task.updateDate).findFirst();
//                                                            task.achieve = String.valueOf(0);
//                                                        }
//                                                    });
//                                                    setMemoList();
////                                                    RealmResults<Task> results = realm.where(Task.class).equalTo("achieve", String.valueOf(0)).findAll();
////                                                    List<Task> items = realm.copyFromRealm(results);
////                                                    TaskAdapter adapter = new TaskAdapter(MainActivity.this, R.layout.layout_task, items);
////                                                    MainActivity.taskList.setAdapter(adapter);
//                                                }
//                                            });
//
//                                    // 表示
//                                    alertDlg.create().show();
//
//                                    return true;
//
//                                case R.id.delete:
//                                    AlertDialog.Builder alertDlg2 = new AlertDialog.Builder(MainActivity.this);
//                                    alertDlg2.setTitle("本当に削除しますか？");
//                                    alertDlg2.setMessage("一度削除すると元には戻ません");
//                                    alertDlg2.setPositiveButton(
//                                            "削除する",
//                                            new DialogInterface.OnClickListener() {
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    // OK ボタンクリック処理
//                                                    realm.executeTransaction(new Realm.Transaction() {
//                                                        @Override
//                                                        public void execute(Realm realm) {
//                                                            //task = getItem(position);
//                                                            //task = realm.where(Task.class).equalTo("updateDate", task.updateDate).findFirst();
//                                                            RealmResults<SmallTask> results = realm.where(SmallTask.class).equalTo("id", task.id).findAll();
//                                                            results.deleteAllFromRealm();
//                                                            task.deleteFromRealm();
//                                                        }
//                                                    });
//                                                    setMemoList();
////                                                    RealmResults<Task> results = realm.where(Task.class).equalTo("achieve", String.valueOf(0)).findAll();
////                                                    List<Task> items = realm.copyFromRealm(results);
////                                                    TaskAdapter adapter = new TaskAdapter(MainActivity.this, R.layout.layout_task, items);
////                                                    MainActivity.taskList.setAdapter(adapter);
//                                                    Toast.makeText(MainActivity.this, "削除しました", Toast.LENGTH_SHORT).show();
//                                                }
//                                            });
//                                    alertDlg2.setNegativeButton(
//                                            "キャンセル",
//                                            new DialogInterface.OnClickListener() {
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    // Cancel ボタンクリック処理
//                                                }
//                                            });
//
//                                    // 表示
//                                    alertDlg2.create().show();
//
//                            }
//                            return true;
//                        }
//                    });
//
//                } else {
//                    popup.getMenuInflater().inflate(R.menu.menu_over_flow_achieve, popup.getMenu());
//                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                        public boolean onMenuItemClick(final MenuItem item) {
//                            switch (item.getItemId()) {
//                                case R.id.reuse:
//                                    //task = getItem(position);
//                                    Intent intent = new Intent(MainActivity.this, TaskActivity.class);
//                                    intent.putExtra("updateDate", task.updateDate);
//                                    intent.putExtra("reuse", 1);
//                                    //Log.d("position",task.updateDate);
//                                    //intent.putExtra("card_id", task.getId());
//                                    startActivity(intent);
//
//                                    //task.achieve = String.valueOf(0);
//
//                                    return true;
//
//                                case R.id.delete:
//                                    AlertDialog.Builder alertDlg2 = new AlertDialog.Builder(MainActivity.this);
//                                    alertDlg2.setTitle("本当に削除しますか？");
//                                    alertDlg2.setMessage("一度削除すると元には戻ません");
//                                    alertDlg2.setPositiveButton(
//                                            "削除する",
//                                            new DialogInterface.OnClickListener() {
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    // OK ボタンクリック処理
//                                                    realm.executeTransaction(new Realm.Transaction() {
//                                                        @Override
//                                                        public void execute(Realm realm) {
//                                                            //task = getItem(position);
//                                                            //task = realm.where(Task.class).equalTo("updateDate", task.updateDate).findFirst();
//                                                            RealmResults<SmallTask> results = realm.where(SmallTask.class).equalTo("id", task.id).findAll();
//                                                            results.deleteAllFromRealm();
//                                                            task.deleteFromRealm();
//                                                        }
//                                                    });
//                                                    setMemoList();
////                                                    RealmResults<Task> results = realm.where(Task.class).equalTo("achieve", String.valueOf(1)).findAll();
////                                                    List<Task> items = realm.copyFromRealm(results);
////                                                    TaskAdapter adapter = new TaskAdapter(MainActivity.this, R.layout.layout_task, items);
////                                                    AchieveActivity.taskList.setAdapter(adapter);
//                                                    Toast.makeText(MainActivity.this, "削除しました", Toast.LENGTH_SHORT).show();
//                                                }
//                                            });
//                                    alertDlg2.setNegativeButton(
//                                            "キャンセル",
//                                            new DialogInterface.OnClickListener() {
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    // Cancel ボタンクリック処理
//                                                }
//                                            });
//
//                                    // 表示
//                                    alertDlg2.create().show();
//
//                            }
//                            return true;
//                        }
//                    });
//
//                }
//
//                popup.show();
//
//                return true;
//            }
//
//        });

        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //日付取得
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.JAPANESE);
                String updateDate = sdf.format(date);

                Intent i = new Intent(MainActivity.this, TaskActivity.class);
                i.putExtra("idDate",updateDate);
                i.putExtra("new",1);
                startActivity(i);
            }
        });

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setDisplayShowHomeEnabled(true);
        //actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(Html.fromHtml("<font color='#FFFFFF'>Task Alarm</font>"));

    }

    @Override
    protected void onResume() {
        super.onResume();

        setMemoList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        realm.close();

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.achieve:
                Intent intent = new Intent(MainActivity.this, AchieveActivity.class);
                startActivity(intent);
                break;

        }
        return true;
    }

    public void setMemoList() {

        //realmから読み取る
        RealmResults<Task> results = realm.where(Task.class).equalTo("achieve",String.valueOf(0)).findAll();
        results = results.sort("updateDate");
        List<Task> items = realm.copyFromRealm(results);

        TaskAdapter adapter = new TaskAdapter(this, R.layout.layout_task, items);

        taskList.setAdapter(adapter);

        Log.d("a", "a");

        if (items.isEmpty()){
            taskList.setEmptyView(emptyTextView);
            Log.d("b", "b");
        } else {
            emptyTextView.setVisibility(View.GONE);
        }
    }

    //データをRealmに保存
    public void save(final String title, final String deadline, final String passLine, final String safeLine, final String id) {

        //メモを保存する
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                Task Task = realm.createObject(Task.class);
                Task.title = title;
                Task.deadline = deadline;
                Task.passline = passLine;
                Task.safeline = safeLine;
                Task.id = id;

                //SmallTask SmallTask = realm.createObject(SmallTask.class);
                //Task.SmallTasks = SmallTasks;

            }

        });
    }

}
