package hu.bme.aut.android.onlab.ui.recipie

import com.airbnb.epoxy.EpoxyController
import hu.bme.aut.android.onlab.R
import hu.bme.aut.android.onlab.databinding.*
import androidx.navigation.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import hu.bme.aut.android.onlab.data.Recipie
import hu.bme.aut.android.onlab.ui.epoxy.ViewBindingKotlinModel

class RecipieController (
    private val saved_rec: Recipie,
    private var prep_title: String
) : EpoxyController()
{
    override fun buildModels() {

        HeaderEpoxyModel(saved_rec).id(saved_rec.name).addTo(this)
        InformationsEpoxyModel(saved_rec).id(saved_rec.time).addTo(this)

        if(saved_rec.ingredients?.isEmpty()!!){
            return
        }
        saved_rec.ingredients!!.forEach{ item ->
            IngredientEpoxyModel(item).id(item).addTo(this)
        }
        PreparationTextEpoxyModel(prep_title).id(prep_title).addTo(this)

        if(saved_rec.steps?.isEmpty()!!){
            return
        }
        saved_rec.steps!!.forEach { item ->
            PreparationEpoxyModel(item).id(item).addTo(this)
        }
    }

    // Data classes
    data class HeaderEpoxyModel(val saved_rec: Recipie):
        ViewBindingKotlinModel<RecipieHeaderBinding>(R.layout.recipie_header){
        override fun RecipieHeaderBinding.bind() {
            tvRecipieName.text = saved_rec.name
            imgBtnEdit.setOnClickListener {
                it.findNavController().navigate(R.id.action_nav_recipie_to_nav_change_recipie)
            }
            if(saved_rec.favourite == true){
                imgBtnFavourite.setImageResource(R.drawable.ic_menu_favourites)
            }
        }
    }

    data class InformationsEpoxyModel(val saved_rec: Recipie):
        ViewBindingKotlinModel<RecipieInformationsBinding>(R.layout.recipie_informations){
        override fun RecipieInformationsBinding.bind() {
            tvRecipieTime.text = saved_rec.time
            tvRecipieAbundance.text = saved_rec.abundance
            var chipGroup: ChipGroup = cgRecipieFlags
            chipGroup.removeAllViews()
            if(saved_rec.flags != null){
                for (element in saved_rec.flags!!) {
                    val chip = Chip(chipGroup.context)
                    chip.text = element
                    chip.isChecked = true
                    chip.isClickable = false
                    chip.isCheckable = false
                    chipGroup.addView(chip)
                }
            }
        }
    }

    data class IngredientEpoxyModel(val ingredient: String?):
            ViewBindingKotlinModel<RecipeIngredientItemBinding>(R.layout.recipe_ingredient_item){
        override fun RecipeIngredientItemBinding.bind() {
            tvIngredient.text = ingredient
        }
    }

    data class PreparationTextEpoxyModel(val title: String):
        ViewBindingKotlinModel<RecipiePreparationTextBinding>(R.layout.recipie_preparation_text){
        override fun RecipiePreparationTextBinding.bind() {
            tvRecipiePrepTitle.text = title
        }
    }

    data class PreparationEpoxyModel(val step: String?):
        ViewBindingKotlinModel<ItemBinding>(R.layout.item){
        override fun ItemBinding.bind(){
            tvTitle.text = step
        }
    }

}

