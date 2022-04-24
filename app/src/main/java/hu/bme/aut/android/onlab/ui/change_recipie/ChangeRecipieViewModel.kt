package hu.bme.aut.android.onlab.ui.change_recipie

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.airbnb.mvrx.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import hu.bme.aut.android.onlab.R
import hu.bme.aut.android.onlab.data.Recipie
import hu.bme.aut.android.onlab.ui.recipie.RecipeArgs
import java.io.Serializable

data class ChangeRecipeArgs(
    val recipeId: String,
) : Serializable

data class ChangeRecipeState(
    val recipeId: String,
    val recipeRequest: Async<Recipie> = Uninitialized,
    val changedRecipie: Recipie? = null
) : MavericksState {
    constructor(args: ChangeRecipeArgs) : this(recipeId = args.recipeId)
}

class ChangeRecipieViewModel(initialState: ChangeRecipeState)
    : MavericksViewModel<ChangeRecipeState>(initialState) {

    private val db = Firebase.firestore
    private val firebaseUser: FirebaseUser?
        get() = FirebaseAuth.getInstance().currentUser

    private var old_name: String = ""
    private var new_name: String = ""

    var flagsMap = mapOf<String, ObservableBoolean>()
    var favourite = ObservableBoolean()
    var recipe_name = ObservableField<String>()
    var creating_time = ObservableField<String>()
    var creating_abundance = ObservableField<String>()

    init {
        old_name = initialState.recipeId
        new_name = old_name

        // Get all flags from DB
        db.collection("flags").whereEqualTo("creator",
            firebaseUser?.email).get().addOnSuccessListener { snapshots ->
            if(snapshots != null){
                for(snap in snapshots.documents){
                    val name: String = snap.data?.get("name") as String
                    flagsMap = flagsMap.plus(Pair(name, ObservableBoolean(false)))
                }

                // Get Recipe from DB
                db.collection("recipies").whereEqualTo("name", initialState.recipeId).get()
                    .addOnSuccessListener { snapshot ->
                        if (snapshot.documents.isNotEmpty()) {
                            val tmp_data = snapshot.documents[0].data
                            val flags = tmp_data?.get("flags") as List<String?>?

                            // Set observable initial values
                            recipe_name.set(initialState.recipeId)
                            creating_time.set(tmp_data?.get("time") as String)
                            creating_abundance.set(tmp_data["abundance"] as String)

                            // Check chose flags
                            for(flag in flagsMap){
                                if (flags != null) {
                                    if(flags.contains(flag.key)){
                                        flag.value.set(true)
                                    }
                                }
                            }
                            val tmp_chips = mutableListOf<String>()
                            flagsMap.forEach{ flag ->
                                tmp_chips += flag.key
                            }
                            setState {
                                copy(
                                    recipeRequest = Success(
                                        Recipie(
                                            initialState.recipeId,
                                            tmp_data?.get("favourite") as Boolean,
                                            tmp_chips,
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
        }
    }

    fun checkFavourite() = withState {
        favourite.set(!favourite.get())
        val recipie = it.changedRecipie ?: it.recipeRequest() ?: return@withState
        setState {
            copy(
                changedRecipie = recipie.copy(
                    favourite = favourite.get()
                )
            )
        }
    }

    fun addIngredientDialog(context: Context){
        val v = LayoutInflater.from(context).inflate(R.layout.add_ingredient, null)
        val ingredient = v.findViewById<EditText>(R.id.et_new_recipie_ingredient)
        val quantity = v.findViewById<EditText>(R.id.et_new_recipie_quantity)
        var info: String? = null
        val units_array = context.resources?.getStringArray(R.array.units_array)
        val unit = v.findViewById<Spinner>(R.id.spinner_unit)
        unit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                info = quantity.text.toString() + " -"
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?,
                                        position: Int, id: Long) {
                info = quantity.text.toString() + " " + units_array?.get(position).toString()
            }
        }

        val add_dialog = AlertDialog.Builder(context)
        add_dialog.setView(v)
        add_dialog.setPositiveButton("Ok"){
                dialog,_->
            this.addIngredient(ingredient.text.toString(), info)
            Toast.makeText(context, "Adding Ingredient", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        add_dialog.setNegativeButton("Cancel"){
                dialog,_->
            Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show()
        }
        add_dialog.create()
        add_dialog.show()
    }

    fun addIngredient(item: String, quantity: String?) = withState {
        val recipie = it.changedRecipie ?: it.recipeRequest() ?: return@withState
        setState {
            copy(
                changedRecipie = recipie.copy(
                    ingredients = recipie.ingredients?.plus(Pair(item, quantity))
                )
            )
        }
    }

    fun deleteIngredient(itm: String) = withState {
        val recipie = it.changedRecipie ?: it.recipeRequest() ?: return@withState
        setState {
            copy(
                changedRecipie = recipie.copy(
                    ingredients = recipie.ingredients?.minus(itm)
                )
            )
        }
    }

    fun addPrepDialog(context: Context) {
        val v = LayoutInflater.from(context).inflate(R.layout.add_step, null)
        val step = v.findViewById<EditText>(R.id.et_new_recipie_step)
        val add_dialog = AlertDialog.Builder(context)
        add_dialog.setView(v)
        add_dialog.setPositiveButton("Ok"){
                dialog,_->
            addStep(step.text.toString())
            Toast.makeText(context, "Adding Step", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        add_dialog.setNegativeButton("Cancel"){
                dialog,_->
            Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show()
        }
        add_dialog.create()
        add_dialog.show()
    }

    fun addStep(item: String) = withState {
        val recipie = it.changedRecipie ?: it.recipeRequest() ?: return@withState
        setState {
            copy(
                changedRecipie = recipie.copy(
                    steps = recipie.steps?.plus(item)
                )
            )
        }
    }

    fun deleteSteps(item: String) = withState {
        val recipie = it.changedRecipie ?: it.recipeRequest() ?: return@withState
        setState {
            copy(
                changedRecipie = recipie.copy(
                    steps = recipie.steps?.minus(item)
                )
            )
        }
    }

//    fun deletePhoto(idx: Int){
//        tmp_rec.imageUrls?.drop(idx)
//        if(photos.size >= 1){
//            photos.removeAt(idx)
//        }
//        requestModelBuild()
//    }
//
//    fun addPhoto(item: Bitmap?) {
//        if (item != null) {
//            photos.add(item)
//            requestModelBuild()
//        }
//    }
//
//    fun getBitmap(): Bitmap {
//        return photos[0]
//    }
//
//    fun updateLists(){
//        tmp_rec.ingredients = ingredients
//        tmp_rec.steps = steps
//    }

    fun checkChips(context: Context) = withState {
        val recipie = it.changedRecipie ?: it.recipeRequest() ?: return@withState
        var tmp_chips = mutableListOf<String>()
        flagsMap.forEach{ flag ->
            if(flag.value.get()){
                tmp_chips += flag.key
            }
        }
        setState {
            copy(
                changedRecipie = recipie.copy(
                    flags = tmp_chips
                )
            )
        }

        setObcservables(context)
    }

    fun setObcservables(context: Context) = withState {
        val recipie = it.changedRecipie ?: it.recipeRequest() ?: return@withState
        setState {
            copy(
                changedRecipie = recipie.copy(
                    name = recipe_name.get(),
                    time = creating_time.get(),
                    abundance = creating_abundance.get(),
                    favourite = favourite.get()
                )
            )
        }
        new_name = recipe_name.get() ?: old_name
        saveRecipie(context)
    }

    fun getArgs(): Bundle{
        return RecipeArgs(new_name).asMavericksArgs()
    }

    fun saveRecipie(context: Context) = withState {

        val recipie = it.changedRecipie ?: it.recipeRequest() ?: return@withState

        db.collection("recipies").whereEqualTo("name", old_name).get().
        addOnSuccessListener { snapshot ->
            if(snapshot != null){
                if(snapshot.documents.isNotEmpty()){
                    db.collection("recipies").document(snapshot.documents[0].id).
                    update("abundance", recipie.abundance, "favourite",
                        recipie.favourite, "flags", recipie.flags, "imageUrls", recipie.imageUrls,
                        "ingredients", recipie.ingredients, "name", recipie.name, "steps",
                        recipie.steps, "time", recipie.time)

                    Toast.makeText(context, "Changes saved", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun deleteRecipie(context: Context) = withState {
        db.collection("recipies").whereEqualTo("name", it.recipeId).get().
        addOnSuccessListener { snapshot ->
            if(snapshot != null){
                for(doc in snapshot.documents){
                    db.collection("recipies").document(doc.id).delete()
                    Toast.makeText(context, "Recipe deleted", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}