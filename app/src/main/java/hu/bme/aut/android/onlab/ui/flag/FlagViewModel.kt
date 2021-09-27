package hu.bme.aut.android.onlab.ui.flag

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FlagViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is flag Fragment"
    }
    val text: LiveData<String> = _text
}