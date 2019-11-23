package jordan_jefferson.com.oncallphonemanager;

import android.view.View;

public interface RecyclerViewItemClickListener<T> {

    void onRecyclerViewItemClicked(View v, T data);

    void onRecyclerViewItemIsEnabledChange(View v, T data);

}
