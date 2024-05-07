package com.example.mobiletechnologies

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.app.ui.dashboard.Exercise

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        Log.i("DatabaseHelper", "Creating database...")

        // Create exercise type table
        val createExerciseTypeTable = """
            CREATE TABLE $TABLE_EXERCISE_TYPE (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_TYPE_NAME TEXT UNIQUE
            )
        """.trimIndent()
        db?.execSQL(createExerciseTypeTable)

        // Insert initial data
        insertInitialExerciseTypes(db)

        // Create exercise record table
        val createExerciseRecordTable = """
            CREATE TABLE $TABLE_EXERCISE_RECORD (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_TYPE_ID INTEGER,
                $COL_DURATION INTEGER,
                FOREIGN KEY($COL_TYPE_ID) REFERENCES $TABLE_EXERCISE_TYPE($COL_ID)
            )
        """.trimIndent()
        db?.execSQL(createExerciseRecordTable)
    }
    fun deleteExerciseById(id: Int): Boolean {
        val db = writableDatabase
        val deletedRows = db.delete(TABLE_EXERCISE_RECORD, "$COL_ID=?", arrayOf(id.toString()))
        return deletedRows > 0
    }
    private fun insertInitialExerciseTypes(db: SQLiteDatabase?) {
        Log.i("DatabaseHelper", "Inserting initial exercise types...")
        val exerciseTypes = listOf("Running", "Swimming", "Cycling")
        for (type in exerciseTypes) {
            val values = ContentValues().apply {
                put(COL_TYPE_NAME, type)
            }
            val result = db?.insertWithOnConflict(TABLE_EXERCISE_TYPE, null, values, SQLiteDatabase.CONFLICT_IGNORE)
            if (result == -1L) {
                Log.e("DatabaseHelper", "Failed to insert type: $type")
            } else {
                Log.i("DatabaseHelper", "Inserted type: $type")
            }
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i("DatabaseHelper", "Upgrading database from $oldVersion to $newVersion...")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_EXERCISE_RECORD")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_EXERCISE_TYPE")
        onCreate(db)
    }


    fun getExercises(): List<Exercise> {
        val exercises = mutableListOf<Exercise>()
        val db = readableDatabase
        val query = """
            SELECT e.$COL_ID, et.$COL_TYPE_NAME, e.$COL_DURATION
            FROM $TABLE_EXERCISE_RECORD e
            JOIN $TABLE_EXERCISE_TYPE et ON e.$COL_TYPE_ID = et.$COL_ID
        """.trimIndent()
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COL_TYPE_NAME))
                val duration = cursor.getInt(cursor.getColumnIndexOrThrow(COL_DURATION))
                exercises.add(Exercise(name, duration, id))
            } while (cursor.moveToNext())
        } else {
            Log.e("DatabaseHelper", "No exercise records found")
        }
        cursor.close()
        return exercises
    }

    companion object {
        private const val DATABASE_NAME = "exercise.db"
        private const val DATABASE_VERSION = 8
        const val TABLE_EXERCISE_TYPE = "ExerciseType"
        const val TABLE_EXERCISE_RECORD = "ExerciseRecord"
        const val COL_ID = "id"
        const val COL_TYPE_NAME = "typeName"
        const val COL_TYPE_ID = "typeId"
        const val COL_DURATION = "duration"
    }
}