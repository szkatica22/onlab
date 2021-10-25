package hu.bme.aut.android.onlab.ui.change_recipie

import com.airbnb.epoxy.EpoxyController
import hu.bme.aut.android.onlab.R
import hu.bme.aut.android.onlab.databinding.ChangeRecipieHeaderBinding
import hu.bme.aut.android.onlab.databinding.ChangeRecipieInformationsBinding
import hu.bme.aut.android.onlab.databinding.ChangeRecipieItemBinding
import hu.bme.aut.android.onlab.databinding.ChangeRecipieStepBinding
import hu.bme.aut.android.onlab.ui.epoxy.ViewBindingKotlinModel

class ChangeRecipieController(private var ingredients: ArrayList<ChangeItem>,
    private var steps: ArrayList<ChangeItem>/*, private val recipie_name: String*/) : EpoxyController() {

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
        if(ingredients.isEmpty()){
            return
        }
        ingredients.forEach{ item ->
            IngredientEpoxyModel(item).id(item.title).addTo(this)
        }
        if(steps.isEmpty()){
            return
        }
        steps.forEach{ item ->
            PreparationEpoxyModel(item).id(item.title).addTo(this)
        }
//        HeaderEpoxyModel(recipie_name).id(recipie_name)

    }

//    data class HeaderEpoxyModel(val title: String):
//        ViewBindingKotlinModel<ChangeRecipieHeaderBinding>(R.layout.){
//            override fun ChangeRecipieHeaderBinding.bind() {
//                tvChangeRecipieName.text = title
//            }
//    }

//    data class InformationsEpoxyModel():
//    ViewBindingKotlinModel<ChangeRecipieInformationsBinding>(){
//        override fun ChangeRecipieInformationsBinding.bind() {
//
//        }
//    }

    data class IngredientEpoxyModel(val ingredient: ChangeItem):
    ViewBindingKotlinModel<ChangeRecipieItemBinding>(R.layout.change_recipie_item){
        override fun ChangeRecipieItemBinding.bind() {
            tvChangeRecipieItemTitleId.text = ingredient.title
        }
    }

    data class PreparationEpoxyModel(val step: ChangeItem):
    ViewBindingKotlinModel<ChangeRecipieStepBinding>(R.layout.change_recipie_step){
        override fun ChangeRecipieStepBinding.bind(){
            tvChangeRecipieStepTitleId.text = step.title
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