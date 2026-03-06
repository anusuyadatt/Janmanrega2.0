package nic.hp.ccmgnrega.model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;


public class Contact {
    protected List<Long> id = new ArrayList<>();
    protected List<String> mobile = new ArrayList<>();
    protected List<String> name = new ArrayList<>();
    private List<String> displayName = new ArrayList<>();
    private Context _activity;
    private static final String[] PROJECTION = new String[] {
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
    };

    public Contact(Context activity) {
        this._activity = activity;
        _init();
    }

    public List<Long> getID() {
        return this.id;
    }
    public List<String> getName() {
        return this.name;
    }
    public void setName(List<String> name) {
        this.name = name;
    }
    public List<String> getDiaplayName() {
        return this.displayName;
    }

    public List<String> getMobile() {
        return this.mobile;
    }

    public void setMobile(List<String> mobile) {
        this.mobile = mobile;
    }

    private void _init() {
        this.id.clear();
        this.mobile.clear();
        this.name.clear();
        this.displayName.clear();
        ContentResolver cr = this._activity.getContentResolver();
        Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PROJECTION, null, null, ContactsContract.Contacts.DISPLAY_NAME);
        if (cursor != null) {
            try {
                final int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                final int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                final int idIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
                String name, number;
                while (cursor.moveToNext()) {
                    name = cursor.getString(nameIndex);
                    number = cursor.getString(numberIndex);
                    if (number.length() >= 10) {
                        this.id.add(cursor.getLong(idIndex));
                        this.name.add(name);
                        this.mobile.add(number);
                        this.displayName.add(name + " (" + number + ")");
                    }
                }
            } finally {
                cursor.close();
            }
        }
    }


}