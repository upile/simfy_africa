package upile.simfy;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import upile.simfy.adapter.CardAdapter;
import upile.simfy.database.DatabaseHandler;
import upile.simfy.models.ListItem;
import upile.simfy.models.UserDetails;
import upile.simfy.network.NetworkChangeReceiver;
import upile.simfy.network.NetworkUtil;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CardAdapter adapter;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppData.setActivity(this);
        db = new DatabaseHandler(this);

        List<UserDetails> userDetailsList = db.getAllUserDetails();
        if (userDetailsList.size() == 0) {
            Intent i = new Intent(MainActivity.this, UserActivity.class);
            startActivity(i);
            this.finish();
            return;
        }

        Intent intent = getIntent();
        boolean fromLogin = intent.getBooleanExtra("FromLogin", false);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        adapter = new CardAdapter(new ArrayList<ListItem>());
        recyclerView.setAdapter(adapter);
        getData();
        Button re_enterName = (Button) findViewById(R.id.enterName);
        Button exitApp = (Button) findViewById(R.id.exit);
        TextView welcomeTextView = (TextView) findViewById(R.id.welcome_txt);

        UserDetails userDetails = new UserDetails();
        if (userDetailsList.size() > 0) {
            for (UserDetails user : userDetailsList) {
                userDetails = user;
                break;
            }
        }
        if (fromLogin) {
            welcomeTextView.setText(String.format("welcome %s", userDetails.getName()));
        } else {
            welcomeTextView.setText(String.format("Welcome %s, thanks for returning, we hope you enjoy your stay!", userDetails.getName()));
        }

        re_enterName.setOnClickListener(enterNameClickListerner);
        exitApp.setOnClickListener(exitAppClickListerner);
    }

    View.OnClickListener enterNameClickListerner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            List<UserDetails> list = db.getAllUserDetails();
            if (list.size() > 0) {
                for (UserDetails user :
                        list) {
                    db.deleteUser(user);
                }
            }
            Intent i = new Intent(MainActivity.this, UserActivity.class);
            startActivity(i);
            finish();
        }
    };

    View.OnClickListener exitAppClickListerner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    private void getData() {
        class GetData extends AsyncTask<Void, Void, List<ListItem>> {
            List<ListItem> items;

            @Override
            protected List<ListItem> doInBackground(Void... params) {
                items = setData();
                AppData.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(NetworkUtil.getConnectivityStatusString(AppData.getActivity()).equalsIgnoreCase("Not connected to Internet")) {
                            NetworkChangeReceiver.displayDialog("Not connected to Internet");
                        }
                    }
                });

                return items;
            }

            @Override
            protected void onPostExecute(List<ListItem> items) {
                adapter.items = items;
                adapter.notifyDataSetChanged();
                super.onPostExecute(items);
            }
        }
        GetData gd = new GetData();
        gd.execute();
    }

    public List<ListItem> setData() {
        List<ListItem> items = new ArrayList<>();
        String[] images = getResources().getStringArray(R.array.images_array);
        String description = getResources().getString(R.string.very_long_description);
        for (int i = 0; i < images.length; i++) {
            ListItem item = new ListItem();
            item.setName(String.format("Track%s", i + 1));
            item.setImageUrl(images[i]);
            item.setDescription(description);
            items.add(item);
        }
        return items;
    }
}