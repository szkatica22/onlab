package hu.bme.aut.android.onlab.ui.change_recipie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChangeRecipieViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is change recipe Fragment"
    }
    val text: LiveData<String> = _text
}