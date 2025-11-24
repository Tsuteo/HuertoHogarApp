package com.example.huertohogarfinal

import android.app.Application
import com.example.huertohogarfinal.data.db.HuertoHogarDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class HuertoApp : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy {
        HuertoHogarDatabase.getDatabase(this, applicationScope)
    }
}