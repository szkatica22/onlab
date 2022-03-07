package hu.bme.aut.android.onlab.ui.recipie

import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.navigation.navArgument
import com.airbnb.mvrx.*
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

class RecipieViewModel(initialState: RecipeState) : MavericksViewModel<RecipeState>(initialState) {

    private val db = Firebase.firestore
    private val firebaseUser: FirebaseUser?
        get() = FirebaseAuth.getInstance().currentUser

    private lateinit var other_users: List<String>
    private lateinit var rec: Recipie

    init {
        setState {
            //this.copy(recipeId = this.arguments?.get("recipiename").toString())
            this.copy(recipeId = "Tiramisu")
        }

        // Save the other users into a list
        /*db.collection("recipies").whereNotEqualTo("author", firebaseUser?.email).get()
            .addOnSuccessListener { snapshots ->
                if (snapshots.documents.isNotEmpty()) {
                    for (doc in snapshots.documents) {
                        if (doc["author"] != firebaseUser?.email && !other_users.contains(
                                doc["author"].toString()
                            )
                        ) {
                            other_users += doc["author"].toString()
                        }
                    }
                }
            }*/
        // Get Rec. from DB
        db.collection("recipies").whereEqualTo("name", initialState.recipeId).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.documents.isNotEmpty()) {
                    val tmp_data = snapshot.documents[0].data
                    setState {
                        copy(
                            recipeRequest = Success(
                                Recipie(
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
                            )
                        )
                    }
                }
            }
    }

//    private val _text = MutableLiveData<String>().apply {
//        value = "This is recipie Fragment"
//    }
//    val text: LiveData<String> = _text

    fun deleteRecipie(recipie: Recipie, context: Context) {

        db.collection("recipies").whereEqualTo("name", recipie.name).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null) {
                    for (doc in snapshot.documents) {
                        db.collection("recipies").document(doc.id).delete()
                        Toast.makeText(context, "Recipie deleted", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
    }

    fun saveCart(recipie: Recipie, inflater: Context) {

        if (recipie.ingredients?.isEmpty()!!) {
            return
        }
        var flag_ok = true
        recipie.ingredients!!.keys.forEach { item ->
            val quant_unit = recipie.ingredients!![item]?.split(" ")
            val new_item = ShoppingItem(
                item, quant_unit?.get(0)?.toFloat(),
                quant_unit?.get(1), firebaseUser?.email, false
            )
            db.collection("shopping_list").add(new_item).addOnFailureListener {
                flag_ok = false
            }
        }
        when {
            flag_ok -> {
                Toast.makeText(inflater, "All successfully added", Toast.LENGTH_SHORT)
                    .show()
            }
            else -> {
                Toast.makeText(inflater, "Add failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getPhoto(recipie: Recipie, inflater: Context): Drawable? {
        if (recipie.imageUrls?.isNotEmpty() == true) {
            inflater.let {
                return Picasso.get().load(recipie.imageUrls!![0]) as Drawable
            }
        }
        return null
    }

    /*fun getRecipie(): Recipie? {

        withState { it.recipeRequest() }
    }*/

    fun shareDialog(recipie: Recipie, inflater: Context) {
        var chose_users = booleanArrayOf()
        //var all_user = other_users.toTypedArray()
        val all_user = emptyList<String>().toTypedArray()
        for (itm in all_user) {
            if (recipie.shares?.contains(itm) == true) {
                chose_users += true
            } else {
                chose_users += false
            }
        }
        // Share dialog
        val add_dialog = AlertDialog.Builder(inflater)
        add_dialog.setTitle("Share with:")
        add_dialog.setMultiChoiceItems(all_user, chose_users) { dialog, position, isChecked ->
            chose_users[position] = isChecked
        }
        add_dialog.setPositiveButton("Ok") { dialog, _ ->
            // Save chose users
            var tmp_users = emptyList<String>()
            for (idx in chose_users.indices) {
                if (chose_users[idx]) {
                    tmp_users += all_user[idx]
                }
            }
            saveShares(recipie, inflater, tmp_users)
            dialog.dismiss()
        }
        add_dialog.setNegativeButton("Cancel") { dialog, _ ->
            Toast.makeText(inflater, "Cancel", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        add_dialog.create()
        add_dialog.show()
    }

    fun saveShares(recipie: Recipie, inflater: Context, tmp_users: List<String>) {
        db.collection("recipies").whereEqualTo("name", recipie.name).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null) {
                    if (snapshot.documents.isNotEmpty()) {
                        db.collection("recipies").document(snapshot.documents[0].id)
                            .update("shares", tmp_users)
                        Toast.makeText(
                            inflater, "Recipe Shared Successfully",
                            Toast.LENGTH_SHORT
                        ).show()
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