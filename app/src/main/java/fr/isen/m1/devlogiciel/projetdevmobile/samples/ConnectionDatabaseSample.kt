package fr.isen.m1.devlogiciel.projetdevmobile.samples

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import fr.isen.m1.devlogiciel.projetdevmobile.model.PistesModel

class ConnectionDatabaseSample {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val rf: DatabaseReference = database.getReference("pistes")

    fun getPistesFromDatabase(): PistesModel? {
        var pistes: PistesModel? = null
        rf.get()
            .addOnSuccessListener {
                if (it.exists()) {
                    pistes = it.getValue(PistesModel::class.java)
                    println(pistes?.pistes?.size)
                }
            }
            .addOnFailureListener {
                println("Error getting data")
            }
        return pistes
    }
}