package fr.isen.m1.devlogiciel.projetdevmobile.samples

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import fr.isen.m1.devlogiciel.projetdevmobile.model.CommentModel
import fr.isen.m1.devlogiciel.projetdevmobile.model.MountainModel
import fr.isen.m1.devlogiciel.projetdevmobile.model.MountainsModel
import fr.isen.m1.devlogiciel.projetdevmobile.model.SlopeModel
import fr.isen.m1.devlogiciel.projetdevmobile.model.SlopesModel

class ConnectionDatabaseSample {
    private val database = FirebaseDatabase.getInstance()

    val slopesReference = database.getReference("piste")
    val mountainsReference = database.getReference("remontee")

    fun getSlopes(): LiveData<SlopesModel> {
        return object : LiveData<SlopesModel>() {
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val slopeList = ArrayList<SlopeModel>()
                    for (slope in snapshot.children) {
                        val slopeItem =  slope.getValue(SlopeModel::class.java)
                        val comments = ArrayList<CommentModel>()
                        slope.child("comments").children.forEach { commentSnapshot ->
                            val commentItem = commentSnapshot.getValue(CommentModel::class.java)
                            commentItem?.let { comments.add(it) }
                        }
                        slopeItem?.comments = comments
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

    fun getMountains(): LiveData<MountainsModel> {
        return object : LiveData<MountainsModel>() {
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val mountainList = ArrayList<MountainModel>()
                    for (mountain in snapshot.children) {
                        val mountainItem = mountain.getValue(MountainModel::class.java)
                        val comments = ArrayList<CommentModel>()
                        mountain.child("comments").children.forEach { commentSnapshot ->
                            val commentItem = commentSnapshot.getValue(CommentModel::class.java)
                            commentItem?.let { comments.add(it) }
                        }
                        mountainItem?.comments = comments
                        mountainItem?.let { mountainList.add(it) }
                    }
                    value = MountainsModel(mountainList)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("ConnectionPistesSample", "Error getting data", error.toException())
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

    fun sendStatus(status: Boolean, type: Boolean, index: Int) {
        val path: String = if(type) {
            "piste/$index/status"
        } else {
            "remontee/$index/status"
        }
        val ref = database.getReference(path)
        ref.setValue(status)
    }

    fun sendState(state: SlopeModel.Companion.SlopeStateEnum, index: Int) {
        val ref = database.getReference("piste/$index/state")
        ref.setValue(state)
    }

    //Index of slope or moutain and id = list.size()
    fun sendComments(userName: String, message: String, index: Int, type: Boolean, id: Int) {
        val path: String = if(type) {
            "piste/$index/comments/$id"
        } else {
            "remontee/$index/comments/$id"
        }
        val ref = database.getReference(path)
        ref.setValue(CommentModel(userName, message))
    }
}