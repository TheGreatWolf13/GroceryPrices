package tgw.groceryprices;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public abstract class GenericActivity extends AppCompatActivity {

    protected void setScreen(Class<? extends GenericActivity> clazz) {
        this.startActivity(new Intent(this, clazz));
    }
}
