package com.hdik.main.db.helper;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.hdik.main.db.bean.Employee;

public class DatabaseHandler extends SQLiteOpenHelper {

	// Database Version
	private static final int DATABASE_VERSION = 1;
	// Database Name
	private static final String DATABASE_NAME = "dbEmp";
	// Employee table name
	private static final String TABLE_EMPLOYEE = "employee";

	private static String EMP_ID = "emp_id";
	private static String NAME = "name";
	private static String PHONE = "phone";
	private static String ADDRESS = "address";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_EMPLOYEE + "("
				+ EMP_ID + " INTEGER PRIMARY KEY," + NAME + " TEXT," + PHONE
				+ " TEXT," + ADDRESS + " TEXT" + ")";
		db.execSQL(CREATE_USER_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEE);
		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */
	// Adding new employee
	public int addEmployee(Employee emp) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(NAME, emp.getName());
		values.put(ADDRESS, emp.getAddress());
		values.put(PHONE, emp.getPhone());
		// Inserting Row
		int id = (int) db.insert(TABLE_EMPLOYEE, null, values);
		db.close(); // Closing database connection
		return id;
	}

	// Getting Single Employee
	public Employee getEmployee(String id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_EMPLOYEE, new String[] { EMP_ID, NAME,
				ADDRESS, PHONE }, EMP_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Employee emp = new Employee(cursor.getString(1), cursor.getString(2),
				cursor.getString(3));
		db.close();

		return emp;

	}

	// Getting list of All Employee
	public ArrayList<Employee> getEmployees() {
		ArrayList<Employee> empList = new ArrayList<Employee>();
		String selectQuery = "SELECT  * FROM " + TABLE_EMPLOYEE;
		try {
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					Employee emp = new Employee();
					emp.setEmpId(Integer.parseInt(cursor.getString(0)));
					emp.setName(cursor.getString(1));
					emp.setPhone(cursor.getString(2));
					emp.setAddress(cursor.getString(3));
					empList.add(emp);
				} while (cursor.moveToNext());
			}
			cursor.close();
			db.close();
			return empList;
		} catch (Exception e) {
			Log.d("Error in getting users from DB", e.getMessage());
			return null;
		}
	}

	// updating a existing employee
	public int updateEmployee(Employee emp, String position) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(NAME, emp.getName());
		values.put(PHONE, emp.getPhone());
		values.put(ADDRESS, emp.getAddress());
		// updating row
		return db.update(TABLE_EMPLOYEE, values, EMP_ID + " = ?",
				new String[] { String.valueOf(position) });
		
	}

	// Deleting single Employee
	public void deleteEmployee(Employee emp) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_EMPLOYEE, EMP_ID + " = ?",
				new String[] { String.valueOf(emp.getEmpId()) });
		db.close();
	}

	// Getting No Of Employee
	public int getCountEmployee() {
		String countQuery = "SELECT  * FROM " + TABLE_EMPLOYEE;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();
		db.close();

		// return count
		return cursor.getCount();
	}

}
