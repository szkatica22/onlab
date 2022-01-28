package hu.bme.aut.android.onlab.ui.shared_recipie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedRecipieViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Shared Recipie Fragment"
    }
    val text: LiveData<String> = _text
}