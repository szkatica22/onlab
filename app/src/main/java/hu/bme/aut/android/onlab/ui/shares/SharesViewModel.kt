package hu.bme.aut.android.onlab.ui.shares

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Shared Recipes Fragment"
    }
    val text: LiveData<String> = _text
}