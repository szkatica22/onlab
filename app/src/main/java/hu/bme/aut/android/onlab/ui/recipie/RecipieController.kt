package hu.bme.aut.android.onlab.ui.recipie

import com.airbnb.epoxy.EpoxyController
import hu.bme.aut.android.onlab.R
import hu.bme.aut.android.onlab.databinding.*
import androidx.navigation.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import hu.bme.aut.android.onlab.ui.epoxy.ViewBindingKotlinModel

class RecipieController (private var ingredients: ArrayList<Item>,
                        private var steps: ArrayList<Item>, private val recipie_name: String,
                        private var flags: ArrayList<String>, private var time: String,
                        private var abundance: String, private var btn_ingredient: String, private var prep_title: String,
                        private var btn_step: String, private var btn_delete: String) : EpoxyController()
{
    override fun buildModels() {
        HeaderEpoxyModel(recipie_name).id(recipie_name).addTo(this)
        InformationsEpoxyModel(flags, time, abundance).id(time).addTo(this)

        if(ingredients.isEmpty()){
            return
        }
        ingredients.forEach{ item ->
            IngredientEpoxyModel(item).id(item.title).addTo(this)
        }
        PreparationTextEpoxyModel(prep_title).id(prep_title).addTo(this)

        if(steps.isEmpty()){
            return
        }
        steps.forEach { item ->
            PreparationEpoxyModel(item).id(item.title).addTo(this)
        }
    }

    // Data classes
    data class HeaderEpoxyModel(val title: String):
        ViewBindingKotlinModel<RecipieHeaderBinding>(R.layout.recipie_header){
        override fun RecipieHeaderBinding.bind() {
            tvRecipieName.text = title
            imgBtnEdit.setOnClickListener {
                it.findNavController().navigate(R.id.action_nav_recipie_to_nav_change_recipie)
            }
        }
    }

    data class InformationsEpoxyModel(val flags: ArrayList<String>, val time: String, val abundance: String):
        ViewBindingKotlinModel<RecipieInformationsBinding>(R.layout.recipie_informations){
        override fun RecipieInformationsBinding.bind() {
            tvRecipieTime.text = time
            tvRecipieAbundance.text = abundance
            var chipGroup: ChipGroup = cgRecipieFlags
            for (i in 0 until flags.size) {
                val chip = Chip(chipGroup.context)
                chip.text = flags[i]
                chip.isClickable = false
                chip.isCheckable = false
                chipGroup.addView(chip)
            }
        }
    }

    data class IngredientEpoxyModel(val ingredient: Item):
            ViewBindingKotlinModel<RecipeIngredientItemBinding>(R.layout.recipe_ingredient_item){
        override fun RecipeIngredientItemBinding.bind() {
            tvIngredient.text = ingredient.title
        }
    }

    data class PreparationTextEpoxyModel(val title: String):
        ViewBindingKotlinModel<RecipiePreparationTextBinding>(R.layout.recipie_preparation_text){
        override fun RecipiePreparationTextBinding.bind() {
            tvRecipiePrepTitle.text = title
        }
    }

    data class PreparationEpoxyModel(val step: Item): //tv_recipie_ingredient_item
        ViewBindingKotlinModel<ItemBinding>(R.layout.item){
        override fun ItemBinding.bind(){
            tvTitle.text = step.title
        }
    }

}