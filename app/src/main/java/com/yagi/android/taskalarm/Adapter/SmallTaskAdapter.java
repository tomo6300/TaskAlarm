package com.yagi.android.taskalarm.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yagi.android.taskalarm.MainActivity;
import com.yagi.android.taskalarm.Model.SmallTask;
import com.yagi.android.taskalarm.Model.Task;
import com.yagi.android.taskalarm.R;
import com.yagi.android.taskalarm.SmallTaskActivity;
import com.yagi.android.taskalarm.TaskActivity;

import java.util.Calendar;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import io.realm.Realm;
import io.realm.RealmResults;

import static com.yagi.android.taskalarm.TaskActivity.idDate;

public class SmallTaskAdapter extends ArrayAdapter<SmallTask> {

    public Realm realm;
    private Context context;

    SmallTask smallTask1;

    private final LayoutInflater inflater;

    public SmallTaskAdapter(Context context, int textViewResourceId, List<SmallTask> objects) {
        super(context, textViewResourceId, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        //SmallTask smallTask = getItem(position);

        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.layout_task, parent, false);

        }

        realm = Realm.getDefaultInstance();

        smallTask1 = getItem(position);
        Log.d("Sitem",String.valueOf(getItem(position)));


        ((TextView) convertView.findViewById(R.id.task_title)).setText(smallTask1.title);
        ((TextView) convertView.findViewById(R.id.task_deadline)).setText(smallTask1.deadline);

        String deadline = smallTask1.deadline;
        String passLine = smallTask1.passline;
        String safeLine = smallTask1.safeline;

        // 日時1を「2014/01/02 11:22:33」に設定します
        Calendar cal1 = Calendar.getInstance();
        cal1.set(2018, 8, 3, 11, 22, 33);

        Calendar cal2 = Calendar.getInstance();
        cal2.getTime();

        Calendar cal3 = Calendar.getInstance();
        extraction (deadline, cal3);

        Calendar cal4 = Calendar.getInstance();
        extraction(passLine, cal4);

        Calendar cal5 = Calendar.getInstance();
        extraction(safeLine, cal5);


        int diff = cal2.compareTo(cal3);
        int diff2 = cal2.compareTo(cal4);
        int diff3 = cal2.compareTo(cal5);

        Log.d(smallTask1.title,smallTask1.achieve);
        if (TaskActivity.reuse == 1){
            if (smallTask1.achieve.equals(String.valueOf(1))){
//            ImageView smallTaskImage = (ImageView)convertView.findViewById(R.id.task_image);
//            smallTaskImage.setImageResource(R.drawable.achieve_gold);
                ((ImageView) convertView.findViewById(R.id.task_image)).setImageResource(R.drawable.achieve_gold);
                Log.d(smallTask1.title,smallTask1.achieve + String.valueOf(position));
            } else {
                ((ImageView) convertView.findViewById(R.id.task_image)).setImageResource(R.drawable.frame_style);
            }

        } else if (smallTask1.achieve.equals(String.valueOf(1))){
//            ImageView smallTaskImage = (ImageView)convertView.findViewById(R.id.task_image);
//            smallTaskImage.setImageResource(R.drawable.achieve_gold);
            ((ImageView) convertView.findViewById(R.id.task_image)).setImageResource(R.drawable.achieve_gold);
            Log.d(smallTask1.title,smallTask1.achieve + String.valueOf(position));
        } else if (diff > 0){
            ((ImageView) convertView.findViewById(R.id.task_image)).setImageResource(R.drawable.cross);
        } else if (diff2 > 0){
            ((ImageView) convertView.findViewById(R.id.task_image)).setImageResource(R.drawable.warning);
        } else if (diff3 > 0){
            ((ImageView) convertView.findViewById(R.id.task_image)).setImageResource(R.drawable.caution);
        } else {
            ((ImageView) convertView.findViewById(R.id.task_image)).setImageResource(R.drawable.frame_style);
        }


        ((ImageView) convertView.findViewById(R.id.over_flow)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getContext(), v);
                smallTask1 = getItem(position);
                Log.d(smallTask1.title,smallTask1.achieve);
                if (smallTask1.achieve.equals(String.valueOf(0))) {
                    popup.getMenuInflater().inflate(R.menu.menu_over_flow, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(final MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.achieve:
                                    smallTask1 = realm.where(SmallTask.class).equalTo("updateDate", smallTask1.updateDate).findFirst();
                                    realm.executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            smallTask1 = getItem(position);
                                            smallTask1 = realm.where(SmallTask.class).equalTo("updateDate", smallTask1.updateDate).findFirst();
                                            Log.d("smalltask",String.valueOf(smallTask1));
                                            Log.d(smallTask1.title,smallTask1.achieve + "asas");
                                            smallTask1.achieve = String.valueOf(1);
                                        }
                                    });
                                    //((ImageView) v.findViewById(R.id.task_image)).setImageResource(R.drawable.achieve_gold);

                                    RealmResults<SmallTask> results = realm.where(SmallTask.class).equalTo("id",idDate).findAll();
                                    results = results.sort("updateDate");
                                    List<SmallTask> items = realm.copyFromRealm(results);
                                    SmallTaskAdapter adapter = new SmallTaskAdapter(getContext(), R.layout.layout_task, items);
                                    Log.d("results",String.valueOf(results));
                                    TaskActivity.detailListView.setAdapter(adapter);

                                    AlertDialog.Builder alertDlg = new AlertDialog.Builder(getContext());
                                    alertDlg.setTitle("小タスク達成！");
                                    alertDlg.setMessage("この調子！");
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
                                                            smallTask1 = getItem(position);
                                                            smallTask1 = realm.where(SmallTask.class).equalTo("updateDate", smallTask1.updateDate).findFirst();
                                                            smallTask1.achieve = String.valueOf(0);
                                                        }
                                                    });
                                                    RealmResults<SmallTask> results = realm.where(SmallTask.class).equalTo("id",idDate).findAll();
                                                    results = results.sort("updateDate");
                                                    List<SmallTask> items = realm.copyFromRealm(results);
                                                    SmallTaskAdapter adapter = new SmallTaskAdapter(getContext(), R.layout.layout_task, items);
                                                    TaskActivity.detailListView.setAdapter(adapter);
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
                                                            //smallTask1 = getItem(position);
                                                            //Log.d("item",String.valueOf(item.getItemId()));
                                                            SmallTask smallTask = getItem(position);
                                                            smallTask = realm.where(SmallTask.class).equalTo("updateDate", smallTask.updateDate).findFirst();
                                                            //Log.d("smallTask", String.valueOf(position));
                                                            smallTask.deleteFromRealm();
                                                        }
                                                    });
                                                    RealmResults<SmallTask> results = realm.where(SmallTask.class).equalTo("id",idDate).findAll();
                                                    results = results.sort("updateDate");
                                                    Log.d("a", String.valueOf(results));
                                                    List<SmallTask> items = realm.copyFromRealm(results);
                                                    SmallTaskAdapter adapter = new SmallTaskAdapter(getContext(), R.layout.layout_task, items);
                                                    TaskActivity.detailListView.setAdapter(adapter);
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
                    popup.getMenuInflater().inflate(R.menu.menu_over_flow_small_achieve, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(final MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.reuse:
                                    smallTask1 = realm.where(SmallTask.class).equalTo("updateDate", smallTask1.updateDate).findFirst();
                                    realm.executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            smallTask1 = getItem(position);
                                            smallTask1 = realm.where(SmallTask.class).equalTo("updateDate", smallTask1.updateDate).findFirst();
                                            Log.d("smalltask",String.valueOf(smallTask1));
                                            Log.d(smallTask1.title,smallTask1.achieve + "asas");
                                            smallTask1.achieve = String.valueOf(0);
                                        }
                                    });
                                    //((ImageView) v.findViewById(R.id.task_image)).setImageResource(R.drawable.achieve_gold);

                                    RealmResults<SmallTask> results = realm.where(SmallTask.class).equalTo("id",idDate).findAll();
                                    results = results.sort("updateDate");
                                    List<SmallTask> items = realm.copyFromRealm(results);
                                    SmallTaskAdapter adapter = new SmallTaskAdapter(getContext(), R.layout.layout_task, items);
                                    Log.d("results",String.valueOf(results));
                                    TaskActivity.detailListView.setAdapter(adapter);

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
                                                            smallTask1 = getItem(position);
                                                            smallTask1 = realm.where(SmallTask.class).equalTo("updateDate", smallTask1.updateDate).findFirst();
                                                            smallTask1.deleteFromRealm();
                                                        }
                                                    });
                                                    RealmResults<SmallTask> results = realm.where(SmallTask.class).equalTo("id",idDate).findAll();
                                                    results = results.sort("updateDate");
                                                    List<SmallTask> items = realm.copyFromRealm(results);
                                                    SmallTaskAdapter adapter = new SmallTaskAdapter(getContext(), R.layout.layout_task, items);
                                                    TaskActivity.detailListView.setAdapter(adapter);
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

        return convertView;

    }

    public void extraction (String line, Calendar calendar){
        int cala = line.indexOf("年");
        int calb = line.indexOf("月");
        int calc = line.indexOf("日");
        int cald = line.indexOf(":");
        calendar.set(Integer.parseInt(line.substring(0,cala)), Integer.parseInt(line.substring(cala+1,calb)) - 1, Integer.parseInt(line.substring(calb+1,calc)),Integer.parseInt(line.substring(calc+1,cald)), Integer.parseInt(line.substring(cald+1)), 00);
    }

}