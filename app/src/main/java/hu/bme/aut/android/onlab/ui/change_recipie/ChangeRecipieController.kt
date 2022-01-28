package hu.bme.aut.android.onlab.ui.change_recipie

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import com.airbnb.epoxy.EpoxyController
import hu.bme.aut.android.onlab.R
import hu.bme.aut.android.onlab.databinding.*
import hu.bme.aut.android.onlab.ui.epoxy.ViewBindingKotlinModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import hu.bme.aut.android.onlab.data.Recipie

class ChangeRecipieController(
    private val context: Context?,
    private var tmp_rec: Recipie,
    private var btn_ingredient: String,
    private var prep_title: String,
    private var btn_step: String,
    private var btn_delete: String,
    private var btn_save: String,
    private val inflater: LayoutInflater
) : EpoxyController() {

    private val db = Firebase.firestore
    private val original_rec_name = tmp_rec.name
    private var ingredients: ArrayList<String> = tmp_rec.ingredients as ArrayList<String>
    private var ingr_quantities: ArrayList<String> = tmp_rec.ingr_quantities as ArrayList<String>
    private var steps: ArrayList<String> = tmp_rec.steps as ArrayList<String>
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
                tmp_rec.name = s.toString()
            }
        }
    }

    private val time_watcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            tmp_rec.time = s.toString()
        }
    }

    private val abundance_watcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            tmp_rec.abundance = s.toString()
        }
    }

    fun addIngredient(item: String, quantity: String?) {
        ingredients.add(item)
        if(quantity != null){
            ingr_quantities.add(quantity)
        }
        requestModelBuild()
    }

    fun deleteIngredient(idx: Int) {
        ingredients.removeAt(idx)
        ingr_quantities.removeAt(idx)
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

    fun updateLists(){
        tmp_rec.ingredients = ingredients
        tmp_rec.ingr_quantities = ingr_quantities
        tmp_rec.steps = steps
    }

    fun addChip(chip_name: String){
        choosed_chips.add(chip_name)
    }
    fun removeChip(chip_name: String){
        choosed_chips.remove(chip_name)
    }

    fun deleteRecipie(){
        db.collection("recipies").whereEqualTo("name", original_rec_name).get().
        addOnSuccessListener { snapshot ->
            if(snapshot != null){
                for(doc in snapshot.documents){
                    db.collection("recipies").document(doc.id).delete()
                    Toast.makeText(inflater.context, "Recipe deleted", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun saveChanges(view: View) {
        updateLists()
        tmp_rec.flags = choosed_chips
        db.collection("recipies").whereEqualTo("name", original_rec_name).get().
        addOnSuccessListener { snapshot ->
            if(snapshot != null){
                if(snapshot.documents.isNotEmpty()){
                    db.collection("recipies").document(snapshot.documents[0].id).
                    update("abundance", tmp_rec.abundance, "favourite",
                        tmp_rec.favourite, "flags", tmp_rec.flags, "imageUrls", tmp_rec.imageUrls,
                        "ingredients", tmp_rec.ingredients, "ingr_quantities", tmp_rec.ingr_quantities,
                        "name", tmp_rec.name, "steps", tmp_rec.steps, "time", tmp_rec.time)

                    val bundle = Bundle()
                    bundle.putString("recipiename", tmp_rec.name)
                    view.findNavController().navigate(R.id.action_nav_change_recipie_to_nav_recipie, bundle)

                    Toast.makeText(inflater.context, "Changes saved", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    override fun buildModels() {
        HeaderEpoxyModel(this).id(original_rec_name).addTo(this)
        InformationsEpoxyModel(this).id(tmp_rec.time).addTo(this)
        if(!ingredients.isEmpty()){
            ingredients.forEach{ item ->
                val idx = ingredients.indexOf(item)
                IngredientEpoxyModel(item, ingr_quantities[idx], this).id(item).addTo(this)
            }
        }

        IngrFloatingButtonEpoxyController(btn_ingredient, this).id(btn_ingredient).addTo(this)

        PreparationTextEpoxyController(prep_title).id(prep_title).addTo(this)
        if(!steps.isEmpty()){
            steps.forEach{ item ->
                PreparationEpoxyModel(item, this).id(item).addTo(this)
            }
        }

        StepFloatingButtonEpoxyController(btn_step, this).id(btn_step).addTo(this)

        SaveRecipieEpoxyModel(btn_save, this).id(btn_save).addTo(this)

        DeleteRecipieEpoxyModel(btn_delete, this).id(btn_delete).addTo(this)

    }

    // Data classes

    // Header
    data class HeaderEpoxyModel(
        val controller: ChangeRecipieController
    ):
        ViewBindingKotlinModel<ChangeRecipieHeaderBinding>(R.layout.change_recipie_header){
            override fun ChangeRecipieHeaderBinding.bind() {
                etChangeRecipieName.setText(controller.original_rec_name)
                etChangeRecipieName.addTextChangedListener(controller.name_watcher)
                imgBtnEdit.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putString("recipiename", controller.original_rec_name)
                    it.findNavController().navigate(R.id.action_nav_change_recipie_to_nav_recipie, bundle)
                }
                if (controller.tmp_rec.favourite == true){
                    imgBtnFavourite.setImageResource(R.drawable.ic_menu_favourites)
                }
                // Check & set favourite boolean
                imgBtnFavourite.setOnClickListener {
                    if (controller.tmp_rec.favourite == true){
                        controller.tmp_rec.favourite = false
                        imgBtnFavourite.setImageResource(R.drawable.ic_recipie_favorite_border)
                    } else {
                        controller.tmp_rec.favourite = true
                        imgBtnFavourite.setImageResource(R.drawable.ic_menu_favourites)
                    }
                }
            }
    }

    // Information
    data class InformationsEpoxyModel(val controller: ChangeRecipieController):
    ViewBindingKotlinModel<ChangeRecipieInformationsBinding>(R.layout.change_recipie_informations){
        override fun ChangeRecipieInformationsBinding.bind() {
            etRecipieTime.setText(controller.tmp_rec.time)
            etRecipieTime.addTextChangedListener(controller.time_watcher)
            etRecipieAbundance.setText(controller.tmp_rec.abundance)
            etRecipieAbundance.addTextChangedListener(controller.abundance_watcher)

            val chipGroup: ChipGroup = cgRecipieFlags
            chipGroup.removeAllViews()
            controller.db.collection("flags").get().addOnSuccessListener { snapshots ->
                if(snapshots != null){
                    for(snap in snapshots.documents){
                        val chip = Chip(chipGroup.context)
                        chip.text = snap.get("name").toString()
                        chip.isClickable = true
                        chip.isCheckable = true
                        chipGroup.addView(chip)

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
        }
    }

    // Ingredients
    data class IngredientEpoxyModel(val ingredient: String, val quantity: String,
                                    var controller: ChangeRecipieController):
    ViewBindingKotlinModel<ChangeRecipieItemBinding>(R.layout.change_recipie_item){
        override fun ChangeRecipieItemBinding.bind() {
            tvChangeRecipieItemTitleId.text = ingredient
            tvChangeRecipieItemQuantity.text = quantity
            ivChangeRecipieDelete.setOnClickListener {
                controller.deleteIngredient(controller.ingredients.indexOf(ingredient))

            }
        }
    }

    // Ingredients Button
    data class IngrFloatingButtonEpoxyController(val title: String, var controller: ChangeRecipieController):
    ViewBindingKotlinModel<ChangeRecipieFloatingButtonBinding>(R.layout.change_recipie_floating_button){
        override fun ChangeRecipieFloatingButtonBinding.bind() {
            fltBtnRecipieItems.text = title
            fltBtnRecipieItems.setOnClickListener {

                val v = controller.inflater.inflate(R.layout.add_ingredient, null)
                val ingredient = v.findViewById<EditText>(R.id.et_new_recipie_ingredient)
                val quantity = v.findViewById<EditText>(R.id.et_new_recipie_quantity)
                var info: String? = null

                val units_array = controller.context?.resources?.getStringArray(R.array.units_array)
//                val array_adapter = ArrayAdapter(controller.context, R.id.spinner_unit, units)
                Log.d("ARRAY: ", units_array.toString())

                var unit = v.findViewById<Spinner>(R.id.spinner_unit)
                unit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        info = quantity.text.toString() + " -"
                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,
                                                position: Int, id: Long) {
                        Log.d("CHOSE: ", units_array?.get(position).toString())
                        info = quantity.text.toString() + " " + units_array?.get(position).toString()
                    }
                }

                val add_dialog = AlertDialog.Builder(controller.inflater.context)
                add_dialog.setView(v)
                add_dialog.setPositiveButton("Ok"){
                    dialog,_->
                    controller.addIngredient(ingredient.text.toString(), info)
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

    // Preparation Text
    data class PreparationTextEpoxyController(val title: String):
        ViewBindingKotlinModel<ChangeRecipiePreparationTextBinding>(R.layout.change_recipie_preparation_text){
        override fun ChangeRecipiePreparationTextBinding.bind() {
            tvChangeRecipiePrepTitle.text = title
        }

    }

    // Preparation Steps
    data class PreparationEpoxyModel(val step: String, var controller: ChangeRecipieController):
    ViewBindingKotlinModel<ChangeRecipieStepBinding>(R.layout.change_recipie_step){
        override fun ChangeRecipieStepBinding.bind(){
            tvChangeRecipieStepTitleId.text = step
            ivChangeRecipieDelete.setOnClickListener {
                controller.deleteSteps(controller.steps.indexOf(step))
            }
        }
    }

    // Preparation Step Button
    data class StepFloatingButtonEpoxyController(val title: String, var controller: ChangeRecipieController):
        ViewBindingKotlinModel<ChangeRecipieFloatingButtonBinding>(R.layout.change_recipie_floating_button){
        override fun ChangeRecipieFloatingButtonBinding.bind() {
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

    // Save Button
    data class SaveRecipieEpoxyModel(val title: String, val controller: ChangeRecipieController):
        ViewBindingKotlinModel<ChangeRecipieSaveBtnBinding>(R.layout.change_recipie_save_btn){
        override fun ChangeRecipieSaveBtnBinding.bind() {
            btnSaveRecipie.text = title
            btnSaveRecipie.setOnClickListener {
                controller.saveChanges(it)
            }
        }
    }

    // Delete Button
    data class DeleteRecipieEpoxyModel(val title: String, val controller: ChangeRecipieController):
    ViewBindingKotlinModel<ChangeRecipieDeleteBtnBinding>(R.layout.change_recipie_delete_btn){
        override fun ChangeRecipieDeleteBtnBinding.bind() {
            btnDeleteRecipie.text = title
            btnDeleteRecipie.setOnClickListener {
                controller.deleteRecipie()
                it.findNavController().navigate(R.id.action_nav_change_recipie_to_nav_recipies)
            }
        }
    }
}
