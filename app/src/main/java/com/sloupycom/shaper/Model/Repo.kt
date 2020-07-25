package com.sloupycom.shaper.Model

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking
import kotlin.collections.HashMap

class Repo {
    private val TAG = "REPO"
    private val COLLECTION_USERS = "users"
    private val SUBCOLLECTION_TASKS = "tasks"

    val mDatabase = Firebase.firestore
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


    fun getTasks(listener: OnDataChanged) {
        val tasks = ArrayList<Task>()
        runBlocking {
            mDatabase.collection(COLLECTION_USERS)
                .document(mUser!!.uid)
                .collection(SUBCOLLECTION_TASKS)
                .addSnapshotListener { value, error ->
                    tasks.clear()

                    if (error != null) {
                        Log.w(TAG, "Listen failed.", error)
                        return@addSnapshotListener
                    }

                    for (doc in value!!) {
                        tasks.add(
                            Task(
                                doc.get("creation_date") as String?
                                , doc.get("description") as String?
                                , doc.get("name") as String?
                                , doc.get("next_due") as String?
                                , doc.get("owner_id") as String?
                                , doc.get("reminder") as String?
                                , doc.get("repetition") as String?
                                , doc.get("state") as String?
                            )
                        )
                    }
                    Log.d(TAG, "Current cites in CA: $tasks")
                    listener.onDataChanged(tasks)
                }
        }
    }

    fun write() {

        val data = hashMapOf(
            "creation_date" to "2TokyoDate",
            "description" to "2JapanDesc",
            "name" to "2JapanName",
            "next_due" to "2JapanNxtDue",
            "owner_id" to "2JapanOwId",
            "reminder" to "2JapanREmin",
            "repetition" to "2JapanREpi",
            "state" to "2JapanState"
        )

        val user = hashMapOf(
            "email" to "2test email",
                    "gid" to "2test gid",
                    "last_name" to "2test lname",
                    "name" to "2test name"
        )

        mDatabase.collection(COLLECTION_USERS)
            .document("testUser2")
            .collection(SUBCOLLECTION_TASKS)
            .add(data)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    fun addTask(task: HashMap<String, String>) {
        mDatabase.collection(COLLECTION_USERS)
            .document(mUser!!.uid)
            .collection(SUBCOLLECTION_TASKS)
            .add(task)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    interface OnDataChanged{
        fun onDataChanged(data: ArrayList<Task>)
    }

}