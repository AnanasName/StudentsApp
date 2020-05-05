package data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import model.Student;
import utils.Util;

public class DatabaseHandler extends SQLiteOpenHelper {

    public DatabaseHandler(@Nullable Context context) {
        super(context, Util.DATABASE_NAME,null, Util.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_STUDENTS_TABlE = "CREATE TABLE " + Util.TABLE_NAME + "("
                + Util.KEY_ID + " INTEGER PRIMARY KEY,"
                + Util.KEY_FACULTY + " TEXT,"
                + Util.KEY_FIRST_NAME + " TEXT,"
                + Util.KEY_LAST_NAME + " TEXT,"
                + Util.KEY_AVERAGE_SCORE + " REAL,"
                + Util.KEY_IMAGE + " BLOB" +")";
        db.execSQL(CREATE_STUDENTS_TABlE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Util.TABLE_NAME);
        onCreate(db);
    }

    public void addStudents(Student student){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        putStudent(contentValues, student);

        db.insert(Util.TABLE_NAME, null, contentValues);
        db.close();
    }

    public Student getStudent(int id){
        Student student = new Student();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Util.TABLE_NAME, new String[]{Util.KEY_ID,
        Util.KEY_FACULTY, Util.KEY_FIRST_NAME, Util.KEY_LAST_NAME, Util.KEY_AVERAGE_SCORE,
        Util.KEY_IMAGE}, Util.KEY_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null, null);

        if (cursor != null){
            cursor.moveToFirst();
        }

        if (cursor != null){
            try{
                setToStudent(student, cursor);
            }catch (NullPointerException exp){
                Log.d("Null", "NullPointer");
            }
        }
        return student;
    }

    public List<Student> getAllStudents(){
        SQLiteDatabase db = this.getReadableDatabase();

        List<Student> studentList = new ArrayList<>();

        String SELECT_ALL_STUDENTS = "SELECT * FROM " + Util.TABLE_NAME;
        Cursor cursor = db.rawQuery(SELECT_ALL_STUDENTS, null);
        if (cursor.moveToFirst()){
            do{
                Student student = new Student();
                setToStudent(student, cursor);

                studentList.add(student);
            }while(cursor.moveToNext());
        }
        return studentList;
    }

    public int updateStudent(Student student){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        putStudent(contentValues, student);

        return db.update(Util.TABLE_NAME, contentValues,
                Util.KEY_ID + "=?", new String[]{String.valueOf(student.getId())});
    }

    public void deleteStudent(Student student){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(Util.TABLE_NAME, Util.KEY_ID + "=?", new String[]{String.valueOf(student.getId())});
        db.close();
    }

    public int getStudentsCount(){
        SQLiteDatabase db = this.getReadableDatabase();

        String COUNT_QUERY = "SELECT * FROM " + Util.TABLE_NAME;
        Cursor cursor = db.rawQuery(COUNT_QUERY, null);

        return cursor.getCount();
    }

    private void putStudent(ContentValues contentValues, Student student ){
        contentValues.put(Util.KEY_FACULTY, student.getFaculty());
        contentValues.put(Util.KEY_FIRST_NAME, student.getFirstName());
        contentValues.put(Util.KEY_LAST_NAME, student.getLastName());
        contentValues.put(Util.KEY_AVERAGE_SCORE, student.getAverageScore());
        contentValues.put(Util.KEY_IMAGE, student.getImage());
    }

    private void setToStudent(Student student, Cursor cursor){
        student.setId(Integer.parseInt(cursor.getString(0)));
        student.setFaculty(cursor.getString(1));
        student.setLastName(cursor.getString(2));
        student.setFirstName(cursor.getString(3));
        student.setAverageScore(Double.parseDouble(cursor.getString(4)));
        student.setImage(cursor.getBlob(5));
    }
}

