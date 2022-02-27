package hu.bme.aut.android.onlab.ui.recipie

import android.annotation.SuppressLint
import com.airbnb.epoxy.*
import com.google.android.material.chip.Chip
import hu.bme.aut.android.onlab.R

@SuppressLint("NonConstantResourceId")
@EpoxyModelClass(layout = R.layout.epoxy_recipie_chipgroup)
abstract class ChipGroup(models: List<EpoxyModel<*>>) :
    EpoxyModelGroup(R.layout.epoxy_recipie_chipgroup) {

        @EpoxyAttribute
        lateinit var title: String

//        override fun bind(holder: ModelGroupHolder) {
//            super.bind(holder)
//            holder.rootView.findViewById<Chip>(R.id.epoxy_model_group_child_container).c
//        }


}