package com.sloupycom.shaper.model

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class Repo {
    /** Values **/
    private val TAG = "REPO"
    private val COLLECTION_USERS = "users"
    private val SUBCOLLECTION_TASKS = "tasks"
    private val mDatabase = Firebase.firestore
    private val mUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private val todayIndex = listOf<Int>(
        SimpleDateFormat("dd").format(Calendar.getInstance().time).toInt(),
        SimpleDateFormat("MM").format(Calendar.getInstance().time).toInt(),
        SimpleDateFormat("yyyy").format(Calendar.getInstance().time).toInt()
    )

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
        mDatabase.collection(COLLECTION_USERS)
            .document(mUser!!.uid)
            .collection(SUBCOLLECTION_TASKS)
            .whereLessThanOrEqualTo("next_due_index", todayIndex)
            .whereIn("state", listOf("DUE","OVERDUE", "DONE"))
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.w(TAG, "Listen failed.", error)
                    return@addSnapshotListener
                }
                listener.onDataChanged(getTasksFromSnapShot(value))
            }
        runBlocking {
        }
    }

    /**
     * Add task to database
     */
    fun addTask(task: HashMap<String, Any>) {
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
    fun getDueTasksWithDate(day: Int, month: Int, year: Int, listener: OnDataChanged) {
        runBlocking {
            mDatabase.collection(COLLECTION_USERS)
                .document(mUser!!.uid)
                .collection(SUBCOLLECTION_TASKS)
                .whereEqualTo("next_due_index", listOf(day, month, year))
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
                    doc.get("owner_id") as String?,
                    doc.get("reminder") as String?,
                    doc.get("repetition") as String?,
                    doc.get("state") as String?,
                    doc.get("id") as String,
                    doc.get("next_due") as String?,
                    doc.get("next_due_index") as List<Int>?
                )
            )
        }
        return tasks
    }

    fun getUserCredentials(): FirebaseUser? {
        return mUser
    }

    interface OnDataChanged{
        fun onDataChanged(data: ArrayList<Task>)
    }

}