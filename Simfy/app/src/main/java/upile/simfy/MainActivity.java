package upile.simfy;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        adapter = new CardAdapter(new ArrayList<ListItem>());
        recyclerView.setAdapter(adapter);
        getData();
    }

    private void getData() {
        class GetData extends AsyncTask<Void, Void, List<ListItem>> {
            List<ListItem> items;

            @Override
            protected List<ListItem> doInBackground(Void... params) {
                items = setData();
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
            item.setName(String.format("Track%s", i+1));
            item.setImageUrl(images[i]);
            item.setDescription(description);
            items.add(item);
        }
        return items;
    }
}