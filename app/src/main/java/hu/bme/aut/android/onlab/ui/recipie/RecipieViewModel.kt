package hu.bme.aut.android.onlab.ui.recipie

import android.app.AlertDialog
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import com.airbnb.mvrx.MavericksViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import hu.bme.aut.android.onlab.data.Recipie
import hu.bme.aut.android.onlab.data.ShoppingItem

class RecipieViewModel(recipie: Recipie) : MavericksViewModel<Recipie>(recipie) {

    private val db = Firebase.firestore
    private val firebaseUser: FirebaseUser?
        get() = FirebaseAuth.getInstance().currentUser

    private lateinit var other_users: List<String>

    init {

        // Save the other users into a list
        db.collection("recipies").whereNotEqualTo("author", firebaseUser?.email).
        get().addOnSuccessListener { snapshots ->
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
        }
        // Todo: iderakni a db-bol valo receptlekerest? - hogy kerul ide a bundle a receptnevvel, hogy melyik kell?
//        setState {
//
//        }
    }

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

    fun getPhoto(recipie: Recipie, inflater: LayoutInflater): Drawable? {
        if(recipie.imageUrls?.isNotEmpty() == true){
            inflater.let {
                return Picasso.get().load(recipie.imageUrls!![0]) as Drawable
            }
        }
        return null
    }

    fun shareDialog(recipie: Recipie, inflater: LayoutInflater) {
        var chose_users = booleanArrayOf()
        var all_user = other_users.toTypedArray()
        for(itm in all_user){
            if(recipie.shares?.contains(itm) == true){
                chose_users += true
            } else {
                chose_users += false
            }
        }
        // Share dialog
        val add_dialog = AlertDialog.Builder(inflater.context)
        add_dialog.setTitle("Share with:")
        add_dialog.setMultiChoiceItems(all_user, chose_users){ dialog, position, isChecked ->
            chose_users[position] = isChecked
        }
        add_dialog.setPositiveButton("Ok"){
                dialog,_->
            // Save chose users
            var tmp_users = emptyList<String>()
            for (idx in chose_users.indices){
                if(chose_users[idx]){
                    tmp_users += all_user[idx]
                }
            }
            saveShares(recipie,inflater, tmp_users)
            dialog.dismiss()
        }
        add_dialog.setNegativeButton("Cancel"){
                dialog,_->
            Toast.makeText(inflater.context, "Cancel", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        add_dialog.create()
        add_dialog.show()
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