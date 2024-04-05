package fr.isen.m1.devlogiciel.projetdevmobile.services

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import fr.isen.m1.devlogiciel.projetdevmobile.model.ChatModel
import fr.isen.m1.devlogiciel.projetdevmobile.model.SlopeModel
import fr.isen.m1.devlogiciel.projetdevmobile.model.SlopesModel

class SlopeDatabaseService {
    private val database = FirebaseDatabase.getInstance()

    val slopesReference = database.getReference("piste")

    fun getSlopes(): LiveData<SlopesModel> {
        return object : LiveData<SlopesModel>() {
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val slopeList = ArrayList<SlopeModel>()
                    for (slope in snapshot.children) {
                        val slopeItem = slope.getValue(SlopeModel::class.java)
                        slopeItem?.let { slopeList.add(it) }
                    }
                    value = SlopesModel(slopeList)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("SlopeDatabaseService", "Error getting data", error.toException())
                }
            }

            override fun onActive() {
                slopesReference.addValueEventListener(listener)
            }

            override fun onInactive() {
                slopesReference.removeEventListener(listener)
            }
        }
    }

    fun getSlope(index: Int): LiveData<SlopeModel> {
        return object : LiveData<SlopeModel>() {
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    value = snapshot.children.elementAt(index).getValue(SlopeModel::class.java)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("MountainDatabaseService", "Error getting data", error.toException())
                }
            }

            override fun onActive() {
                slopesReference.addValueEventListener(listener)
            }

            override fun onInactive() {
                slopesReference.removeEventListener(listener)
            }
        }
    }

    fun sendStatus(status: Boolean, index: Int) {
        val ref = database.getReference("piste/$index/status")
        ref.setValue(status)
    }

    fun sendState(value: SlopeModel.Companion.SlopeStateEnum, index: Int) {
        val ref = database.getReference("piste/$index/state")
        ref.setValue(value)
    }

    fun sendComments(username: String, message: String, index: Int, id: Int) {
        val ref = database.getReference("piste/$index/comments/$id")
        ref.setValue(ChatModel(username, message))
    }

}