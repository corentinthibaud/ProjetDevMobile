package fr.isen.m1.devlogiciel.projetdevmobile.samples

import android.util.Log
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fr.isen.m1.devlogiciel.projetdevmobile.model.PisteModel
import fr.isen.m1.devlogiciel.projetdevmobile.model.PistesModel

class ConnectionDatabaseSample {
    private val rf = Firebase.database.reference

    fun getPistesFromDatabase(): PistesModel? {
        Log.d("ConnectionPistesSample", "Starting getPistesFromDatabase")
        var pistes: PistesModel? = null
        rf.child("piste").get()
            .addOnSuccessListener {
                Log.d("ConnectionPistesSample","Got value ${it.value}")
                if (it.exists()) {
                    val pisteList = ArrayList<PisteModel>()
                    for (piste in it.children) {
                        val pisteItem = piste.getValue(PisteModel::class.java)
                        Log.d("ConnectionPistesSample", pisteItem.toString())
                        pisteItem?.let { it1 -> pisteList.add(it1) }
                    }
                    pistes = PistesModel(pisteList)
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