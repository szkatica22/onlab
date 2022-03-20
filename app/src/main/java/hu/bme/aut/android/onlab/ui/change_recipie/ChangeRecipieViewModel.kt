package hu.bme.aut.android.onlab.ui.change_recipie

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.asMavericksArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import hu.bme.aut.android.onlab.R
import hu.bme.aut.android.onlab.data.RecipeArgs
import hu.bme.aut.android.onlab.data.RecipeState
import hu.bme.aut.android.onlab.data.Recipie

class ChangeRecipieViewModel(initialState: RecipeState) : MavericksViewModel<RecipeState>(initialState) {

    private val db = Firebase.firestore
    private val firebaseUser: FirebaseUser?
        get() = FirebaseAuth.getInstance().currentUser

    private var old_recipe: Recipie = Recipie()
    private var old_rec_name = ""
    var flagsMap = mapOf<String, ObservableBoolean>()
    private var choosed_chips = mutableListOf<String>()

    var favourite = ObservableBoolean()
    var recipe_name = ObservableField<String>()
    var creating_time = ObservableField<String>()
    var creating_abundance = ObservableField<String>()

    init {
        old_rec_name = initialState.recipeId

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
                            // Check chose flags
                            for(flag in flagsMap){
                                if (flags != null) {
                                    if(flags.contains(flag.key)){
                                        flag.value.set(true)
                                    }
                                }
                            }
                            old_recipe = Recipie(
                                initialState.recipeId,
                                tmp_data?.get("favourite") as Boolean,
                                flags,
                                tmp_data.get("imageUrls") as List<String?>?,
                                tmp_data.get("time").toString(),
                                tmp_data.get("abundance").toString(),
                                tmp_data.get("author").toString(),
                                tmp_data.get("ingredients") as Map<String?, String?>?,
                                tmp_data.get("steps") as List<String?>?,
                                tmp_data.get("shares") as List<String?>?
                            )

                            // Set observable variables initial values
                            if(old_recipe.favourite == true){
                                favourite.set(true)
                            } else {
                                favourite.set(false)
                            }
                            recipe_name.set(old_recipe.name)
                            creating_time.set(old_recipe.time)
                            creating_abundance.set(old_recipe.abundance)

                            setState {
                                copy(
                                    recipeRequest = Success(
                                        old_recipe
                                    )
                                )
                            }
                        }
                    }
            }
        }
    }

    fun checkFavourite() {
        old_recipe.favourite = !old_recipe.favourite!!
        favourite.set(!favourite.get())
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

    fun addIngredient(item: String, quantity: String?) {
//        ingredients = ingredients?.plus(Pair(item, quantity))
        old_recipe.ingredients = old_recipe.ingredients?.plus(Pair(item, quantity))
//        ingredients.add(item)
//        if(quantity != null){
//            ingr_quantities.add(quantity)
//        }
//        requestModelBuild()
    }

    fun deleteIngredient(itm: String) {
//        ingredients = ingredients?.minus(itm)
        old_recipe.ingredients = old_recipe.ingredients?.minus(itm)
//        notifyModelChanged(ingredients?.size!!)
//        requestModelBuild()
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

    fun addStep(item: String) {
//        steps.add(item)
        old_recipe.steps = old_recipe.steps?.plus(item)
//        requestModelBuild()
    }

    fun deleteSteps(idx: Int) {
//        steps.removeAt(idx)
//        val itm = steps[idx]
        if(old_recipe.steps != null){
            val itm = old_recipe.steps!![idx]
            old_recipe.steps = old_recipe.steps?.minus(itm)
        }
//        requestModelBuild()
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

    fun checkChips(){
        choosed_chips = mutableListOf<String>()
        flagsMap.forEach{ flag ->
            if(flag.value.get()){
                choosed_chips += flag.key
            }
        }
        old_recipe.flags = choosed_chips
    }

    fun getArgs(): Bundle{
        return RecipeArgs(old_recipe.name!!).asMavericksArgs()
    }

    fun saveRecipie(context: Context){
        old_recipe.name = recipe_name.get()
        old_recipe.time = creating_time.get()
        old_recipe.abundance = creating_abundance.get()

        // Check chose chips
        checkChips()

        db.collection("recipies").whereEqualTo("name", old_rec_name).get().
        addOnSuccessListener { snapshot ->
            if(snapshot != null){
                if(snapshot.documents.isNotEmpty()){
                    db.collection("recipies").document(snapshot.documents[0].id).
                    update("abundance", old_recipe.abundance, "favourite",
                        old_recipe.favourite, "flags", old_recipe.flags, "imageUrls", old_recipe.imageUrls,
                        "ingredients", old_recipe.ingredients, "name", old_recipe.name, "steps",
                        old_recipe.steps, "time", old_recipe.time)

                    Toast.makeText(context, "Changes saved", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun deleteRecipie(context: Context){
        db.collection("recipies").whereEqualTo("name", old_rec_name).get().
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