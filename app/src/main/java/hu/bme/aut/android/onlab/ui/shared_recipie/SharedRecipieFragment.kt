package hu.bme.aut.android.onlab.ui.shared_recipie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.withStarted
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.group
import com.airbnb.mvrx.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import hu.bme.aut.android.onlab.*
import hu.bme.aut.android.onlab.R
import hu.bme.aut.android.onlab.data.Recipie
import hu.bme.aut.android.onlab.databinding.FragmentSharedRecipieBinding
import java.io.Serializable

class SharedRecipieFragment: Fragment(), MavericksView {
    private val sharedrecipieViewModel: SharedRecipieViewModel by fragmentViewModel()
    private var _binding: FragmentSharedRecipieBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSharedRecipieBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun invalidate() = withState(sharedrecipieViewModel){ state ->

        binding.ervRecipie.withModels {
            val rec = state.recipeRequest() ?: return@withModels

            // Header
            sharedRecipieHeader {
                id("header")
                recipie(rec)
                onClickUnshareButton { _ ->
                    sharedrecipieViewModel.deleteShare(rec.name, requireContext())
                    findNavController().navigate(R.id.action_nav_shared_recipie_to_nav_shares)
                }
            }

            // Informations par 1
            sharedRecipieInformations {
                id("info")
                recipie(rec)
//                recipiePhoto(sharedrecipieViewModel.getPhoto(rec, inflater))
            }

            // Chipgroup
            group {
                id("chipgroup")
                layout(R.layout.epoxy_recipie_chigroup)
                if (rec.flags != null) {
                    for (flag in rec.flags!!) {
                        recipieChipitem {
                            id(flag)
                            title(flag)
//                            val chip = Chip(context)
//                            chip.text = flag
//                            chip.isCheckable = false
//                            chip.isChecked = true
//                            chip.isClickable = false
                        }
                    }

                }
            }

            // Informations part 2
            recipieCookingInfo {
                id("cooking_info")
                recipie(rec)
            }

            // Ingredient items
            rec.ingredients?.forEach { ingr ->
                recipeIngredientItem {
                    id(ingr.key)
                    ingredientTextView(ingr.key)
                    quantityTextView(ingr.value)
                }
            }

            // Prep text
            recipiePreparationText {
                id("prep_text")
            }

            // Preparation items
            rec.steps?.forEach{ step ->
                item {
                    id(step)
                    itemTextView(step)
                }
            }

            // Add Cart Button
            sharedRecipieFltBtn {
                id("add_cart")
                onClickAddCart { _ ->
                    sharedrecipieViewModel.saveCart(rec, requireContext())
                }
            }
        }
    }
}