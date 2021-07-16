package com.yagi.android.taskalarm;

import android.content.Intent;
import android.os.Bundle;


import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yagi.android.taskalarm.Adapter.TaskAdapter;
import com.yagi.android.taskalarm.Model.SmallTask;
import com.yagi.android.taskalarm.Model.Task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    private TaskAdapter taskAdapter;
    public static ListView taskList;
    private Realm realm;
    public static TextView emptyTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Realm.init(getApplicationContext());
        realm = Realm.getDefaultInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        taskList = (ListView) findViewById(R.id.category_list);

        emptyTextView = (TextView) findViewById(R.id.emptyTextView);

        taskList.setAdapter(taskAdapter);

        taskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Task task = (Task) adapterView.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, TaskActivity.class);
                intent.putExtra("updateDate", task.updateDate);
                startActivity(intent);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

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
        assert actionBar != null;
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
        if (item.getItemId() == R.id.achieve) {
            Intent intent = new Intent(MainActivity.this, AchieveActivity.class);
            startActivity(intent);
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
            }

        });
    }

}
