package hu.bme.aut.android.onlab.ui.recipie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.airbnb.epoxy.group
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import hu.bme.aut.android.onlab.*
import hu.bme.aut.android.onlab.databinding.FragmentRecipieBinding

class RecipieFragment: Fragment(), MavericksView {
    private val recipieViewModel: RecipieViewModel by fragmentViewModel()
    private var _binding: FragmentRecipieBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRecipieBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun invalidate() = withState(recipieViewModel){ state ->

        binding.ervRecipie.withModels {
            val rec = state.recipeRequest() ?: return@withModels
            // Header
            recipieHeader {
                id("header")
                recipie(rec)
                onClickDeleteButton { _ ->
                    recipieViewModel.deleteRecipie(rec, requireContext())
                    findNavController().navigate(R.id.action_nav_recipie_to_nav_recipies)
                }
                onClickEditButton { _ ->
                    findNavController()
                        .navigate(
                            R.id.action_nav_recipie_to_nav_change_recipie,
                            recipieViewModel.getArgs(rec)
                        )
                }
            }

            // Informations part 1
            recipieInformations {
                id("info")
                recipie(rec)
//                recipiePhoto(recipieViewModel.getPhoto(rec, requireContext()))
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
            rec.steps?.forEach { step ->
                item {
                    id(step)
                    itemTextView(step)
                }
            }

            // Share & Add Cart Buttons
            addShopListFltBtn {
                id("buttons")
                onClickShare { _ ->
                    recipieViewModel.shareDialog(rec, requireContext())
                }

                onClickAddCart { _ ->
                    recipieViewModel.saveCart(rec, requireContext())
                }
            }
        }
    }
}