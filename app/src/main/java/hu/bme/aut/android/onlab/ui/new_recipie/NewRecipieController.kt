package hu.bme.aut.android.onlab.ui.new_recipie

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.children
import androidx.core.view.get
import androidx.core.view.iterator
import androidx.core.widget.addTextChangedListener
import androidx.navigation.findNavController
import com.airbnb.epoxy.EpoxyController
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import hu.bme.aut.android.onlab.R
import hu.bme.aut.android.onlab.data.Recipie
import hu.bme.aut.android.onlab.databinding.*
import hu.bme.aut.android.onlab.ui.epoxy.ViewBindingKotlinModel

class NewRecipieController(private var btn_ingredient: String, private var prep_title: String,
                           private var btn_step: String, private var btn_save: String,
                           private val inflater: LayoutInflater) : EpoxyController() {

    private val db = Firebase.firestore
    private var new_recipie: Recipie = Recipie()
    private var chipGroup: ChipGroup? = null
    private var ingredients: ArrayList<String> = arrayListOf<String>()
    private var steps: ArrayList<String> = arrayListOf<String>()
    private var choosed_chips = mutableListOf<String>()

    private val name_watcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (start == 22) {
                Toast.makeText(inflater.context, "Maximum Limit Reached", Toast.LENGTH_SHORT)
                    .show()
            } else {
                new_recipie.name = s.toString()
            }

        }
    }

    private val time_watcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            new_recipie.time = s.toString()
        }
    }

    private val abundance_watcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            new_recipie.abundance = s.toString()
        }
    }

    private val firebaseUser: FirebaseUser?
        get() = FirebaseAuth.getInstance().currentUser

    fun addIngredient(item: String) {
        ingredients.add(item)
        requestModelBuild()
    }

    fun deleteIngredient(idx: Int) {
        ingredients.removeAt(idx)
        requestModelBuild()
    }

    fun addStep(item: String) {
        steps.add(item)
        requestModelBuild()
    }

    fun deleteSteps(idx: Int) {
        steps.removeAt(idx)
        requestModelBuild()
    }

    fun updateAuthor(){
        new_recipie.author = firebaseUser?.email
    }
    fun updateLists(){
        new_recipie.ingredients = ingredients
        new_recipie.steps = steps
    }

    fun addChip(chip_name: String){
        choosed_chips.add(chip_name)
    }
    fun removeChip(chip_name: String){
        choosed_chips.remove(chip_name)
    }

    fun saveRecipie(){
        // TODO: kivalasztott chip-eket itt kellene belementeni majd
    //  TODO: - vegigmenni rajtuk egyesevel elmentegetni a recept peldanyokat
        updateAuthor()
        updateLists()
        if(new_recipie.favourite == null){
            new_recipie.favourite = false
        }

        // Check choosed flags
        new_recipie.flags = choosed_chips

        db.collection("recipies").add(new_recipie).addOnSuccessListener {
            Toast.makeText(inflater.context, "Recipie saved", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { e ->
            Toast.makeText(inflater.context, e.toString(), Toast.LENGTH_SHORT).show()
        }

    }

    override fun buildModels() {
        HeaderEpoxyModel(this).id(name_watcher.toString()).addTo(this)
        InformationsEpoxyModel(this, chipGroup).id(time_watcher.toString()).addTo(this)
        if(!ingredients.isEmpty()){
            ingredients.forEach { item ->
                IngredientEpoxyModel(ingredients, item,this).id(item).addTo(this)
            }
        }
        IngrFloatingButtonEpoxyController(btn_ingredient, this).id(btn_ingredient).addTo(this)

        PreparationTextEpoxyController(prep_title).id(prep_title).addTo(this)
        if(!steps.isEmpty()){
            steps.forEach { item ->
//                Log.d("ITEM: ", item.toString())
                PreparationEpoxyModel(steps, item, this).id(item).addTo(this)
            }
        }

        StepFloatingButtonEpoxyController(btn_step, this).id(btn_step).addTo(this)

        SaveRecipieEpoxyModel(btn_save, this).id(btn_save).addTo(this)

    }

    // Data classes

    // Header
    data class HeaderEpoxyModel(var controller: NewRecipieController):
    ViewBindingKotlinModel<NewRecipieHeaderBinding>(R.layout.new_recipie_header){
        override fun NewRecipieHeaderBinding.bind() {
//            controller.updateName(etNewRecipieName.text.toString())
            etNewRecipieName.addTextChangedListener(controller.name_watcher)

            imgBtnFavourite.setOnClickListener {
                if (controller.new_recipie.favourite == null || controller.new_recipie.favourite == false){
                    controller.new_recipie.favourite = true
                    imgBtnFavourite.setImageResource(R.drawable.ic_menu_favourites)
                } else {
                    controller.new_recipie.favourite = false
                    imgBtnFavourite.setImageResource(R.drawable.ic_recipie_favorite_border)
                }
            }
            imgBtnCancel.setOnClickListener {
                it.findNavController().navigate(R.id.action_nav_new_recipie_to_nav_recipies)
            }
        }
    }

    // Information
    data class InformationsEpoxyModel(
        var controller: NewRecipieController,
        var chipGroup: ChipGroup?
    ):
        ViewBindingKotlinModel<NewRecipieInformationsBinding>(R.layout.new_recipie_informations){
        override fun NewRecipieInformationsBinding.bind() {

            chipGroup = cgRecipieFlags
            chipGroup?.removeAllViews()
            controller.db.collection("flags").get().addOnSuccessListener { snapshots ->
                if(snapshots != null){
                    for(snap in snapshots.documents){
                        val chip = Chip(chipGroup?.context)
                        chip.text = snap.get("name").toString()
                        chip.isClickable = true
                        chip.isCheckable = true
                        chipGroup?.addView(chip)

                        chip.setOnCheckedChangeListener { view, is_checked ->
                            if(is_checked){
                                controller.addChip(view.text.toString())
                            } else {
                                controller.removeChip(view.text.toString())
                            }
                        }
                    }
                }
            }

            etRecipieTime.addTextChangedListener(controller.time_watcher)
            etRecipieAbundance.addTextChangedListener(controller.abundance_watcher)
        }
    }

    // Ingredients
    data class IngredientEpoxyModel(var ingredients: List<String>, val ingredient: String,
                                    var controller: NewRecipieController):
        ViewBindingKotlinModel<NewRecipieItemBinding>(R.layout.new_recipie_item){
        override fun NewRecipieItemBinding.bind() {
            tvNewRecipieItemTitleId.text = ingredient
            ivNewRecipieDelete.setOnClickListener {
                controller.deleteIngredient(ingredients.indexOf(ingredient))
            }
        }
    }

    // Ingredients Button
    data class IngrFloatingButtonEpoxyController(val title: String, var controller: NewRecipieController):
        ViewBindingKotlinModel<NewRecipieFloatingButtonBinding>(R.layout.new_recipie_floating_button){
        override fun NewRecipieFloatingButtonBinding.bind() {
            fltBtnRecipieItems.text = title
            fltBtnRecipieItems.setOnClickListener {

                val v = controller.inflater.inflate(R.layout.add_ingredient, null)
                val ingredient = v.findViewById<EditText>(R.id.et_new_recipie_ingredient)
                val add_dialog = AlertDialog.Builder(controller.inflater.context)
                add_dialog.setView(v)
                add_dialog.setPositiveButton("Ok"){
                        dialog,_->
                    controller.addIngredient(ingredient.text.toString())
                    Toast.makeText(controller.inflater.context, "Adding Ingredient", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                add_dialog.setNegativeButton("Cancel"){
                        dialog,_->
                    Toast.makeText(controller.inflater.context, "Cancel", Toast.LENGTH_SHORT).show()
                }
                add_dialog.create()
                add_dialog.show()
            }
        }
    }

    // Preparation text
    data class PreparationTextEpoxyController(val title: String):
        ViewBindingKotlinModel<NewRecipiePreparationTextBinding>(R.layout.new_recipie_preparation_text){
        override fun NewRecipiePreparationTextBinding.bind() {
            tvNewRecipiePrepTitle.text = title
        }

    }

    // Preparation steps
    data class PreparationEpoxyModel(var steps: List<String>, val step: String, var controller: NewRecipieController):
        ViewBindingKotlinModel<NewRecipiePrepItemBinding>(R.layout.new_recipie_prep_item){
        override fun NewRecipiePrepItemBinding.bind(){
            tvNewRecipieItemTitleId.text = step
            ivNewRecipieDelete.setOnClickListener {
                controller.deleteSteps(steps.indexOf(step))
            }
        }
    }

    // Preparation Steps Button
    data class StepFloatingButtonEpoxyController(val title: String, var controller: NewRecipieController):
        ViewBindingKotlinModel<NewRecipieFloatingButtonBinding>(R.layout.new_recipie_floating_button){
        override fun NewRecipieFloatingButtonBinding.bind() {
            fltBtnRecipieItems.text = title
            fltBtnRecipieItems.setOnClickListener {

                val v = controller.inflater.inflate(R.layout.add_step, null)
                val step = v.findViewById<EditText>(R.id.et_new_recipie_step)
                val add_dialog = AlertDialog.Builder(controller.inflater.context)
                add_dialog.setView(v)
                add_dialog.setPositiveButton("Ok"){
                        dialog,_->
                    controller.addStep(step.text.toString())
                    Toast.makeText(controller.inflater.context, "Adding Step", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                add_dialog.setNegativeButton("Cancel"){
                        dialog,_->
                    Toast.makeText(controller.inflater.context, "Cancel", Toast.LENGTH_SHORT).show()
                }
                add_dialog.create()
                add_dialog.show()
            }
        }
    }

    // Save Recipie Button
    data class SaveRecipieEpoxyModel(val title: String, var controller: NewRecipieController):
        ViewBindingKotlinModel<NewRecipieSaveBtnBinding>(R.layout.new_recipie_save_btn){
        override fun NewRecipieSaveBtnBinding.bind() {
            btnSaveNewRecipie.text = title
            btnSaveNewRecipie.setOnClickListener {
                controller.saveRecipie()
                it.findNavController().navigate(R.id.action_nav_new_recipie_to_nav_recipies)
            }
        }
    }
}