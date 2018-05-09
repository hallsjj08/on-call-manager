package jordan_jefferson.com.oncallphonemanager;

import android.provider.BaseColumns;

public final class DBContract {

    private DBContract(){}

    public static class DBEntry implements BaseColumns{
        public static final String TABLE_NAME = "contact_info";
        public static final String _ID = "id";
        public static final String COLUMN_NAME_CONTACT_NAME = "contact_name";
        public static final String COLUMN_NAME_COMPANY_NAME = "company_name";
        public static final String COLUMN_NAME_DISPLAY_PHONE_NUMBER = "display_phone_number";
        public static final String COLUMN_NAME_REGEX_PHONE_NUMBER = "regex_phone_number";
    }

}
