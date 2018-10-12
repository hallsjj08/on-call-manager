package jordan_jefferson.com.oncallphonemanager.onboarding;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import jordan_jefferson.com.oncallphonemanager.R;

public class SectionsPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private int[] images = {R.drawable.man_answer_phone_image,
            R.drawable.add_contact_icon,
            R.drawable.onbaording_manager_icons};
    private String[] titles = {"WELCOME", "HOW IT WORKS", "EASILY MANAGEABLE"};
    private String[] descriptions = {"This app allows you to manage your incoming phone calls much like" +
            " a priority contact in your Do Not Disturb Settings.",
                                "However, you do not need to know the full phone number of the person " +
            "trying to reach you. just replace any unknown digits with \"#\" when adding or editing a contact.",
                                    "Just turn on the call manager in the title bar at the top and " +
             "any incoming calls matching numbers in your contact list will be pushed through."};

    public SectionsPagerAdapter(Context context){
        this.context = context;
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return titles.length;
    }

    /**
     * Determines whether a page View is associated with a specific key object
     * as returned by {@link #instantiateItem(ViewGroup, int)}. This method is
     * required for a PagerAdapter to function properly.
     *
     * @param view   Page View to check for association with <code>object</code>
     * @param object Object to check for association with <code>view</code>
     * @return true if <code>view</code> is associated with the key object <code>object</code>
     */
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    /**
     * Create the page for the given position.  The adapter is responsible
     * for adding the view to the container given here, although it only
     * must ensure this is done by the time it returns from
     * {@link #finishUpdate(ViewGroup)}.
     *
     * @param container The containing View in which the page will be shown.
     * @param position  The page position to be instantiated.
     * @return Returns an Object representing the new page.  This does not
     * need to be a View, but can be some other container of the page.
     */
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.fragment_on_boarding, container, false);

        ImageView image = view.findViewById(R.id.onBoardingImageView);
        TextView title = view.findViewById(R.id.textViewTitle);
        TextView description = view.findViewById(R.id.textViewDescription);

        image.setImageResource(images[position]);
        title.setText(titles[position]);
        description.setText(descriptions[position]);

        switch (position){
            case 0:
                view.setBackgroundColor(view.getResources().getColor(R.color.boardingScreenOne));
                break;
            case 1:
                view.setBackgroundColor(view.getResources().getColor(R.color.boardingScreenTwo));
                break;
            case 2:
                view.setBackgroundColor(view.getResources().getColor(R.color.boardingScreenThree));
                break;
        }

        container.addView(view);

        return view;
    }

    /**
     * Remove a page for the given position.  The adapter is responsible
     * for removing the view from its container, although it only must ensure
     * this is done by the time it returns from {@link #finishUpdate(ViewGroup)}.
     *
     * @param container The containing View from which the page will be removed.
     * @param position  The page position to be removed.
     * @param object    The same object that was returned by
     *                  {@link #instantiateItem(View, int)}.
     */
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
