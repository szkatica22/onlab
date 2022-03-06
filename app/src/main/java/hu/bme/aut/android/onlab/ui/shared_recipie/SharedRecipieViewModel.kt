package hu.bme.aut.android.onlab.ui.shared_recipie

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.Uninitialized
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import hu.bme.aut.android.onlab.data.Recipie
import hu.bme.aut.android.onlab.data.ShoppingItem
import java.io.Serializable

data class RecipeArgs(
    val recipeId: String,
) : Serializable

data class RecipeState(
    val recipeId: String,
    val recipeRequest: Async<Recipie> = Uninitialized
) : MavericksState {
    constructor(args: RecipeArgs) : this(recipeId = args.recipeId)
}

class SharedRecipieViewModel(initialState: RecipeState) : MavericksViewModel<RecipeState>(initialState) {

    private val db = Firebase.firestore
    private val firebaseUser: FirebaseUser?
        get() = FirebaseAuth.getInstance().currentUser

    private lateinit var rec: Recipie

    init {
//        setState {
////            this.copy(recipeId = this.arguments?.get("recipiename").toString())
//            this.copy(recipeId = "Tiramisu")
//        }
//        initialState.recipeId = arguments?.get("recipiename").toString()
//        initialState.recipeId = "Tiramisu"

        // Get Rec. from DB
        db.collection("recipies").whereEqualTo("name", initialState.recipeId).get().
        addOnSuccessListener { snapshot ->
            if (snapshot.documents.isNotEmpty()) {
                val tmp_data = snapshot.documents[0].data
                rec = Recipie(
                    initialState.recipeId,
                    tmp_data?.get("favourite") as Boolean,
                    tmp_data.get("flags") as List<String?>?,
                    tmp_data.get("imageUrls") as List<String?>?,
                    tmp_data.get("time").toString(),
                    tmp_data.get("abundance").toString(),
                    tmp_data.get("author").toString(),
                    tmp_data.get("ingredients") as Map<String?, String?>?,
                    tmp_data.get("steps") as List<String?>?,
                    tmp_data.get("shares") as List<String?>?
                )
            }
        }
    }

    fun deleteShare(rec_name: String?, inflater: LayoutInflater){
        db.collection("recipies").whereEqualTo("name", rec_name).get().
        addOnSuccessListener { snapshot ->
            if(snapshot != null){
                for(doc in snapshot.documents){
                    if(doc["author"] != firebaseUser?.email){
                        val tmp_list = doc["shares"] as ArrayList<*>
                        tmp_list.remove(firebaseUser?.email)
                        db.collection("recipies").document(doc.id).
                        update("shares", tmp_list)
                    }
                    Toast.makeText(inflater.context, "Share deleted successfully",
                        Toast.LENGTH_SHORT).show()
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

    fun getPhoto(recipie: Recipie, inflater: LayoutInflater): Drawable? {
        if(recipie.imageUrls?.isNotEmpty() == true){
            inflater.let {
                return Picasso.get().load(recipie.imageUrls!![0]) as Drawable
            }
        }
        return null
    }

    fun getRecipie(): Recipie {
        return rec
    }

    fun getBundle(recipie: Recipie): Bundle {
        val bundle = Bundle()
        bundle.putString("recipiename", recipie.name)
        return bundle
    }

//    private val _text = MutableLiveData<String>().apply {
//        value = "This is Shared Recipie Fragment"
//    }
//    val text: LiveData<String> = _text
}