package wovilon.pingpong1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import wovilon.pingpong1.db.DbUpdater;
import wovilon.pingpong1.model.Level;

public class LevelsListActivity extends AppCompatActivity {
    ListView levelsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels_list);

        // initializing listView and arrayList
        levelsListView=(ListView)findViewById(R.id.LevelsListView);
        //levelsListView.setBackgroundResource(R.drawable.menubackground);

        final ArrayList<Map<String, Object>> levelsList=new ArrayList<>();
        Map<String, Object> m;
        String levelLb="Level";

        ArrayList<String> levelsNamesList= new ArrayList<>();
        DbUpdater dbUpdater=new DbUpdater(this, "UserLevels");

        for (int i=0; i<dbUpdater.getCount(); i++){
            m=new HashMap<>();
            m.put(levelLb,dbUpdater.getLevelFromDb(i).getName());
            levelsList.add(m);
            //levelsList.add(dbUpdater.getLevelFromDb(i));;
            levelsNamesList.add(dbUpdater.getLevelFromDb(i).getName());
        }




        String[] from = {levelLb};
        int[] to = {R.id.LevelTextView};

        final SimpleAdapter adapter = new SimpleAdapter(this, levelsList, R.layout.level_list_item, from, to);

        //set adapter
        //ArrayAdapter levelsAdapter=new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, levelsNamesList);
        levelsListView.setAdapter(adapter);
        levelsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(LevelsListActivity.this, GameActivity.class);
                intent.putExtra("LevelType", "UserLevels");
                intent.putExtra("LevelNumber", i);
                startActivity(intent);
            }
        });


    }
}
