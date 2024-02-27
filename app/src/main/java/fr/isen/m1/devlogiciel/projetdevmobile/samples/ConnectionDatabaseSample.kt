package fr.isen.m1.devlogiciel.projetdevmobile.samples

import android.util.Log
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fr.isen.m1.devlogiciel.projetdevmobile.model.PisteModel
import fr.isen.m1.devlogiciel.projetdevmobile.model.PistesModel
import fr.isen.m1.devlogiciel.projetdevmobile.model.RemonteeModel
import fr.isen.m1.devlogiciel.projetdevmobile.model.RemonteesModel
import kotlinx.coroutines.tasks.await

class ConnectionDatabaseSample {
    private val rf = Firebase.database.reference

    suspend fun getPistesFromDatabase(): PistesModel? {
        Log.d("ConnectionPistesSample", "Starting getPistesFromDatabase")
        return try {
            val snapshot = rf.child("piste").get().await()
            if (snapshot.exists()) {
                val pisteList = ArrayList<PisteModel>()
                for (piste in snapshot.children) {
                    Log.d("ConnectionRemonteeSample", "Get value$piste")
                    val pisteItem = piste.getValue(PisteModel::class.java)
                    pisteItem?.let { pisteList.add(it) }
                }
                Log.d("ConnectionPistesSample", "Ending getPistesFromDatabase")
                PistesModel(pisteList)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("ConnectionPistesSample", "Error getting data", e)
            null
        }
    }

    suspend fun getRemonteeFromDatabase(): RemonteesModel? {
        Log.d("ConnectionRemonteeSample", "Starting getRemonteeFromDatabase")
        return try {
            val snapshot = rf.child("remontee").get().await()
            if (snapshot.exists()) {
                val remonteeList = ArrayList<RemonteeModel>()
                for (remontee in snapshot.children) {
                    Log.d("ConnectionRemonteeSample", "Get value$remontee")
                    val remonteeItem = remontee.getValue(RemonteeModel::class.java)
                    remonteeItem?.let { remonteeList.add(it) }
                }
                Log.d("ConnectionRemonteeSample", "Ending getRemonteeFromDatabase")
                RemonteesModel(remonteeList)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("ConnectionRemonteeSample", "Error getting data", e)
            null
        }
    }
}