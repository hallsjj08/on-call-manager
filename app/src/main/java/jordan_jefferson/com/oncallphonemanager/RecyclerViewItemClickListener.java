package jordan_jefferson.com.oncallphonemanager;

import android.view.View;

import jordan_jefferson.com.oncallphonemanager.data.Contact;

/*
An interface to provide an onClickItem method for the RecyclerView.
 */

//TODO: create an interface that allows the user to swipe an item to show a delete button.
public interface RecyclerViewItemClickListener {

    void recyclerViewItemClicked(View v, Object object);

}
