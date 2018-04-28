package com.lzy.androidlibrary.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 系统联系人获取实现.
 *
 * @author linzhiyong
 * @email wflinzhiyong@163.com
 * @time 2017/8/6
 * @desc
 */
public class LContactUtil {

    /**
     * 获取联系人信息
     *
     * @param context
     * @param contactData
     * @return
     * @throws JSONException
     */
    public static JSONObject getContact(Context context, Uri contactData) throws JSONException {
        ContentResolver contentResolver = context.getContentResolver();

        // 获取联系人姓名，id
        Cursor cursor = contentResolver.query(contactData, null, null, null, null);
        cursor.moveToFirst();
        String username = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
        cursor.close();

        // 根据id获取联系人电话
        Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                "_id = " + contactId,
                null,
                null,
                null);
        String phoneNumber = null;
        while (phoneCursor.moveToNext()) {
            phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }
        phoneCursor.close();

        JSONObject contactObj = new JSONObject();
        contactObj.put("username", username);
        contactObj.put("phoneNumber", phoneNumber);

        return contactObj;
    }

}
