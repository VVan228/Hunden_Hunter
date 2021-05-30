package com.example.hund_hunter.main_activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.content.Context;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cursoradapter.widget.CursorAdapter;

import com.example.hund_hunter.R;
import com.example.hund_hunter.data_classes.Order;
import com.example.hund_hunter.fire_classes.FireDB;
import com.example.hund_hunter.fire_classes.MyChildListenerFactory;
import com.example.hund_hunter.fire_classes.MyQuery;
import com.example.hund_hunter.fire_classes.interfaces.OnChildAddedListener;
import com.example.hund_hunter.log_in_activities.UserAccountActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

// класс для списка своих объявлений
public class ListOfMyItems extends Activity {

    Button back;
    ListView list;
    ArrayList<String> names;
    CustomListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_items);

        list = findViewById(R.id.lv_list_items);
        back = (Button) findViewById(R.id.bth_back_from_list);
        back.setOnClickListener(v -> startActivity(new Intent(ListOfMyItems.this, UserAccountActivity.class)));

        names = new ArrayList<>();
        adapter = new CustomListAdapter(this, R.layout.list_item, names);
        list.setAdapter(adapter);

        FireDB db = new FireDB(new String[]{"orders"});

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        db.getData(new MyQuery(db.getRef()).orderBy("email").equalTo(email),
                new MyChildListenerFactory().addAddedListener(new OnChildAddedListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Order obj = snapshot.getValue(Order.class);
                names.add(0, obj.getPet());
                adapter.notifyDataSetChanged();
            }
        }).create());
    }
}


class CustomListAdapter extends ArrayAdapter<String> {

    private Context context;

    public CustomListAdapter(Context context, int resourceId, List<String> items ) {
        super(context, resourceId, items);
        this.context = context;
    }

    private class ViewHolder {
        TextView name;
        Button button;
    }
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        String rowItem = getItem(position);

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

        holder.name.setText(rowItem);

        holder.button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(context, "Delete button pressed: ", Toast.LENGTH_LONG).show();
            }

        });

        return convertView;
    }

}




