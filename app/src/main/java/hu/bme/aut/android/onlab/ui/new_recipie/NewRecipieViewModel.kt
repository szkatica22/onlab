package hu.bme.aut.android.onlab.ui.new_recipie

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.databinding.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.Success
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import hu.bme.aut.android.onlab.R
import hu.bme.aut.android.onlab.data.RecipeState
import hu.bme.aut.android.onlab.data.Recipie

class NewRecipieViewModel(initialState: RecipeState) : MavericksViewModel<RecipeState>(initialState) {

    companion object {
        private const val REQUEST_CODE = 101
    }

//    var flagsMap: Map<String, LiveData<Boolean>> = mapOf()

    private var tmp_bitmap: Bitmap? = null
    private var img_url: String? = null
    private var flag_photo = false
    private var new_recipie: Recipie = Recipie()
    private var ingredients: Map<String?, String?>? = mapOf<String?, String?>()
    private var photos: ArrayList<Bitmap> = arrayListOf<Bitmap>()
    private var steps: ArrayList<String> = arrayListOf<String>()
    private var choosed_chips = mutableListOf<String>()

    var recipe_name = ObservableField<String>()
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
                    choosed_chips += name
                }

                setState {
                    copy(
                        recipeRequest = Success(
                            Recipie(
                                author = firebaseUser?.email,
                                name = recipe_name.get(),
                                time = creating_time.get(),
                                abundance = creating_abundance.get(),
                                ingredients = ingredients,
                                steps = steps,
                                flags = choosed_chips
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

    fun addIngredient(item: String, quantity: String?) {
        ingredients = ingredients?.plus(Pair(item, quantity))
        new_recipie.ingredients = new_recipie.ingredients?.plus(Pair(item, quantity))
//        ingredients.add(item)
//        if(quantity != null){
//            ingr_quantities.add(quantity)
//        }
//        requestModelBuild()
    }

    fun deleteIngredient(itm: String) {
        ingredients = ingredients?.minus(itm)
        new_recipie.ingredients = new_recipie.ingredients?.minus(itm)
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
        steps.add(item)
        new_recipie.steps = new_recipie.steps?.plus(item)
//        requestModelBuild()
    }

    fun deleteSteps(idx: Int) {
        steps.removeAt(idx)
        val itm = steps[idx]
        new_recipie.steps = new_recipie.steps?.minus(itm)
//        requestModelBuild()
    }

    fun checkFavourite() {
        new_recipie.favourite = !new_recipie.favourite!!
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

    fun updateAuthor(){
        new_recipie.author = firebaseUser?.email
    }
    fun updateLists(){
        new_recipie.ingredients = ingredients
        new_recipie.steps = steps
        new_recipie.flags = choosed_chips
//        new_recipie.imageUrls = photos
    }

//    fun getBitmap(): Bitmap {
//        return photos[0]
//    }

    fun checkChips(){
        choosed_chips = mutableListOf<String>()
        flagsMap.forEach{ flag ->
            if(flag.value.get()){
                choosed_chips += flag.key
            }
        }
        new_recipie.flags = choosed_chips
    }

    fun saveRecipie(context: Context){
        updateAuthor()
        updateLists()
        if(new_recipie.favourite == null){
            new_recipie.favourite = false
        }

        new_recipie.name = recipe_name.get()
        new_recipie.time = creating_time.get()
        new_recipie.abundance = creating_abundance.get()

        // Check chose flags
        checkChips()

        db.collection("recipies").add(new_recipie).addOnSuccessListener {
            Toast.makeText(context, "Recipie saved", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { e ->
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
        }

    }
}