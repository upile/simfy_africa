package upile.simfy;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import upile.simfy.database.DatabaseHandler;
import upile.simfy.models.UserDetails;

public class UserActivity extends AppCompatActivity {
    TextView nameEditText;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        AppData.setActivity(this);

        db = new DatabaseHandler(this);

        nameEditText = (TextView) findViewById(R.id.edittext_name);
        Button submit = (Button) findViewById(R.id.btn_submit);
        submit.setOnClickListener(submitClickListener);
    }

    View.OnClickListener submitClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String name = nameEditText.getText().toString().trim();
            if (name.matches("")) {
                Snackbar.make(v, "You did not enter a name, Please enter name first", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return;
            } else {
                UserDetails userDetails = new UserDetails();
                userDetails.setName(name);
                List<UserDetails> list = db.getAllUserDetails();
                if (list.size() > 0) {
                    for (UserDetails user :
                            list) {
                        db.deleteUser(user);
                    }
                }

                db.addUserDetails(userDetails);
                Intent i = new Intent(UserActivity.this, MainActivity.class);
                i.putExtra("FromLogin", true);
                startActivity(i);
                finish();
            }

        }
    };

}
