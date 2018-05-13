package jordan_jefferson.com.oncallphonemanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.codemybrainsout.onboarder.AhoyOnboarderActivity;
import com.codemybrainsout.onboarder.AhoyOnboarderCard;

import java.util.ArrayList;
import java.util.List;

public class NewUserOnBoardingActivity extends AhoyOnboarderActivity {

    public static final String COMPLETED_ONBOARDING_PREF_NAME = "Onboarding Completed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String card1Description = "This app allows you to manage your incoming phone calls much like" +
                " a priority contact in your Do Not Disturb Settings.";

        String card2Description = "However, you do not need to know the full phone number of the person " +
                "trying to reach you. just replace any unknown digits with \"#\" as shown above.";

        String card3Description = "Allow Do not Disturb access if you would like to receive " +
                "calls from contacts added in this app while your phone is on silent. This can be " +
                "accessed in settings.";

        String card4Description = "You will be prompted to allow this app to make and manage phone " +
                "calls after this tutorial. Allowing this permission will allow you to enable the On " +
                "Call feature of this app.";

        AhoyOnboarderCard ahoyOnboarderCard1 = new AhoyOnboarderCard("Welcome", card1Description, R.mipmap.ic_launcher_round);
        ahoyOnboarderCard1.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard1.setTitleColor(R.color.secondaryColor);
        ahoyOnboarderCard1.setDescriptionColor(R.color.secondaryColor);
        ahoyOnboarderCard1.setTitleTextSize(dpToPixels(10, this));
        ahoyOnboarderCard1.setDescriptionTextSize(dpToPixels(6, this));
        //ahoyOnboarderCard1.setIconLayoutParams(iconWidth, iconHeight, marginTop, marginLeft, marginRight, marginBottom);

        AhoyOnboarderCard ahoyOnboarderCard2 = new AhoyOnboarderCard("Example", card2Description, null);
        ahoyOnboarderCard2.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard2.setTitleColor(R.color.secondaryColor);
        ahoyOnboarderCard2.setDescriptionColor(R.color.secondaryColor);
        ahoyOnboarderCard2.setTitleTextSize(dpToPixels(10, this));
        ahoyOnboarderCard2.setDescriptionTextSize(dpToPixels(6, this));
        //ahoyOnboarderCard2.setIconLayoutParams(iconWidth, iconHeight, marginTop, marginLeft, marginRight, marginBottom);

        AhoyOnboarderCard ahoyOnboarderCard3 = new AhoyOnboarderCard("Silent Mode", card3Description, R.drawable.ic_notifications_active_secondary_108dp);
        ahoyOnboarderCard3.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard3.setTitleColor(R.color.secondaryColor);
        ahoyOnboarderCard3.setDescriptionColor(R.color.secondaryColor);
        ahoyOnboarderCard3.setTitleTextSize(dpToPixels(10, this));
        ahoyOnboarderCard3.setDescriptionTextSize(dpToPixels(6, this));
        //ahoyOnboarderCard3.setIconLayoutParams(iconWidth, iconHeight, marginTop, marginLeft, marginRight, marginBottom);

        AhoyOnboarderCard ahoyOnboarderCard4 = new AhoyOnboarderCard("App Permissions", card4Description, R.drawable.ic_phone_in_talk_secondary_108dp);
        ahoyOnboarderCard4.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard4.setTitleColor(R.color.secondaryColor);
        ahoyOnboarderCard4.setDescriptionColor(R.color.secondaryColor);
        ahoyOnboarderCard4.setTitleTextSize(dpToPixels(10, this));
        ahoyOnboarderCard4.setDescriptionTextSize(dpToPixels(6, this));
        //ahoyOnboarderCard4.setIconLayoutParams(iconWidth, iconHeight, marginTop, marginLeft, marginRight, marginBottom);

        List<AhoyOnboarderCard> pages = new ArrayList<>();
        pages.add(ahoyOnboarderCard1);
        pages.add(ahoyOnboarderCard2);
        pages.add(ahoyOnboarderCard3);
        pages.add(ahoyOnboarderCard4);

        setOnboardPages(pages);

//        List<Integer> colorList = new ArrayList<>();
//        colorList.add(R.color.secondaryColor);
//        colorList.add(R.color.primaryColor);
//        colorList.add(R.color.solid_three);
//        setColorBackground(colorList);

        setColorBackground(R.color.primaryColor);

    }

    @Override
    public void onFinishButtonPressed() {
        SharedPreferences.Editor sharedPreferencesEditor =
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        sharedPreferencesEditor.putBoolean(
                COMPLETED_ONBOARDING_PREF_NAME, true);
        sharedPreferencesEditor.apply();

//        startActivity(new Intent(this, CallManagerActivity.class));

        finish();
    }

    /**
     * Called when pointer capture is enabled or disabled for the current window.
     *
     * @param hasCapture True if the window has pointer capture.
     */
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
