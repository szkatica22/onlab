package hu.bme.aut.android.onlab.ui.new_recipie

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import com.airbnb.epoxy.EpoxyController
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.type.Color
import hu.bme.aut.android.onlab.R
import hu.bme.aut.android.onlab.R.color.*
import hu.bme.aut.android.onlab.databinding.*
import hu.bme.aut.android.onlab.ui.change_recipie.ChangeItem
import hu.bme.aut.android.onlab.ui.change_recipie.ChangeRecipieController
import hu.bme.aut.android.onlab.ui.epoxy.ViewBindingKotlinModel

class NewRecipieController(private var recipie: NewItem, private var ingredients: ArrayList<String>,
    private var steps: ArrayList<String>, private var flags: ArrayList<String>,
    private var btn_ingredient: String, private var prep_title: String, private var btn_step: String,
    private var btn_save: String, private val inflater: LayoutInflater ) : EpoxyController() {

    private val title: String = ""

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

    override fun buildModels() {
        HeaderEpoxyModel(recipie).id(recipie.title).addTo(this)
        InformationsEpoxyModel(flags, title).id(title).addTo(this)
        if(!ingredients.isEmpty()){
            ingredients.forEach { item ->
                IngredientEpoxyModel(ingredients, item, this).id(item).addTo(this)
            }
        }
        IngrFloatingButtonEpoxyController(btn_ingredient, this).id(btn_ingredient).addTo(this)

        PreparationTextEpoxyController(prep_title).id(prep_title).addTo(this)
        if(!steps.isEmpty()){
            steps.forEach { item ->
                PreparationEpoxyModel(steps, item, this).id(item).addTo(this)
            }
        }
        StepFloatingButtonEpoxyController(btn_step, this).id(btn_step).addTo(this)

        SaveRecipieEpoxyModel(btn_save).id(btn_save).addTo(this)

    }

    // Data classes

    // Header
    data class HeaderEpoxyModel(var recipie: NewItem):
    ViewBindingKotlinModel<NewRecipieHeaderBinding>(R.layout.new_recipie_header){
        @SuppressLint("ResourceAsColor")
        override fun NewRecipieHeaderBinding.bind() {
            imgBtnCancel.setOnClickListener {
                it.findNavController().navigate(R.id.action_nav_new_recipie_to_nav_flag)
            }
            // TODO: lecsekkolni ez igy jo-e => nem valami jo - kijavitani!!!
//            imgBtnFavourite.setOnClickListener {
//                if (!recipie.favourite){
//                    recipie.favourite = true
//                    imgBtnFavourite.setBackgroundColor(design_default_color_secondary_variant)
//                } else {
//                    recipie.favourite = false
//                    imgBtnFavourite.setBackgroundColor(design_default_color_surface)
//                }
//
//            }
        }
    }

    // Information
    data class InformationsEpoxyModel(val flags: ArrayList<String>, val title: String):
        ViewBindingKotlinModel<NewRecipieInformationsBinding>(R.layout.new_recipie_informations){
        override fun NewRecipieInformationsBinding.bind() {
            var chipGroup: ChipGroup = cgRecipieFlags
            chipGroup.removeAllViews()
            for (i in 0 until flags.size) {
                val chip = Chip(chipGroup.context)
                chip.text = flags[i]
                chip.isClickable = true
                chip.isCheckable = true
                chipGroup.addView(chip)
            }
        }
    }

    // Ingredients
    data class IngredientEpoxyModel(var ingredients: ArrayList<String>, val ingredient: String, var controller: NewRecipieController):
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
    data class PreparationEpoxyModel(val steps: ArrayList<String>, val step: String, var controller: NewRecipieController):
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
    data class SaveRecipieEpoxyModel(val title: String):
        ViewBindingKotlinModel<NewRecipieSaveBtnBinding>(R.layout.new_recipie_save_btn){
        override fun NewRecipieSaveBtnBinding.bind() {
            btnSaveNewRecipie.text = title
            btnSaveNewRecipie.setOnClickListener {
                it.findNavController().navigate(R.id.action_nav_new_recipie_to_nav_flag)
            }
        }
    }
}