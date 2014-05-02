//package com.hdik.main.db;
//
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.ArrayList;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.SQLException;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.util.Log;
//import android.util.SparseArray;
//
//import com.hdik.accountmanager.models.BeanAccount;
//import com.hdik.accountmanager.models.BeanUser;
//import com.hdik.accountmanager.utils.Const;
//
//public class ExternalDbOpenHelper extends SQLiteOpenHelper {
//
//	// Path to the device folder with databases
//	public static String DB_PATH;
//
//	// Database file name
//	public static String DB_NAME;
//	public SQLiteDatabase database;
//	public final Context context;
//
//	public SQLiteDatabase getDb() {
//		return database;
//	}
//
//	public ExternalDbOpenHelper(Context context, String databaseName,
//			int databaseVersion) {
//		super(context, databaseName, null, databaseVersion);
//		this.context = context;
//		// Write a full path to the databases of your application
//		String packageName = context.getPackageName();
//		DB_PATH = String.format("//data//data//%s//databases//", packageName);
//		DB_NAME = databaseName;
//		openDataBase();
//	}
//
//	// This piece of code will create a database if it�s not yet created
//	public void createDataBase() {
//		boolean dbExist = checkDataBase();
//		if (!dbExist) {
//
//			this.getReadableDatabase();
//			try {
//				copyDataBase();
//			} catch (IOException e) {
//				Log.e(this.getClass().toString(), "Copying error");
//				throw new Error("Error copying database!");
//			}
//		} else {
//			Log.i(this.getClass().toString(), "Database already exists");
//		}
//	}
//
//	// Performing a database existence check
//	private boolean checkDataBase() {
//		SQLiteDatabase checkDb = null;
//		try {
//			String path = DB_PATH + DB_NAME;
//			checkDb = SQLiteDatabase.openDatabase(path, null,
//					SQLiteDatabase.OPEN_READONLY);
//		} catch (SQLException e) {
//			Log.e(this.getClass().toString(), "Error while checking db");
//		}
//		// Android doesn�t like resource leaks, everything should
//		// be closed
//		if (checkDb != null) {
//			checkDb.close();
//		}
//		return checkDb != null;
//	}
//
//	// Method for copying the database
//	private void copyDataBase() throws IOException {
//		// Open a stream for reading from our ready-made database
//		// The stream source is located in the assets
//		InputStream externalDbStream = context.getAssets().open(DB_NAME);
//
//		// Path to the created empty database on your Android device
//		String outFileName = DB_PATH + DB_NAME;
//
//		// Now create a stream for writing the database byte by byte
//		OutputStream localDbStream = new FileOutputStream(outFileName);
//
//		// Copying the database
//		byte[] buffer = new byte[1024];
//		int bytesRead;
//		while ((bytesRead = externalDbStream.read(buffer)) > 0) {
//			localDbStream.write(buffer, 0, bytesRead);
//		}
//		// Don�t forget to close the streams
//		localDbStream.close();
//		externalDbStream.close();
//	}
//
//	public SQLiteDatabase openDataBase() throws SQLException {
//		String path = DB_PATH + DB_NAME;
//		if (database == null) {
//			createDataBase();
//		}
//		database = SQLiteDatabase.openDatabase(path, null,
//				SQLiteDatabase.OPEN_READWRITE);
//		return database;
//	}
//
//	@Override
//	public synchronized void close() {
//		if (database != null) {
//			database.close();
//		}
//		super.close();
//	}
//
//	@Override
//	public void onCreate(SQLiteDatabase db) {
//	}
//
//	@Override
//	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//	}
//
//	// adding User
//	public int addUser(BeanUser user) {
//		ContentValues values = new ContentValues();
//		values.put(Const.UserCol.UNAME, user.getUserName());
//		values.put(Const.UserCol.UPASS, user.getUserPass());
//		values.put(Const.UserCol.UFNAME, user.getUserFirstName());
//		values.put(Const.UserCol.ULNAME, user.getUserLastName());
//		values.put(Const.UserCol.USTATE, user.getUserState());
//		values.put(Const.UserCol.UCITY, user.getUserCity());
//		values.put(Const.UserCol.UPHONE, user.getUserPhone());
//		values.put(Const.UserCol.UZIP, user.getUserZip());
//		values.put(Const.UserCol.UEMAIL, user.getUserEmail());
//
//		// Inserting Row
//		int id = (int) database.insert(Const.TABLE_USERS_MASTER, null, values);
//
//		return id;
//	}
//
//	// Getting Single User
//	public BeanUser getUser(String id) {
//		Cursor cursor = database.query(Const.TABLE_USERS_MASTER,
//				new String[] { Const.UserCol.UID, Const.UserCol.UNAME,
//						Const.UserCol.UPASS, Const.UserCol.UFNAME,
//						Const.UserCol.ULNAME, Const.UserCol.UEMAIL,
//						Const.UserCol.USTATE, Const.UserCol.UCITY,
//						Const.UserCol.UPHONE, Const.UserCol.UZIP },
//				Const.UserCol.UID + "=?", new String[] { String.valueOf(id) },
//				null, null, null, null);
//		if (cursor != null)
//			cursor.moveToFirst();
//		BeanUser user = new BeanUser();
//		user.setUserId(cursor.getInt(0));
//		user.setUserName(cursor.getString(1));
//		user.setUserPass(cursor.getString(2));
//		user.setUserFirstName(cursor.getString(3));
//		user.setUserLastName(cursor.getString(4));
//		user.setUserEmail(cursor.getString(5));
//		user.setUserState(cursor.getString(6));
//		user.setUserCity(cursor.getString(7));
//		user.setUserPhone(cursor.getString(8));
//		user.setUserZip(cursor.getString(9));
//
//		return user;
//
//	}
//
//	// Getting User List
//	public ArrayList<BeanUser> getUsers() {
//		ArrayList<BeanUser> userList = new ArrayList<BeanUser>();
//		String selectQuery = "SELECT  * FROM " + Const.TABLE_USERS_MASTER;
//		try {
//
//			Cursor cursor = database.rawQuery(selectQuery, null);
//			if (cursor != null && cursor.getCount() > 0) {
//				if (cursor.moveToFirst()) {
//					do {
//						BeanUser user = new BeanUser();
//
//						user.setUserId(cursor.getInt(0));
//						user.setUserName(cursor.getString(1));
//						user.setUserPass(cursor.getString(2));
//						user.setUserFirstName(cursor.getString(3));
//						user.setUserLastName(cursor.getString(4));
//						user.setUserEmail(cursor.getString(5));
//						user.setUserCity(cursor.getString(6));
//						user.setUserState(cursor.getString(7));
//						user.setUserPhone(cursor.getString(8));
//						user.setUserZip(cursor.getString(9));
//						userList.add(user);
//					} while (cursor.moveToNext());
//				}
//				cursor.close();
//			}
//
//			return userList;
//		} catch (Exception e) {
//			Log.d("Error in getting users from DB", e.getMessage());
//			return null;
//		}
//	}
//
//	// Getting Account Group List
//	public SparseArray<String> getAcGroupList() {
//
//		SparseArray<String> sArray = new SparseArray<String>();
//		String selectQuery = "SELECT  * FROM " + Const.TABLE_H_MASTER;
//		try {
//			Cursor cursor = database.rawQuery(selectQuery, null);
//			if (cursor != null && cursor.getCount() > 0) {
//				if (cursor.moveToFirst()) {
//					do {
//						sArray.put(cursor.getInt(0), cursor.getString(1));
//					} while (cursor.moveToNext());
//				}
//				cursor.close();
//			}
//			return sArray;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//
//	}
//
//	public int addAccount(BeanAccount beanAccount) {
//
//		ContentValues values = new ContentValues();
//		values.put(Const.AccountCol.UID, beanAccount.getUserId());
//		values.put(Const.AccountCol.HID, beanAccount.getHeaderId());
//		values.put(Const.AccountCol.ANAME, beanAccount.getAcName());
//		values.put(Const.AccountCol.AOBAL, beanAccount.getAcOpenBal());
//		values.put(Const.AccountCol.ACBAL, beanAccount.getAcCurrentBal());
//		values.put(Const.AccountCol.ATYPE, Const.TYPE_SECONDRY);
//		final int hid = beanAccount.getHeaderId();
//		switch (hid) {
//		case Const.HEADER_CASH:
//		case Const.HEADER_EXPENSE:
//		case Const.HEADER_INCOME:
//		case Const.HEADER_OTHER:
//
//			break;
//		default:
//			values.put(Const.AccountCol.AADD, beanAccount.getAcAdd());
//			values.put(Const.AccountCol.ACITY, beanAccount.getAcCity());
//			values.put(Const.AccountCol.ASTATE, beanAccount.getAcState());
//			values.put(Const.AccountCol.AZIP, beanAccount.getAcZip());
//			values.put(Const.AccountCol.AMOBILE, beanAccount.getAcPhone());
//			values.put(Const.AccountCol.AEMAIL, beanAccount.getAcEmail());
//
//			break;
//		}
//		// Inserting Row
//		int id = (int) database
//				.insert(Const.TABLE_ACCOUNT_MASTER, null, values);
//		return id;
//	}
//
//	public int editAccount(BeanAccount beanAccount) {
//
//		ContentValues values = new ContentValues();
//		values.put(Const.AccountCol.HID, beanAccount.getHeaderId());
//		values.put(Const.AccountCol.ANAME, beanAccount.getAcName());
//		values.put(Const.AccountCol.AOBAL, beanAccount.getAcOpenBal());
//		values.put(Const.AccountCol.ACBAL, beanAccount.getAcCurrentBal());
//		final int hid = beanAccount.getHeaderId();
//		switch (hid) {
//		case Const.HEADER_CASH:
//		case Const.HEADER_EXPENSE:
//		case Const.HEADER_INCOME:
//		case Const.HEADER_OTHER:
//
//			break;
//		default:
//			values.put(Const.AccountCol.AADD, beanAccount.getAcAdd());
//			values.put(Const.AccountCol.ACITY, beanAccount.getAcCity());
//			values.put(Const.AccountCol.ASTATE, beanAccount.getAcState());
//			values.put(Const.AccountCol.AZIP, beanAccount.getAcZip());
//			values.put(Const.AccountCol.AMOBILE, beanAccount.getAcPhone());
//			values.put(Const.AccountCol.AEMAIL, beanAccount.getAcEmail());
//			System.out.println("======>" + beanAccount.getAcId());
//			break;
//		}
//		// upadate Row
//
//		int id = (int) database.update(Const.TABLE_ACCOUNT_MASTER, values,
//				Const.AccountCol.AID + " = ?",
//				new String[] { String.valueOf(beanAccount.getAcId()) });
//		return id;
//	}
//
//	// Getting Account List
//	public ArrayList<BeanAccount> getAccountList(final int uid,
//			final ArrayList<BeanAccount> accountList) {
//		String selectQuery = "SELECT  * FROM " + Const.TABLE_ACCOUNT_MASTER
//				+ " WHERE " + Const.AccountCol.UID + "=" + uid;
//		BeanAccount beanAccount;
//
//		try {
//			Cursor cursor = database.rawQuery(selectQuery, null);
//			if (cursor != null && cursor.getCount() > 0) {
//				if (cursor.moveToFirst()) {
//					do {
//						beanAccount = new BeanAccount();
//						beanAccount.setUserId(cursor.getInt(0));
//						beanAccount.setHeaderId(cursor.getInt(1));
//						beanAccount.setAcId(cursor.getLong(2));
//						beanAccount.setAcName(cursor.getString(3));
//						beanAccount.setAcAdd(cursor.getString(4));
//						beanAccount.setAcCity(cursor.getString(5));
//						beanAccount.setAcState(cursor.getString(6));
//						beanAccount.setAcZip(cursor.getString(7));
//						beanAccount.setAcPhone(cursor.getString(8));
//						beanAccount.setAcEmail(cursor.getString(9));
//						beanAccount.setAcOpenBal(cursor.getDouble(10));
//						beanAccount.setAcCurrentBal(cursor.getDouble(11));
//						beanAccount.setAcType(cursor.getString(12));
//						accountList.add(beanAccount);
//
//					} while (cursor.moveToNext());
//				}
//				cursor.close();
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//
//		}
//		return accountList;
//	}
//
//	public BeanAccount getAccount(final int uid, final long aid) {
//		String selectQuery = "SELECT  * FROM " + Const.TABLE_ACCOUNT_MASTER
//				+ " WHERE " + Const.AccountCol.UID + "=" + uid + " AND "
//				+ Const.AccountCol.AID + "=" + aid;
//		BeanAccount beanAccount = null;
//
//		try {
//			Cursor cursor = database.rawQuery(selectQuery, null);
//			if (cursor != null && cursor.getCount() > 0) {
//				if (cursor.moveToFirst()) {
//					beanAccount = new BeanAccount();
//					beanAccount.setUserId(cursor.getInt(0));
//					beanAccount.setHeaderId(cursor.getInt(1));
//					beanAccount.setAcId(cursor.getLong(2));
//					beanAccount.setAcName(cursor.getString(3));
//					beanAccount.setAcAdd(cursor.getString(4));
//					beanAccount.setAcCity(cursor.getString(5));
//					beanAccount.setAcState(cursor.getString(6));
//					beanAccount.setAcZip(cursor.getString(7));
//					beanAccount.setAcPhone(cursor.getString(8));
//					beanAccount.setAcEmail(cursor.getString(9));
//					beanAccount.setAcOpenBal(cursor.getDouble(10));
//					beanAccount.setAcCurrentBal(cursor.getDouble(11));
//					beanAccount.setAcType(cursor.getString(12));
//
//				}
//				cursor.close();
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//
//		}
//		return beanAccount;
//	}
//
//	public ArrayList<BeanAccount> getAccountListFromHeader(final int headerId,
//			final int uid, final ArrayList<BeanAccount> accountList) {
//		String selectQuery = "SELECT  * FROM " + Const.TABLE_ACCOUNT_MASTER
//				+ " WHERE " + Const.AccountCol.UID + "=" + uid + " AND "
//				+ Const.AccountCol.HID + "=" + headerId;
//		BeanAccount beanAccount;
//
//		try {
//			Cursor cursor = database.rawQuery(selectQuery, null);
//			if (cursor != null && cursor.getCount() > 0) {
//				if (cursor.moveToFirst()) {
//					do {
//						beanAccount = new BeanAccount();
//						beanAccount.setUserId(cursor.getInt(0));
//						beanAccount.setHeaderId(cursor.getInt(1));
//						beanAccount.setAcId(cursor.getLong(2));
//						beanAccount.setAcName(cursor.getString(3));
//						beanAccount.setAcAdd(cursor.getString(4));
//						beanAccount.setAcCity(cursor.getString(5));
//						beanAccount.setAcState(cursor.getString(6));
//						beanAccount.setAcZip(cursor.getString(7));
//						beanAccount.setAcPhone(cursor.getString(8));
//						beanAccount.setAcEmail(cursor.getString(9));
//						beanAccount.setAcOpenBal(cursor.getDouble(10));
//						beanAccount.setAcCurrentBal(cursor.getDouble(11));
//						beanAccount.setAcType(cursor.getString(12));
//						accountList.add(beanAccount);
//
//					} while (cursor.moveToNext());
//				}
//				cursor.close();
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//
//		}
//		return accountList;
//	}
//
//	public boolean createInitialAccounts(final int uid) {
//		final String acNames[] = { "Cash A/c", "Profit & Loss A/c" };
//		final int headers[] = { Const.HEADER_CASH, Const.HEADER_PRIMARY };
//		final int ids[] = { -1, -1 };
//		final double bal = 0;
//		for (int i = 0; i < acNames.length; i++) {
//			ContentValues values = new ContentValues();
//			values.put(Const.AccountCol.UID, uid);
//			values.put(Const.AccountCol.HID, headers[i]);
//			values.put(Const.AccountCol.ANAME, acNames[i]);
//			values.put(Const.AccountCol.ATYPE, Const.TYPE_PRIMARY);
//			values.put(Const.AccountCol.AOBAL, bal);
//			values.put(Const.AccountCol.ACBAL, bal);
//			ids[i] = (int) database.insert(Const.TABLE_ACCOUNT_MASTER, null,
//					values);
//		}
//		if (ids[0] != -1 && ids[1] != -1) {
//			return true;
//		}
//		return false;
//
//	}
//
//	public int getUserId(String userName) {
//		String selectQuery = "SELECT uid FROM " + Const.TABLE_USERS_MASTER
//				+ " WHERE uname =" + "'" + userName + "'";
//		try {
//			Cursor cursor = database.rawQuery(selectQuery, null);
//			if (cursor != null && cursor.getCount() > 0) {
//				if (cursor.moveToFirst()) {
//					return cursor.getInt(0);
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return -1;
//	}
//}