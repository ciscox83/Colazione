package com.ldc.bananamuffin;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

public class BreakfastContact {

    private final Context context;
    private final ContentResolver contentResolver;

    public BreakfastContact(Context context) {
        this.context = context;
        this.contentResolver = context.getContentResolver();
    }

    public void get() {
        Cursor cursor = getContactsCursor();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = getId(cursor);
                String name = getName(cursor);

            }
        }
    }

    private final static Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;

    private Cursor getContactsCursor() {
        return contentResolver.query(CONTENT_URI, null, null, null, null);
    }

    private final static String ID = ContactsContract.Contacts._ID;

    private String getId(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndex(ID));
    }

    private final static String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;

    private String getName(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
    }

    private final static Uri EMAIL_CONTENT_URI =  ContactsContract.CommonDataKinds.Email.CONTENT_URI;
    String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
    String DATA = ContactsContract.CommonDataKinds.Email.DATA;


    private Cursor getEmailCursor(String contactId) {
        return contentResolver.query(EMAIL_CONTENT_URI, null, null, new String[] { contactId }, null);
    }

}
