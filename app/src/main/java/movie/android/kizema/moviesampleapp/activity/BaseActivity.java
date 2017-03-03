package movie.android.kizema.moviesampleapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    private boolean hasSavedInstanceState;
    private boolean onResumeCalled = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hasSavedInstanceState = savedInstanceState != null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        onResumeCalled = true;
    }

    protected boolean getShouldGoToServer(){
        return !hasSavedInstanceState && !onResumeCalled;
    }
}
