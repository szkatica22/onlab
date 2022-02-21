package hu.bme.aut.android.onlab.ui.recipie

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.airbnb.mvrx.MavericksViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import hu.bme.aut.android.onlab.R
import hu.bme.aut.android.onlab.data.Recipie
import hu.bme.aut.android.onlab.data.ShoppingItem

class RecipieViewModel(recipie: Recipie) : MavericksViewModel<Recipie>(recipie) {

    private val db = Firebase.firestore
    private val firebaseUser: FirebaseUser?
        get() = FirebaseAuth.getInstance().currentUser

//    private val _text = MutableLiveData<String>().apply {
//        value = "This is recipie Fragment"
//    }
//    val text: LiveData<String> = _text

    fun deleteRecipie(recipie: Recipie, inflater: LayoutInflater){

        db.collection("recipies").whereEqualTo("name", recipie.name).get().
        addOnSuccessListener { snapshot ->
            if(snapshot != null){
                for(doc in snapshot.documents){
                    db.collection("recipies").document(doc.id).delete()
                    Toast.makeText(inflater.context, "Recipie deleted", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun saveCart(recipie: Recipie, inflater: LayoutInflater){

        if(recipie.ingredients?.isEmpty()!!){
            return
        }
        var flag_ok = true
        recipie.ingredients!!.keys.forEach{ item ->
            val quant_unit = recipie.ingredients!![item]?.split(" ")
            val new_item = ShoppingItem(item, quant_unit?.get(0)?.toFloat(),
                quant_unit?.get(1), firebaseUser?.email, false)
            db.collection("shopping_list").add(new_item).addOnFailureListener{
                flag_ok = false
            }
        }
        when {
            flag_ok -> {
                Toast.makeText(inflater.context, "All successfully added", Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(inflater.context, "Add failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun saveShares(recipie: Recipie, inflater: LayoutInflater, tmp_users: List<String>){

        db.collection("recipies").whereEqualTo("name", recipie.name).get().
        addOnSuccessListener { snapshot ->
            if(snapshot != null){
                if(snapshot.documents.isNotEmpty()){
                    db.collection("recipies").document(snapshot.documents[0].id).
                    update("shares", tmp_users)
                    Toast.makeText(inflater.context, "Recipe Shared Successfully",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun getBundle(recipie: Recipie): Bundle {
        val bundle = Bundle()
        bundle.putString("recipiename", recipie.name)
        return bundle
    }

}