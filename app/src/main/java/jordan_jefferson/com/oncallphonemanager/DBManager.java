package jordan_jefferson.com.oncallphonemanager;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/*
DBManager manages the database information for the users contact list. It has basic CRUD methods.
 */
public class DBManager extends SQLiteOpenHelper {

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DBContract.DBEntry.TABLE_NAME + " (" +
                    DBContract.DBEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DBContract.DBEntry.COLUMN_NAME_CONTACT_NAME + " TEXT," +
                    DBContract.DBEntry.COLUMN_NAME_COMPANY_NAME + " TEXT," +
                    DBContract.DBEntry.COLUMN_NAME_DISPLAY_PHONE_NUMBER + " TEXT," +
                    DBContract.DBEntry.COLUMN_NAME_REGEX_PHONE_NUMBER + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DBContract.DBEntry.TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Contacts.db";

    DBManager(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);

    }

    //TODO: update onUpgrade to handle upgraded tables so users don't lose their local data.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion1) {

        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);

    }

    @SuppressLint("Unused") //deleteTable is unused currently. Remove the Suppress Lint when used.
    public void deleteTable(SQLiteDatabase sqLiteDatabase){
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
    }

    public void updateContact(Contact contact){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.DBEntry.COLUMN_NAME_CONTACT_NAME, contact.get_contactName());
        values.put(DBContract.DBEntry.COLUMN_NAME_COMPANY_NAME, contact.get_companyName());
        values.put(DBContract.DBEntry.COLUMN_NAME_DISPLAY_PHONE_NUMBER, contact.get_contactDisplayNumber());
        values.put(DBContract.DBEntry.COLUMN_NAME_REGEX_PHONE_NUMBER, contact.get_contactRegexNumber());
        db.update(DBContract.DBEntry.TABLE_NAME, values, DBContract.DBEntry._ID + "=" + contact.get_id(), null);
        db.close();
    }

    public void addContact(Contact contact){
        ContentValues values = new ContentValues();
        values.put(DBContract.DBEntry.COLUMN_NAME_CONTACT_NAME, contact.get_contactName());
        values.put(DBContract.DBEntry.COLUMN_NAME_COMPANY_NAME, contact.get_companyName());
        values.put(DBContract.DBEntry.COLUMN_NAME_DISPLAY_PHONE_NUMBER, contact.get_contactDisplayNumber());
        values.put(DBContract.DBEntry.COLUMN_NAME_REGEX_PHONE_NUMBER, contact.get_contactRegexNumber());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(DBContract.DBEntry.TABLE_NAME, null, values);
        db.close();
    }

    public void deleteContact(Contact contact){

        SQLiteDatabase db = getWritableDatabase();
        db.delete(DBContract.DBEntry.TABLE_NAME, DBContract.DBEntry._ID + "=" + contact.get_id(), null);
        db.close();

    }

    public ArrayList<Contact> retrieveContacts(){

        ArrayList<Contact> contactList = new ArrayList<>();
        String contactName, companyName, contactDisplayNumber, contactRegexNumber;
        int id;


        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + DBContract.DBEntry.TABLE_NAME + " WHERE 1";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex(DBContract.DBEntry.COLUMN_NAME_CONTACT_NAME)) != null){

                id = c.getInt(c.getColumnIndex(DBContract.DBEntry._ID));
                contactName = c.getString(c.getColumnIndex(DBContract.DBEntry.COLUMN_NAME_CONTACT_NAME));
                companyName = c.getString(c.getColumnIndex(DBContract.DBEntry.COLUMN_NAME_COMPANY_NAME));
                contactDisplayNumber = c.getString(c.getColumnIndex(DBContract.DBEntry.COLUMN_NAME_DISPLAY_PHONE_NUMBER));
                contactRegexNumber = c.getString(c.getColumnIndex(DBContract.DBEntry.COLUMN_NAME_REGEX_PHONE_NUMBER));

                Contact contact = new Contact(contactName, companyName, contactDisplayNumber, contactRegexNumber);
                contact.set_id(id);
                contactList.add(contact);
                c.moveToNext();
            }
        }

        c.close();
        db.close();
        return contactList;

    }
}
