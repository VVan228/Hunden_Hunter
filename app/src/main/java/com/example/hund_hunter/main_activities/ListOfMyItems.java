package com.example.hund_hunter.main_activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.hund_hunter.R;
import com.example.hund_hunter.data_classes.Order;
import com.example.hund_hunter.fire_classes.FireDB;
import com.example.hund_hunter.fire_classes.interfaces.OnDataChangeListener;
import com.example.hund_hunter.other_classes.DBHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

// класс для списка своих объявлений
public class ListOfMyItems extends Activity {

    Button back, add_new_adv;
    ListView list;
    public static CustomListAdapter adapter;
    public static ArrayList<Pet> pets;
    public static FireDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_items_activity);

        list = findViewById(R.id.lv_list_items);
        back = (Button) findViewById(R.id.bth_back_from_list);
        add_new_adv = (Button) findViewById(R.id.bth_new_adv);

        back.setOnClickListener(v -> finish());

        add_new_adv.setOnClickListener(v -> startActivity((new Intent(ListOfMyItems.this, OrderCreationActivity.class))));

        pets = new ArrayList<>();
        adapter = new CustomListAdapter(this, R.layout.list_item, pets);
        list.setAdapter(adapter);

        db = new FireDB();


        final DBHelper dbhelper = new DBHelper(this);
        SQLiteDatabase sqldb = dbhelper.getWritableDatabase();

        String Query = "Select * from " + DBHelper.TABLE_NAME;
        Cursor cursor = sqldb.rawQuery(Query, null);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String current_email;
        if(auth.getCurrentUser()==null){
            return;
        }else{
            current_email = auth.getCurrentUser().getEmail();
        }
        while(cursor.moveToNext()) {
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String link = cursor.getString(cursor.getColumnIndexOrThrow("link"));
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
            if(email.equals(current_email)){
                pets.add(new Pet(name, link, id));
                adapter.notifyDataSetChanged();
            }
        }
        cursor.close();
    }
}


class Pet{
    String name;
    String path;
    int id;
    Pet(String name, String path, int id){
        this.name = name;
        this.path = path;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public int getId() {
        return id;
    }
}

class CustomListAdapter extends ArrayAdapter<Pet> {

    private Context context;

    public CustomListAdapter(Context context, int resourceId, List<Pet> items ) {
        super(context, resourceId, items);
        this.context = context;
    }

    private class ViewHolder {
        TextView name;
        Button button;
    }
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        Pet rowItem = getItem(position);
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();

            holder.name = convertView.findViewById(R.id.pet_name);
            holder.button = (Button)convertView.findViewById(R.id.delete_pet);
            convertView.setTag(holder);
        }
        else
            holder = (ViewHolder) convertView.getTag();

        holder.name.setText(rowItem.getName());

        holder.button.setOnClickListener(v -> {
            Pet rowItem1 = getItem(position);
            String path = rowItem1.getPath();

            ListOfMyItems.db.removeParticularChild(path);

            final DBHelper dbhelper = new DBHelper(context);
            SQLiteDatabase sqldb = dbhelper.getWritableDatabase();
            sqldb.execSQL("DELETE FROM " + DBHelper.TABLE_NAME + " WHERE _id = " + rowItem1.getId() + ";");



            ListOfMyItems.pets.remove(position);
            ListOfMyItems.adapter.notifyDataSetChanged();
        });

        return convertView;
    }

}




