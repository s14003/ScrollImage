package jp.ac.it_college.std.s14003.android.scrollimage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;

public class MainActivity extends AppCompatActivity {
    public float disp_w, disp_h;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display disp = getWindowManager().getDefaultDisplay();
        disp_w = disp.getWidth();
        disp_h = disp.getHeight();

        setContentView(new ImageScroll(this));
    }

}
