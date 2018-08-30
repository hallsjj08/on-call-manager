package jordan_jefferson.com.oncallphonemanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import java.lang.reflect.InvocationTargetException;

public class OnBoardingActivity extends AppCompatActivity {

    private static final String TAG = "ONBOARDING";
    public static final String COMPLETED_ONBOARDING_PREF_NAME = "Onboarding Completed";

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private Button bNext;
    private Button bFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        View onBoardingLayout = findViewById(R.id.main_content);
        int navBarHeight = getNavigationBarSize(getApplicationContext()).y;
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) onBoardingLayout.getLayoutParams();
        params.setMargins(0,0,0,navBarHeight);
        onBoardingLayout.setLayoutParams(params);

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        TabLayout tabLayout = findViewById(R.id.tabDots);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);

        Button bSkip = findViewById(R.id.intro_btn_skip);
        bNext = findViewById(R.id.intro_btn_next);
        bFinish = findViewById(R.id.intro_btn_finish);

        onSkipFinishedPressed(bSkip);
        onSkipFinishedPressed(bFinish);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bNext.setVisibility(position == 2 ? View.GONE : View.VISIBLE);
                bFinish.setVisibility(position == 2 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
            }
        });

    }

    private void onSkipFinishedPressed(Button button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean(COMPLETED_ONBOARDING_PREF_NAME, false)){
                    finish();
                }else{
                    SharedPreferences.Editor sharedPreferencesEditor =
                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                    sharedPreferencesEditor.putBoolean(
                            COMPLETED_ONBOARDING_PREF_NAME, true);
                    sharedPreferencesEditor.apply();

                    Intent intent = new Intent(OnBoardingActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }

            }
        });
    }

    public static Point getNavigationBarSize(Context context) {
        Point appUsableSize = getAppUsableScreenSize(context);
        Point realScreenSize = getRealScreenSize(context);

        // navigation bar on the side
        if (appUsableSize.x < realScreenSize.x) {
            return new Point(realScreenSize.x - appUsableSize.x, appUsableSize.y);
        }

        // navigation bar at the bottom
        if (appUsableSize.y < realScreenSize.y) {
            return new Point(appUsableSize.x, realScreenSize.y - appUsableSize.y);
        }

        // navigation bar is not present
        return new Point();
    }

    public static Point getAppUsableScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static Point getRealScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();

        display.getRealSize(size);

        return size;
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {

        if(mViewPager.getCurrentItem() > 0){
            mViewPager.setCurrentItem(mViewPager.getCurrentItem()-1, true);
        }else {
            super.onBackPressed();
        }
    }
}
