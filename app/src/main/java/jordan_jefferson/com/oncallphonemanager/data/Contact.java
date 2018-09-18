package jordan_jefferson.com.oncallphonemanager.data;

/*
Parameters:
    _id: an autoincremented id associated with class DBManager.
    _contactName: User defined contactName stored in a local database.
    _companyName: User defined companyName stored in a local database.
    _contactDisplayNumber: User defined phone number stored in a local database.
    _contactRegexNumber: A number used to compare with incoming phone calls stored in a local database.
 */

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "contacts")
public class Contact implements Serializable{

    @PrimaryKey(autoGenerate = true)
    private int _id;

    private String _contactName;
    private String _companyName;
    private String _contactDisplayNumber;
    private String _contactRegexNumber;


    public Contact(String contactName, String companyName, String contactDisplayNumber, String contactRegexNumber){
        this._contactName = contactName;
        this._companyName = companyName;
        this._contactDisplayNumber = contactDisplayNumber;
        this._contactRegexNumber = contactRegexNumber;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_contactName() {
        return _contactName;
    }

    public void set_contactName(String _contactName) {
        this._contactName = _contactName;
    }

    public String get_companyName() {
        return _companyName;
    }

    public void set_companyName(String _companyName) {
        this._companyName = _companyName;
    }

    public String get_contactDisplayNumber() {
        return _contactDisplayNumber;
    }

    public void set_contactDisplayNumber(String _contactNumber) {
        this._contactDisplayNumber = _contactNumber;
    }

    public String get_contactRegexNumber() {
        return _contactRegexNumber;
    }

    public void set_contactRegexNumber(String _contactRegexNumber) {
        this._contactRegexNumber = _contactRegexNumber;
    }

}
