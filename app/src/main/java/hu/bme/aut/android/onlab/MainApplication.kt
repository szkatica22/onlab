package hu.bme.aut.android.onlab

import android.app.Application
import com.airbnb.mvrx.Mavericks

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Mavericks.initialize(this)
    }
}