package jordan_jefferson.com.oncallphonemanager;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "contacts")
public class Contact implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long _id;
    private String _contactName;
    private String _companyName;
    private String _contactDisplayNumber;
    private String _contactRegexNumber;


    Contact() { }

    Contact(String contactName, String companyName, String contactDisplayNumber, String contactRegexNumber){
        this._contactName = contactName;
        this._companyName = companyName;
        this._contactDisplayNumber = contactDisplayNumber;
        this._contactRegexNumber = contactRegexNumber;
    }

    protected Contact(Parcel in) {
        _id = in.readLong();
        _contactName = in.readString();
        _companyName = in.readString();
        _contactDisplayNumber = in.readString();
        _contactRegexNumber = in.readString();
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(_id);
        dest.writeString(_contactName);
        dest.writeString(_companyName);
        dest.writeString(_contactDisplayNumber);
        dest.writeString(_contactRegexNumber);
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) { this._id = _id; }

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
