package fr.isen.m1.devlogiciel.projetdevmobile.samples

import android.util.Log
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fr.isen.m1.devlogiciel.projetdevmobile.model.SlopeModel
import fr.isen.m1.devlogiciel.projetdevmobile.model.SlopesModel

class ConnectionDatabaseSample {
    private val rf = Firebase.database.reference

    fun getPistesFromDatabase(): SlopesModel? {
        Log.d("ConnectionPistesSample", "Starting getPistesFromDatabase")
        var pistes: SlopesModel? = null
        rf.child("piste").get()
            .addOnSuccessListener {
                Log.d("ConnectionPistesSample","Got value ${it.value}")
                if (it.exists()) {
                    val pisteList = ArrayList<SlopeModel>()
                    for (piste in it.children) {
                        val pisteItem = piste.getValue(SlopeModel::class.java)
                        Log.d("ConnectionPistesSample", pisteItem.toString())
                        pisteItem?.let { it1 -> pisteList.add(it1) }
                    }
                    pistes = SlopesModel(pisteList)
                    Log.d("ConnectionPistesSample", "Size: " + pistes?.pistes?.size.toString())
                }
            }
            .addOnFailureListener {
                Log.e("ConnectionPistesSample","Error getting data")
            }
        Log.d("ConnectionPistesSample", "Ending getPistesFromDatabase")
        return pistes
    }
}