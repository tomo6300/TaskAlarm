package com.yagi.android.taskalarm;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yagi.android.taskalarm.Adapter.TaskAdapter;
import com.yagi.android.taskalarm.Model.SmallTask;
import com.yagi.android.taskalarm.Model.Task;

import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import io.realm.Realm;
import io.realm.RealmResults;

public class AchieveActivity extends AppCompatActivity {
    private TaskAdapter taskAdapter;
    public static ListView taskList;
    public static TextView emptyTextView;
    private Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achieve);

        Realm.init(getApplicationContext());
        realm = Realm.getDefaultInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        taskList = (ListView) findViewById(R.id.category_list);

        taskList.setAdapter(taskAdapter);

        emptyTextView = (TextView) findViewById(R.id.emptyTextView);

        taskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Task task = (Task) adapterView.getItemAtPosition(position);
                Intent intent = new Intent(AchieveActivity.this, TaskActivity.class);
                intent.putExtra("updateDate", task.updateDate);
                intent.putExtra("reuse",1);
                startActivity(intent);

            }
        });

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
        actionBar.setTitle(Html.fromHtml("<font color='#FFFFFF'>達成したタスク</font>"));

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
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            item.setIcon(R.drawable.back);
            finish();
        }
        return true;
    }

    public void setMemoList() {

        //realmから読み取る
        RealmResults<Task> results = realm.where(Task.class).equalTo("achieve",String.valueOf(1)).findAll();
        results = results.sort("updateDate");
        List<Task> items = realm.copyFromRealm(results);

        TaskAdapter adapter = new TaskAdapter(this, R.layout.layout_task, items);

        taskList.setAdapter(adapter);

        if (items.isEmpty()){
            taskList.setEmptyView(emptyTextView);
            Log.d("b", "b");
        } else {
            emptyTextView.setVisibility(View.GONE);
        }

    }
}
