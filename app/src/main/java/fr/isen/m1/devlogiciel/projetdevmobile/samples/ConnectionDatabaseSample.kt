package fr.isen.m1.devlogiciel.projetdevmobile.samples

import android.content.Context
import android.util.Log
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import fr.isen.m1.devlogiciel.projetdevmobile.model.MountainModel
import fr.isen.m1.devlogiciel.projetdevmobile.model.MountainsModel
import fr.isen.m1.devlogiciel.projetdevmobile.model.SlopeModel
import fr.isen.m1.devlogiciel.projetdevmobile.model.SlopesModel
import kotlinx.coroutines.tasks.await
import java.io.File

class ConnectionDatabaseSample {
    private val rf = Firebase.database.reference
    suspend fun getPistesFromDatabase(): SlopesModel? {
        Log.d("ConnectionPistesSample", "Starting getPistesFromDatabase")
        return try {
            val snapshot = rf.child("piste").get().await()
            if (snapshot.exists()) {
                val pisteList = ArrayList<SlopeModel>()
                for (piste in snapshot.children) {
                    Log.d("ConnectionRemonteeSample", "Get value$piste")
                    val pisteItem = piste.getValue(SlopeModel::class.java)
                    pisteItem?.let { pisteList.add(it) }
                }
                Log.d("ConnectionPistesSample", "Ending getPistesFromDatabase")
                SlopesModel(pisteList)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("ConnectionPistesSample", "Error getting data", e)
            null
        }
    }

    suspend fun getRemonteeFromDatabase(): MountainsModel? {
        Log.d("ConnectionRemonteeSample", "Starting getRemonteeFromDatabase")
        return try {
            val snapshot = rf.child("remontee").get().await()
            if (snapshot.exists()) {
                val remonteeList = ArrayList<MountainModel>()
                for (remontee in snapshot.children) {
                    Log.d("ConnectionRemonteeSample", "Get value$remontee")
                    val remonteeItem = remontee.getValue(MountainModel::class.java)
                    remonteeItem?.let { remonteeList.add(it) }
                }
                Log.d("ConnectionRemonteeSample", "Ending getRemonteeFromDatabase")
                MountainsModel(remonteeList)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("ConnectionRemonteeSample", "Error getting data", e)
            null
        }
    }

    fun insertPisteCache(pistesModel: SlopesModel, context: Context) {
        val cacheFile = File(context.cacheDir, "piste.json")

        if(!cacheFile.exists()) {
            cacheFile.createNewFile()
        }

        val json = Gson().toJson(pistesModel)

        cacheFile.writeText(json)
    }

    fun getPisteCache(context: Context): SlopesModel? {
        val cacheFile = File(context.cacheDir, "piste.json")

        if(!cacheFile.exists()) {
            cacheFile.createNewFile()
        }

        val jsonString = cacheFile.readText()

        return Gson().fromJson(jsonString, SlopesModel::class.java)
    }

    fun insertRemonteeCache(remonteesModel: MountainsModel, context: Context) {
        val cacheFile = File(context.cacheDir, "remontee.json")

        if(!cacheFile.exists()) {
            cacheFile.createNewFile()
        }

        val json = Gson().toJson(remonteesModel)

        cacheFile.writeText(json)
    }

    fun getRemonteeCache(context: Context): MountainsModel? {
        val cacheFile = File(context.cacheDir, "remontee.json")

        if(!cacheFile.exists()) {
            cacheFile.createNewFile()
        }

        val jsonString = cacheFile.readText()

        return Gson().fromJson(jsonString, MountainsModel::class.java)
    }
}