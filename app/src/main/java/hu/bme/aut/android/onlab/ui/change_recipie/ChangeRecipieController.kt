package hu.bme.aut.android.onlab.ui.change_recipie

import com.airbnb.epoxy.EpoxyController
import hu.bme.aut.android.onlab.R
import hu.bme.aut.android.onlab.databinding.*
import hu.bme.aut.android.onlab.ui.epoxy.ViewBindingKotlinModel

class ChangeRecipieController(private var ingredients: ArrayList<ChangeItem>,
    private var steps: ArrayList<ChangeItem>, private val recipie_name: String,
    private var flags: ArrayList<String>, private var time: String,
    private var abundance: String, private var btn_ingredient: String,
    private var btn_step: String, private var btn_delete: String) : EpoxyController() {

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
            IngredientEpoxyModel(item).id(item.title).addTo(this)
        }
        FloatingButtonEpoxyController(btn_ingredient).id(btn_ingredient).addTo(this)

        if(steps.isEmpty()){
            return
        }
        steps.forEach{ item ->
            PreparationEpoxyModel(item).id(item.title).addTo(this)
        }
        FloatingButtonEpoxyController(btn_step).id(btn_step).addTo(this)

        DeleteRecipieEpoxyModel(btn_delete).id(btn_delete).addTo(this)

    }

    data class HeaderEpoxyModel(val title: String):
        ViewBindingKotlinModel<ChangeRecipieHeaderBinding>(R.layout.change_recipie_header){
            override fun ChangeRecipieHeaderBinding.bind() {
                etChangeRecipieName.setText(title)
            }
    }

    data class InformationsEpoxyModel(val flags: ArrayList<String>, val time: String, val abundance: String):
    ViewBindingKotlinModel<ChangeRecipieInformationsBinding>(R.layout.change_recipie_informations){
        override fun ChangeRecipieInformationsBinding.bind() {
            //TODO: megnezni, hogy a flag-es reszt kulon kellene-e csinalni, mondvan eltero lehet, hogy eppen milyen flagek kerulnek bele stb
            etRecipieTime.setText(time)
            etRecipieAbundance.setText(abundance)
            for (i in 0 until flags.size) {
                // TODO: kitalalni itt az egyes chip-eket hogy tudnam megcsinalni stb... (- kulon layout?)
            }
            chip1.text = flags[0]
            chip2.text = flags[1]
            chip3.text = flags[2]
            chip4.text = flags[3]
        }
    }

    data class IngredientEpoxyModel(val ingredient: ChangeItem):
    ViewBindingKotlinModel<ChangeRecipieItemBinding>(R.layout.change_recipie_item){
        override fun ChangeRecipieItemBinding.bind() {
            tvChangeRecipieItemTitleId.text = ingredient.title
        }
    }

    data class FloatingButtonEpoxyController(val title: String):
    ViewBindingKotlinModel<ChangeRecipieFloatingButtonBinding>(R.layout.change_recipie_floating_button){
        override fun ChangeRecipieFloatingButtonBinding.bind() {
            fltBtnRecipieItems.text = title
        }
    }

    data class PreparationEpoxyModel(val step: ChangeItem):
    ViewBindingKotlinModel<ChangeRecipieStepBinding>(R.layout.change_recipie_step){
        override fun ChangeRecipieStepBinding.bind(){
            tvChangeRecipieStepTitleId.text = step.title
        }
    }

    data class DeleteRecipieEpoxyModel(val title: String):
    ViewBindingKotlinModel<ChangeRecipieDeleteBtnBinding>(R.layout.change_recipie_delete_btn){
        override fun ChangeRecipieDeleteBtnBinding.bind() {
            btnDeleteRecipie.text = title
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