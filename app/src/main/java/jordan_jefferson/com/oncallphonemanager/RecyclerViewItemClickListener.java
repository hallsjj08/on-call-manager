package jordan_jefferson.com.oncallphonemanager;

import android.view.View;

public interface RecyclerViewItemClickListener<T> {

    void recyclerViewItemClicked(View v, T data);

}
