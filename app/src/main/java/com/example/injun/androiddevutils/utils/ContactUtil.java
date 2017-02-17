package com.example.injun.androiddevutils.utils;

/**
 * Created by injun on 2017/2/17.
 */


import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

import java.io.Serializable;
import java.util.concurrent.Executors;

/**
 * 功能：获取联系人信息；
 *
 * @author:gengqiyun
 * @date: 2016/6/15
 */
public class ContactUtil
{

    private static final int REQUEST_CODE_ASK_PERMISSIONS = 124;

    /**
     * 开启通讯录
     *
     * @author ysg
     * created at 2016/6/22 20:21
     */
    public static void startContactsActivity(Activity activity, int requestCode)
    {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        activity.startActivityForResult(intent, requestCode);
    }


    /**
     * 在onActivityResult中调用此方法，New一个Connector，传进来，然后通过Connector获取姓名和手机号码
     *
     * @author ysg
     * created at 2016/6/22 19:31
     */
    public static void getConnector(Activity activity, Intent data, Connector connector)
    {
        if (data == null) return;
        //sdk23以后需要用户自己开启权限
        int hasWriteContactsPermission
                = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_CONTACTS);

        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_CONTACTS},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;

        }
        Intent query = new Intent(Intent.ACTION_MAIN);
        query.addCategory("android.intent.category.LAUNCHER");
        Uri contactData = data.getData();
        Cursor cursor = activity.managedQuery(contactData, null, null, null, null);
        int contactIdIndex = 0;
        if (cursor.getCount() > 0)
        {
            contactIdIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
        }
        if (cursor.moveToFirst())
        {
            String contactId = cursor.getString(contactIdIndex);
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
            String phoneNumber = null;
            if (hasPhone.equalsIgnoreCase("1"))
            {
                Cursor phones = activity.getContentResolver().
                        query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " +
                                        contactId, null, null);
                while (phones.moveToNext())
                {
                    phoneNumber = phones.getString(
                            phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    phoneNumber = phoneNumber.replaceAll("-", "");
                    phoneNumber = phoneNumber.replaceAll(" ", "");
                    if (phoneNumber != null && phoneNumber.length() > 11)
                        phoneNumber = phoneNumber.substring(phoneNumber.length() - 11,
                                phoneNumber.length());
                    if (!RegexUtil.validPhoneNum(phoneNumber))
                    {
                        return;
                    }
                    connector.nickName = name;
                    connector.mobile = phoneNumber;
                    break;
                }
                phones.close();
            }
        }
    }

    /**
     * @param uri 某个联系人的uri；
     */
    public static void getPhoneContact(final Context context, final Uri uri, final IContactCallBack callBack)
    {
        if (context == null || uri == null)
        {
            if (callBack != null)
            {
                callBack.getContact(null);
            }
            return;
        }

        new AsyncTask<Void, Void, ContactLocal>()
        {

            @Override
            protected ContactLocal doInBackground(Void... params)
            {
                ContentResolver cr = context.getContentResolver();
                //取得电话本中开始一项的光标
                Cursor cursor = cr.query(uri, null, null, null, null);

                if (cursor != null && cursor.getCount() == 1)
                {
                    ContactLocal contactLocal = new ContactLocal();

                    cursor.moveToFirst();
                    //取得联系人姓名
                    int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                    contactLocal.name = cursor.getString(nameFieldColumnIndex);

                    //取得联系人id；
                    String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    //根据id查询电话号码；
                    Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);
                    if (phone != null && phone.getCount() == 1)
                    {
                        phone.moveToFirst();
                        contactLocal.phone = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        if (!TextUtils.isEmpty(contactLocal.phone))
                        {
                            // 字符替换；
                            if (contactLocal.phone.contains("-"))
                            {
                                contactLocal.phone = contactLocal.phone.replace("-", "");
                            }
                            if (contactLocal.phone.contains(" "))
                            {
                                contactLocal.phone = contactLocal.phone.replace(" ", "");
                            }
                        }
                        phone.close();
                    }
                    cursor.close();
                    return contactLocal;
                }
                return null;
            }

            @Override
            protected void onPostExecute(ContactLocal contactLocal)
            {
                if (callBack != null)
                {
                    callBack.getContact(contactLocal);
                }
            }
        }.executeOnExecutor(Executors.newCachedThreadPool());
    }

    public interface IContactCallBack
    {
        void getContact(ContactLocal contactLocal);
    }

    public static class ContactLocal
    {
        public String name;
        public String phone;
    }
}


class Connector implements Serializable
{
    public String id; //联系人id；
    public String nickName; //联系人姓名；
    public String mobile; //电话；
    public String relationship = "";
    public String type = "";
    public boolean isDefault = false;

    public Connector(String id, String nickName, String mobile, String relationship, boolean isDefault)
    {
        this.id = id;
        this.nickName = nickName;
        this.mobile = mobile;
        this.relationship = relationship;
        this.isDefault = isDefault;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    public String getMobile()
    {
        return mobile;
    }

    public void setMobile(String mobile)
    {
        this.mobile = mobile;
    }

    public String getRelationship()
    {
        return relationship;
    }

    public void setRelationship(String relationship)
    {
        this.relationship = relationship;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public boolean isDefault()
    {
        return isDefault;
    }

    public void setIsDefault(boolean isDefault)
    {
        this.isDefault = isDefault;
    }

    public Connector()
    {

    }
}
