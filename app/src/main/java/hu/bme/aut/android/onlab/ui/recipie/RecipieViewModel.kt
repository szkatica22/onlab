package hu.bme.aut.android.onlab.ui.recipie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RecipieViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is recipie Fragment"
    }
    val text: LiveData<String> = _text
}