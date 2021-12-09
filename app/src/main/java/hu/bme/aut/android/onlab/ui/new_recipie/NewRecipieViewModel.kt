package hu.bme.aut.android.onlab.ui.new_recipie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NewRecipieViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is new recipe Fragment"
    }
    val text: LiveData<String> = _text
}