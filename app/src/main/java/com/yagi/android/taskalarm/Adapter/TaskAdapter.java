package com.yagi.android.taskalarm.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


import com.yagi.android.taskalarm.AchieveActivity;
import com.yagi.android.taskalarm.MainActivity;
import com.yagi.android.taskalarm.Model.SmallTask;
import com.yagi.android.taskalarm.Model.Task;
import com.yagi.android.taskalarm.R;
import com.yagi.android.taskalarm.TaskActivity;

import java.util.Calendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

import static android.graphics.Color.rgb;

public class TaskAdapter extends ArrayAdapter<Task> {
    private List<Task> tasks;
    private Context context;
    private LayoutInflater inflater;
    public Realm realm;

    private Context mContext;

    private int deadPoint = 0;
    private int passPoint = 0;
    private int safePoint = 0;

    Task task;


    public TaskAdapter(Context context, int textViewResourceId, List<Task> objects) {
        super(context, textViewResourceId, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /*public void setList(List<Task> categories) {
        this.tasks = categories;
    }

    public void add(Task task) {
        this.tasks.add(task);
        notifyDataSetChanged();
    }*/

    /*public int getCount() {
        return this.tasks.size();
    }*/

    /*public Object getItem(int position) {
        return this.tasks.get(position);
    }*/

    /*public long getItemId(int position) {
        return ((Task) this.tasks.get(position)).getId().longValue();
    }*/

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        realm = Realm.getDefaultInstance();

        task = getItem(position);

        View v = convertView;
        smallTaskJudge();
        if (v == null) {
            if (deadPoint > 0 || passPoint > 0 || safePoint > 0) {
                v = this.inflater.inflate(R.layout.layout_task_large, parent, false);
            } else {
                v = this.inflater.inflate(R.layout.layout_task, parent, false);
            }
        }


        ((TextView) v.findViewById(R.id.task_title)).setText(task.title);
        ((TextView) v.findViewById(R.id.task_deadline)).setText(task.deadline);

        String deadline = task.deadline;
        String passLine = task.passline;
        String safeLine = task.safeline;

        // 日時1を「2014/01/02 11:22:33」に設定します
        Calendar cal1 = Calendar.getInstance();
        cal1.set(2018, 8, 3, 11, 22, 33);

        Calendar cal2 = Calendar.getInstance();
        cal2.getTime();

        Calendar cal3 = Calendar.getInstance();
        Log.d("deadline", deadline);
        extraction (deadline, cal3);

        Calendar cal4 = Calendar.getInstance();
        extraction(passLine, cal4);

        Calendar cal5 = Calendar.getInstance();
        extraction(safeLine, cal5);

        /*int cal3a = deadline.indexOf("年");
        int cal3b = deadline.indexOf("月");
        cal3.set(Integer.parseInt(deadline.substring(0,cal3a-1)),);*/


        // 2つの日時を比較し、結果によってメッセージを変えます
//        Log.d("cal1", String.valueOf(cal1));
//        Log.d("cal2", String.valueOf(cal2));
//        Log.d("cal3", String.valueOf(cal3));
//        Log.d("cal4", String.valueOf(cal4));
//        Log.d("cal5", String.valueOf(cal5));

        int diff = cal2.compareTo(cal3);
        int diff2 = cal2.compareTo(cal4);
        int diff3 = cal2.compareTo(cal5);
//        Log.d("diff", String.valueOf(diff));
//        Log.d("diff2", String.valueOf(diff2));
//        Log.d("diff3", String.valueOf(diff3));
        if (task.achieve.equals(String.valueOf(1))){
            ((ImageView) v.findViewById(R.id.task_image)).setImageResource(R.drawable.achieve_gold);
        } else if (diff > 0){
            ((ImageView) v.findViewById(R.id.task_image)).setImageResource(R.drawable.cross);
        } else if (diff2 > 0){
            ((ImageView) v.findViewById(R.id.task_image)).setImageResource(R.drawable.warning);
        } else if (diff3 > 0){
            ((ImageView) v.findViewById(R.id.task_image)).setImageResource(R.drawable.caution);
        } else {
            ((ImageView) v.findViewById(R.id.task_image)).setImageResource(R.drawable.frame_style);
        }

        //((ImageView) v.findViewById(R.id.task_image)).setImageResource(R.drawable.warning);

        smallTaskJudge();

        TextView small_task_judge = (TextView) v.findViewById(R.id.small_task_judge);
        if (deadPoint > 0){
            ((TextView) v.findViewById(R.id.small_task_judge)).setText("締切の過ぎた小タスクあり");
            //((TextView) v.findViewById(R.id.small_task_judge)).setBackgroundColor(rgb(255, 192, 203));
            ((TextView) v.findViewById(R.id.small_task_judge)).setTextColor(rgb(255, 0, 0));
//            small_task_judge.setBackgroundColor(rgb(255, 192, 203));
//            small_task_judge.setTextColor(rgb(255, 255, 255));
        } else if (passPoint > 0){
            ((TextView) v.findViewById(R.id.small_task_judge)).setText("締切直前の小タスクあり");
            //small_task_judge.setBackgroundColor(rgb(255, 192, 203));
            ((TextView) v.findViewById(R.id.small_task_judge)).setTextColor(rgb(255, 0, 0));
        } else if (safePoint > 0){
            ((TextView) v.findViewById(R.id.small_task_judge)).setText("締切の近い小タスクあり");
            //small_task_judge.setBackgroundColor(rgb(255, 235, 59));
            ((TextView) v.findViewById(R.id.small_task_judge)).setTextColor(rgb(230, 81, 0));
        } else {
            //((TextView) v.findViewById(R.id.small_task_judge)).setVisibility(View.GONE);
        }

        deadPoint = passPoint = safePoint = 0;

        /*int diff = cal1.compareTo(cal2);
        if (diff == 0) {

        } else if (diff > 0) {
            Log.d("a","a");
            ((ImageView) v.findViewById(R.id.task_image)).setImageResource(R.drawable.caution);
        } else {

        }*/

        ((ImageView) v.findViewById(R.id.over_flow)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getContext(), v);
                if (task.achieve.equals(String.valueOf(0))) {
                    popup.getMenuInflater().inflate(R.menu.menu_over_flow, popup.getMenu());
                    popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                        public boolean onMenuItemClick(final MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.achieve:
                                    task = realm.where(Task.class).equalTo("updateDate", task.updateDate).findFirst();

                                    realm.executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            task = getItem(position);
                                            task = realm.where(Task.class).equalTo("updateDate", task.updateDate).findFirst();
                                            task.achieve = String.valueOf(1);
                                        }
                                    });

                                    RealmResults<Task> results = realm.where(Task.class).equalTo("achieve",String.valueOf(0)).findAll();
                                    results = results.sort("updateDate");
                                    List<Task> items = realm.copyFromRealm(results);
                                    TaskAdapter adapter = new TaskAdapter(getContext(), R.layout.layout_task, items);
                                    MainActivity.taskList.setAdapter(adapter);
                                    if (items.isEmpty()){
                                        MainActivity.taskList.setEmptyView(MainActivity.emptyTextView);
                                        Log.d("b", "b");
                                    } else {
                                        MainActivity.emptyTextView.setVisibility(View.GONE);
                                    }
                                    //MainActivity.taskList.setAdapter(TaskAdapter.this);

                                    AlertDialog.Builder alertDlg = new AlertDialog.Builder(getContext());
                                    alertDlg.setTitle("タスク達成！");
                                    alertDlg.setMessage("おめでとうございます！");
                                    alertDlg.setPositiveButton(
                                            "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // OK ボタンクリック処理

                                                }
                                            });
                                    alertDlg.setNegativeButton(
                                            "元に戻す",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // Cancel ボタンクリック処理
                                                    realm.executeTransaction(new Realm.Transaction() {
                                                        @Override
                                                        public void execute(Realm realm) {
                                                            task = getItem(position);
                                                            task = realm.where(Task.class).equalTo("updateDate", task.updateDate).findFirst();
                                                            task.achieve = String.valueOf(0);
                                                        }
                                                    });
                                                    RealmResults<Task> results = realm.where(Task.class).equalTo("achieve",String.valueOf(0)).findAll();
                                                    results = results.sort("updateDate");
                                                    List<Task> items = realm.copyFromRealm(results);
                                                    TaskAdapter adapter = new TaskAdapter(getContext(), R.layout.layout_task, items);
                                                    MainActivity.taskList.setAdapter(adapter);
                                                }
                                            });

                                    // 表示
                                    alertDlg.create().show();

                                    return true;

                                case R.id.delete:
                                    AlertDialog.Builder alertDlg2 = new AlertDialog.Builder(getContext());
                                    alertDlg2.setTitle("本当に削除しますか？");
                                    alertDlg2.setMessage("一度削除すると元には戻ません");
                                    alertDlg2.setPositiveButton(
                                            "削除する",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // OK ボタンクリック処理
                                                    realm.executeTransaction(new Realm.Transaction() {
                                                        @Override
                                                        public void execute(Realm realm) {
                                                            task = getItem(position);
                                                            task = realm.where(Task.class).equalTo("updateDate", task.updateDate).findFirst();
                                                            RealmResults<SmallTask> results = realm.where(SmallTask.class).equalTo("id", task.id).findAll();
                                                            results.deleteAllFromRealm();
                                                            task.deleteFromRealm();
                                                        }
                                                    });
                                                    RealmResults<Task> results = realm.where(Task.class).equalTo("achieve",String.valueOf(0)).findAll();
                                                    results = results.sort("updateDate");
                                                    List<Task> items = realm.copyFromRealm(results);
                                                    TaskAdapter adapter = new TaskAdapter(getContext(), R.layout.layout_task, items);
                                                    MainActivity.taskList.setAdapter(adapter);
                                                    if (items.isEmpty()){
                                                        MainActivity.taskList.setEmptyView(MainActivity.emptyTextView);
                                                        Log.d("b", "b");
                                                    } else {
                                                        MainActivity.emptyTextView.setVisibility(View.GONE);
                                                    }
                                                    Toast.makeText(getContext(),"削除しました",Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                    alertDlg2.setNegativeButton(
                                            "キャンセル",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // Cancel ボタンクリック処理
                                                }
                                            });

                                    // 表示
                                    alertDlg2.create().show();

                            }
                            return true;
                        }
                    });

                } else {
                    popup.getMenuInflater().inflate(R.menu.menu_over_flow_achieve, popup.getMenu());
                    popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                        public boolean onMenuItemClick(final MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.reuse:
                                    task = getItem(position);
                                    Intent intent = new Intent(getContext(), TaskActivity.class);
                                    intent.putExtra("updateDate", task.updateDate);
                                    intent.putExtra("reuse",1);
                                    //Log.d("position",task.updateDate);
                                    //intent.putExtra("card_id", task.getId());
                                    getContext().startActivity(intent);

                                    //task.achieve = String.valueOf(0);

                                    return true;

                                case R.id.delete:
                                    AlertDialog.Builder alertDlg2 = new AlertDialog.Builder(getContext());
                                    alertDlg2.setTitle("本当に削除しますか？");
                                    alertDlg2.setMessage("一度削除すると元には戻ません");
                                    alertDlg2.setPositiveButton(
                                            "削除する",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // OK ボタンクリック処理
                                                    realm.executeTransaction(new Realm.Transaction() {
                                                        @Override
                                                        public void execute(Realm realm) {
                                                            task = getItem(position);
                                                            task = realm.where(Task.class).equalTo("updateDate", task.updateDate).findFirst();
                                                            RealmResults<SmallTask> results = realm.where(SmallTask.class).equalTo("id", task.id).findAll();
                                                            results.deleteAllFromRealm();
                                                            task.deleteFromRealm();
                                                        }
                                                    });
                                                    RealmResults<Task> results = realm.where(Task.class).equalTo("achieve",String.valueOf(1)).findAll();
                                                    results = results.sort("updateDate");
                                                    List<Task> items = realm.copyFromRealm(results);
                                                    TaskAdapter adapter = new TaskAdapter(getContext(), R.layout.layout_task, items);
                                                    AchieveActivity.taskList.setAdapter(adapter);
                                                    if (items.isEmpty()){
                                                        MainActivity.taskList.setEmptyView(MainActivity.emptyTextView);
                                                        Log.d("b", "b");
                                                    } else {
                                                        MainActivity.emptyTextView.setVisibility(View.GONE);
                                                    }
                                                    Toast.makeText(getContext(),"削除しました",Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                    alertDlg2.setNegativeButton(
                                            "キャンセル",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // Cancel ボタンクリック処理
                                                }
                                            });

                                    // 表示
                                    alertDlg2.create().show();

                            }
                            return true;
                        }
                    });

                }

                popup.show();

            }
        });
        return v;
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

    public void smallTaskJudge (){
        RealmResults<SmallTask> results = realm.where(SmallTask.class).equalTo("id",task.id).findAll();
        results = results.sort("updateDate");
        //RealmResults<SmallTask> results = realm.where(SmallTask.class).findAll();
        List<SmallTask> items = realm.copyFromRealm(results);

        SmallTask smallTask;

        for (int i = 0; i < items.size(); i++){
            smallTask = items.get(i);
            Log.d(smallTask.title, smallTask.deadline);
            String deadlineS = smallTask.deadline;
            String passLineS = smallTask.passline;
            String safeLineS = smallTask.safeline;

            // 日時1を「2014/01/02 11:22:33」に設定します

            Calendar cal6 = Calendar.getInstance();
            cal6.getTime();

            Calendar cal7 = Calendar.getInstance();
            Log.d("deadline", deadlineS);
            extraction (deadlineS, cal7);

            Calendar cal8 = Calendar.getInstance();
            extraction(passLineS, cal8);

            Calendar cal9 = Calendar.getInstance();
            extraction(safeLineS, cal9);

            int diff = cal6.compareTo(cal7);
            int diff2 = cal6.compareTo(cal8);
            int diff3 = cal6.compareTo(cal9);

            if (task.achieve.equals(String.valueOf(1))){

            } else if (diff > 0){
                deadPoint++;
            } else if (diff2 > 0){
                passPoint++;
            } else if (diff3 > 0){
                safePoint++;
            } else {

            }
        }

    }


    public void compare (View v){

    }

    public Context getmContext(){
        return mContext;
    }


}
