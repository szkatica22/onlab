package hu.bme.aut.android.onlab.ui.new_recipie

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.databinding.*
import com.airbnb.mvrx.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import hu.bme.aut.android.onlab.R
import hu.bme.aut.android.onlab.data.Recipie
import java.io.Serializable

data class NewRecipeArgs(
    val recipeId: String,
) : Serializable

data class NewRecipeState(
    val recipeId: String,
    val recipeRequest: Async<Recipie> = Uninitialized,
    val changedRecipie: Recipie? = null
) : MavericksState {
    constructor(args: NewRecipeArgs) : this(recipeId = args.recipeId)
}


class NewRecipieViewModel(initialState: NewRecipeState)
    : MavericksViewModel<NewRecipeState>(initialState) {

    var recipe_name = ObservableField<String>()
    var favourite = ObservableBoolean(false)
    var creating_time = ObservableField<String>()
    var creating_abundance = ObservableField<String>()
    var flagsMap = mapOf<String, ObservableBoolean>()

    private val db = Firebase.firestore
    private val firebaseUser: FirebaseUser?
        get() = FirebaseAuth.getInstance().currentUser

    init {
        // DB-bol lekerni az osszes flag-t ami az adott felhasznalohoz tartozik
        db.collection("flags").whereEqualTo("creator",
            firebaseUser?.email).get().addOnSuccessListener { snapshots ->
            if(snapshots != null){
                for(snap in snapshots.documents){
                    val name: String = snap.data?.get("name") as String
                    flagsMap = flagsMap.plus(Pair(name, ObservableBoolean(false)))
                }

                val tmp_chips = mutableListOf<String>()
                flagsMap.forEach{ flag ->
                    tmp_chips += flag.key
                }
                setState {
                    copy(
                        recipeRequest = Success(
                            Recipie(
                                author = firebaseUser?.email,
                                flags = tmp_chips
                            )
                        )
                    )
                }
            }
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
        Log.d("ADD INGR I: ", "${recipie.ingredients}")
        setState {
            copy(
                changedRecipie = recipie.copy(
                    ingredients = recipie.ingredients?.plus(Pair(item, quantity))
                )
            )
        }
        val recipie_2 = it.changedRecipie ?: it.recipeRequest() ?: return@withState
        Log.d("ADD INGR II: ", "${recipie_2.ingredients}")

//        ingredients = ingredients?.plus(Pair(item, quantity))
//        new_recipie.ingredients = new_recipie.ingredients?.plus(Pair(item, quantity))
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
//        ingredients = ingredients?.minus(itm)
//        new_recipie.ingredients = new_recipie.ingredients?.minus(itm)
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
//        steps.add(item)
//        new_recipie.steps = new_recipie.steps?.plus(item)
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
//        steps.removeAt(idx)
//        val itm = steps[idx]
//        new_recipie.steps = new_recipie.steps?.minus(itm)
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
//        new_recipie.favourite = !new_recipie.favourite!!
    }

//    fun addPhoto(item: Bitmap?) {
//        if (item != null) {
//            photos.add(item)
////            requestModelBuild()
//        }
//    }
//
//    fun deletePhoto(idx: Int) {
//        photos.removeAt(idx)
////        requestModelBuild()
//    }
//
//    fun updateImageView(iv: ImageView, bitmap: Bitmap){
//        iv.setImageBitmap(bitmap)
//        iv.visibility = View.VISIBLE
//    }

//    fun getBitmap(): Bitmap {
//        return photos[0]
//    }

    fun checkChips() = withState{
        val recipie = it.changedRecipie ?: it.recipeRequest() ?: return@withState
        val tmp_chips = mutableListOf<String>()
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
    }

    fun setObcservables() = withState {
        val recipie = it.changedRecipie ?: it.recipeRequest() ?: return@withState
        setState {
            copy(
                changedRecipie = recipie.copy(
                    name = recipe_name.get(),
                    time = creating_time.get(),
                    abundance = creating_abundance.get(),
//                    author = firebaseUser?.email
                )
            )
        }
    }

    fun saveRecipie(context: Context) = withState{

        // Check chose flags
        checkChips()

        // Set observable values
        setObcservables()

        val recipie = it.changedRecipie ?: it.recipeRequest() ?: return@withState

        Log.d("REC: ", "$recipie")
        Log.d("_: ", "SAVE")

//        db.collection("recipies").add(recipie).addOnSuccessListener {
//            Toast.makeText(context, "Recipie saved", Toast.LENGTH_SHORT).show()
//        }.addOnFailureListener { e ->
//            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
//        }

    }
}