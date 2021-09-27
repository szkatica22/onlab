package hu.bme.aut.android.onlab.ui.change_settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChangeSettingsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is change settings Fragment"
    }
    val text: LiveData<String> = _text
}