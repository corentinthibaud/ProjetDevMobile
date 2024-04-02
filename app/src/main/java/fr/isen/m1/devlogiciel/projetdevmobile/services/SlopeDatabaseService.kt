package fr.isen.m1.devlogiciel.projetdevmobile.services

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
                    Log.e("ConnectionPistesSample", "Error getting data", error.toException())
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

}