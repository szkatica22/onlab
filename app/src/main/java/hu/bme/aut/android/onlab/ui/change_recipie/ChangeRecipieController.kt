package hu.bme.aut.android.onlab.ui.change_recipie

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import com.airbnb.epoxy.EpoxyController
import hu.bme.aut.android.onlab.R
import hu.bme.aut.android.onlab.databinding.*
import hu.bme.aut.android.onlab.ui.epoxy.ViewBindingKotlinModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class ChangeRecipieController(private var ingredients: ArrayList<ChangeItem>,
    private var steps: ArrayList<ChangeItem>, private val recipie_name: String,
    private var flags: ArrayList<String>, private var time: String,
    private var abundance: String, private var btn_ingredient: String, private var prep_title: String,
    private var btn_step: String, private var btn_delete: String, private var btn_save: String, private val inflater: LayoutInflater
    ) : EpoxyController() {

    fun addIngredient(item: ChangeItem) {
        ingredients.add(item)
        requestModelBuild()
    }

    fun deleteIngredient(idx: Int) {
        ingredients.removeAt(idx)
        requestModelBuild()
    }

    fun addStep(item: ChangeItem) {
        steps.add(item)
        requestModelBuild()
    }

    fun deleteSteps(idx: Int) {
        steps.removeAt(idx)
        requestModelBuild()
    }

    override fun buildModels() {
        HeaderEpoxyModel(recipie_name).id(recipie_name).addTo(this)
        InformationsEpoxyModel(flags, time, abundance).id(time).addTo(this)
        if(ingredients.isEmpty()){
            return
        }
        ingredients.forEach{ item ->
            IngredientEpoxyModel(ingredients, item, this).id(item.title).addTo(this)
        }
        IngrFloatingButtonEpoxyController(btn_ingredient, this).id(btn_ingredient).addTo(this)

        PreparationTextEpoxyController(prep_title).id(prep_title).addTo(this)
        if(steps.isEmpty()){
            return
        }
        steps.forEach{ item ->
            PreparationEpoxyModel(steps, item, this).id(item.title).addTo(this)
        }
        StepFloatingButtonEpoxyController(btn_step, this).id(btn_step).addTo(this)

        SaveRecipieEpoxyModel(btn_save).id(btn_save).addTo(this)

        DeleteRecipieEpoxyModel(btn_delete).id(btn_delete).addTo(this)

    }

    // Data classes

    data class HeaderEpoxyModel(val title: String):
        ViewBindingKotlinModel<ChangeRecipieHeaderBinding>(R.layout.change_recipie_header){
            override fun ChangeRecipieHeaderBinding.bind() {
                etChangeRecipieName.setText(title)
                imgBtnEdit.setOnClickListener {
                    it.findNavController().navigate(R.id.action_nav_change_recipie_to_nav_recipie)
                }
            }
    }

    data class InformationsEpoxyModel(val flags: ArrayList<String>, val time: String, val abundance: String, ):
    ViewBindingKotlinModel<ChangeRecipieInformationsBinding>(R.layout.change_recipie_informations){
        override fun ChangeRecipieInformationsBinding.bind() {
            etRecipieTime.setText(time)
            etRecipieAbundance.setText(abundance)

            var chipGroup: ChipGroup = cgRecipieFlags
            chipGroup.removeAllViews()
            for (i in 0 until flags.size) {
                val chip = Chip(chipGroup.context)
                chip.text = flags[i]
                chip.isClickable = true
                chip.isCheckable = true
//                chip.setCheckedIconTintResource(Color.parseColor("396200EE").toInt())
//                chip.isCloseIconVisible = false
                chipGroup.addView(chip)
            }
        }
    }

    data class IngredientEpoxyModel(var ingredients: ArrayList<ChangeItem>, val ingredient: ChangeItem, var controller: ChangeRecipieController):
    ViewBindingKotlinModel<ChangeRecipieItemBinding>(R.layout.change_recipie_item){
        override fun ChangeRecipieItemBinding.bind() {
            tvChangeRecipieItemTitleId.text = ingredient.title
            ivChangeRecipieDelete.setOnClickListener {
//                Log.d("DELETE:", "Delete ingredient item")
//                ingredients.remove(ingredient)
//                llRecipieIngredients.removeView(llRecipieIngredients)
                controller.deleteIngredient(ingredients.indexOf(ingredient))

            }
        }
    }

    data class IngrFloatingButtonEpoxyController(val title: String, var controller: ChangeRecipieController): // TODO: INGREDIENT HOZZAADAS ITT
    ViewBindingKotlinModel<ChangeRecipieFloatingButtonBinding>(R.layout.change_recipie_floating_button){
        override fun ChangeRecipieFloatingButtonBinding.bind() {
            fltBtnRecipieItems.text = title
            fltBtnRecipieItems.setOnClickListener {

                val v = controller.inflater.inflate(R.layout.add_ingredient, null)
                val ingredient = v.findViewById<EditText>(R.id.et_new_recipie_ingredient)
                val add_dialog = AlertDialog.Builder(controller.inflater.context)
                add_dialog.setView(v)
                add_dialog.setPositiveButton("Ok"){
                    dialog,_->
                    controller.addIngredient(ChangeItem(ingredient.text.toString()))
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

    data class StepFloatingButtonEpoxyController(val title: String, var controller: ChangeRecipieController): // TODO: STEP HOZZAADAS ITT
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
                    controller.addStep(ChangeItem(step.text.toString()))
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

    data class PreparationTextEpoxyController(val title: String):
        ViewBindingKotlinModel<ChangeRecipiePreparationTextBinding>(R.layout.change_recipie_preparation_text){
        override fun ChangeRecipiePreparationTextBinding.bind() {
            tvChangeRecipiePrepTitle.text = title
        }

    }

    data class PreparationEpoxyModel(val steps: ArrayList<ChangeItem>, val step: ChangeItem, var controller: ChangeRecipieController):
    ViewBindingKotlinModel<ChangeRecipieStepBinding>(R.layout.change_recipie_step){
        override fun ChangeRecipieStepBinding.bind(){
            tvChangeRecipieStepTitleId.text = step.title
            ivChangeRecipieDelete.setOnClickListener {
                controller.deleteSteps(steps.indexOf(step))
            }
        }
    }

    data class SaveRecipieEpoxyModel(val title: String):
        ViewBindingKotlinModel<ChangeRecipieSaveBtnBinding>(R.layout.change_recipie_save_btn){
        override fun ChangeRecipieSaveBtnBinding.bind() {
            btnSaveRecipie.text = title
            btnSaveRecipie.setOnClickListener {
                it.findNavController().navigate(R.id.action_nav_change_recipie_to_nav_recipie)
            }
        }
    }

    data class DeleteRecipieEpoxyModel(val title: String):
    ViewBindingKotlinModel<ChangeRecipieDeleteBtnBinding>(R.layout.change_recipie_delete_btn){
        override fun ChangeRecipieDeleteBtnBinding.bind() {
            btnDeleteRecipie.text = title
            btnDeleteRecipie.setOnClickListener {
                it.findNavController().navigate(R.id.action_nav_change_recipie_to_nav_recipie)
            }
        }
    }



//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChangeIngredientViewHolder {
//
//        return ChangeIngredientViewHolder(
//            ChangeRecipieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        )
//    }
//
//    override fun onBindViewHolder(holder: ChangeIngredientViewHolder, position: Int) {
//        val cur_item = items[position]
//        holder.binding.tvChangeRecipieItemTitleId.text = cur_item.title
////        holder.binding.tvShoppingTitle.text = cur_item.title
//    }
//
//    override fun getItemCount(): Int {
//        return items.size
//    }
}