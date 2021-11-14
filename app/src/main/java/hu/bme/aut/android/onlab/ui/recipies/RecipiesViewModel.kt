package hu.bme.aut.android.onlab.ui.recipies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RecipiesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Recipies Fragment"
    }
    val text: LiveData<String> = _text
}