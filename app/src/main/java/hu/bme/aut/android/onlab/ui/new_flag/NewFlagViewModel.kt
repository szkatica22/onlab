package hu.bme.aut.android.onlab.ui.new_flag

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NewFlagViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is new flag Fragment"
    }
    val text: LiveData<String> = _text
}