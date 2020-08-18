package com.sloupycom.shaper.model

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking
import kotlin.collections.HashMap

class Repo {
    /** Values **/
    private val TAG = "REPO"
    private val COLLECTION_USERS = "users"
    private val SUBCOLLECTION_TASKS = "tasks"
    private val mDatabase = Firebase.firestore
    val mUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    init {
        val user = hashMapOf(
            "email" to mUser!!.email,
            "gid" to mUser.uid,
            "last_name" to mUser.displayName,
            "name" to mUser.displayName
        )
        mDatabase.collection(COLLECTION_USERS).document(mUser.uid).update(user as Map<String, Any>)
    }

    /**
     * Get tasks that are due and overdue
     */
    fun getDueTasks(listener: OnDataChanged) {
        runBlocking {
            mDatabase.collection(COLLECTION_USERS)
                .document(mUser!!.uid)
                .collection(SUBCOLLECTION_TASKS)
                .whereIn("state", listOf("DUE","OVERDUE","DONE"))
                .orderBy("creation_date")
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        Log.w(TAG, "Listen failed.", error)
                        return@addSnapshotListener
                    }
                    listener.onDataChanged(getTasksFromSnapShot(value))
                }
        }
    }

    /**
     * Add task to database
     */
    fun addTask(task: HashMap<String, String>) {
        mDatabase.collection(COLLECTION_USERS)
            .document(mUser!!.uid)
            .collection(SUBCOLLECTION_TASKS)
            .document(task["id"].toString())
            .set(task)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    /**
     * Update a certain task
     */
    fun updateTask(task: Task) {
        mDatabase.collection(COLLECTION_USERS)
            .document(mUser!!.uid)
            .collection(SUBCOLLECTION_TASKS)
            .document(task.id)
            .set(task)
    }

    /**
     * Get tasks based on their due date
     */
    fun getDueTasksWithDate(day: String, month: String, year: String, listener: OnDataChanged) {
        runBlocking {
            mDatabase.collection(COLLECTION_USERS)
                .document(mUser!!.uid)
                .collection(SUBCOLLECTION_TASKS)
                .whereEqualTo("next_due_day", day)
                .whereEqualTo("next_due_month", month)
                .whereEqualTo("next_due_year", year)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        Log.w(TAG, "Listen failed.", error)
                        return@addSnapshotListener
                    }
                    listener.onDataChanged(getTasksFromSnapShot(value))
                }
        }
    }

    /**
     * Bind snapshot data to task objects
     */
    private fun getTasksFromSnapShot(value: QuerySnapshot?): ArrayList<Task> {
        val tasks = ArrayList<Task>()
        for (doc in value!!) {
            tasks.add(
                Task(
                    doc.get("creation_date") as String?,
                    doc.get("description") as String?,
                    doc.get("name") as String?,
                    doc.get("next_due_day") as String?,
                    doc.get("next_due_month") as String?,
                    doc.get("next_due_year") as String?,
                    doc.get("owner_id") as String?,
                    doc.get("reminder") as String?,
                    doc.get("repetition") as String?,
                    doc.get("state") as String?,
                    doc.get("id") as String
                )
            )
        }
        return tasks
    }

    interface OnDataChanged{
        fun onDataChanged(data: ArrayList<Task>)
    }

}