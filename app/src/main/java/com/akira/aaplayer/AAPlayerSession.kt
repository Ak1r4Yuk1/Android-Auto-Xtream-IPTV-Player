package com.akira.aaplayer

import android.content.Intent
import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.Session

class AAPlayerSession : Session() {
    override fun onCreateScreen(intent: Intent): Screen {
        return AAPlayerScreen(carContext)
    }
}
