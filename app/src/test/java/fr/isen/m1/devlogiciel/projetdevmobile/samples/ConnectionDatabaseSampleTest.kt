package fr.isen.m1.devlogiciel.projetdevmobile.samples

import org.junit.Assert.assertNotNull
import org.junit.Test

class ConnectionDatabaseSampleTest {
    @Test
    fun getPistesFromDatabaseTest() {
        val connectionDatabaseSample = ConnectionDatabaseSample()
        val pistes = connectionDatabaseSample.getPistesFromDatabase()
        assertNotNull(pistes)
    }
}