package fr.isen.m1.devlogiciel.projetdevmobile.services

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import fr.isen.m1.devlogiciel.projetdevmobile.model.MountainModel
import fr.isen.m1.devlogiciel.projetdevmobile.model.MountainsModel

class MountainDatabaseService {
    private val database = FirebaseDatabase.getInstance()

    val mountainsReference = database.getReference("remontee")

    fun getMountains(): LiveData<MountainsModel> {
        return object : LiveData<MountainsModel>() {
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val mountainList = ArrayList<MountainModel>()
                    for (mountain in snapshot.children) {
                        val mountainItem = mountain.getValue(MountainModel::class.java)
                        mountainItem?.let { mountainList.add(it) }
                    }
                    value = MountainsModel(mountainList)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("MountainDatabaseService", "Error getting data", error.toException())
                }
            }

            override fun onActive() {
                mountainsReference.addValueEventListener(listener)
            }

            override fun onInactive() {
                mountainsReference.removeEventListener(listener)
            }
        }
    }
}