package hu.bme.aut.android.onlab.ui.new_recipie

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.airbnb.epoxy.group
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import hu.bme.aut.android.onlab.*
import hu.bme.aut.android.onlab.databinding.FragmentNewRecipieBinding

class NewRecipieFragment : Fragment(), MavericksView{

    private val newRecipieViewModel: NewRecipieViewModel by fragmentViewModel()
    private var _binding: FragmentNewRecipieBinding? = null

    private val binding get() = _binding!!
    val db = Firebase.firestore


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewRecipieBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun invalidate() = withState(newRecipieViewModel) { state ->
        binding.ervChangeRecipie.withModels {
            val rec = state.changedRecipie ?: state.recipeRequest() ?: return@withModels

            // Header
            newRecipieHeader {
                id("header")
                viewmodel(newRecipieViewModel)

                onClickCancelButton { _ ->
                    findNavController().navigate(R.id.action_nav_new_recipie_to_nav_recipies)
                }
                onClickFavouriteButton { _ ->
                    newRecipieViewModel.checkFavourite()
                }
            }

            // Photos
            // TODO

            // Chipgroup title
            chipgroupText {
                id("chipgroup_title")
            }

            // Chipgroup
            group {
                id("chipgroup")
                layout(R.layout.epoxy_recipie_chigroup)
                if(rec.flags != null) {
                    for (flag in rec.flags!!) {
                        newRecipieChipitem {
                            id(flag)
                            title(flag)
                            viewmodel(newRecipieViewModel)
                        }
                    }
                }
            }

            // Informations
            newRecipieInformations {
                id("info")
                viewmodel(newRecipieViewModel)
            }

            // Ingredient items
            rec.ingredients?.forEach { ingr ->
                newRecipieItem {
                    id(ingr.key)
                    ingredientTextView(ingr.key)
                    quantityTextView(ingr.value)
                    onClickDeleteButton { _ ->
                        newRecipieViewModel.deleteIngredient(ingr.key!!)
                    }
                }
            }

            // Add ingredient button
            newRecipieFloatingButton {
                id("add_ingr_btn")
                title(getString(R.string.btn_add_new_ingredient))
                onClickFltButton { _ ->
                    newRecipieViewModel.addIngredientDialog(requireContext())
                }
            }

            // Prep text
            recipiePreparationText {
                id("prep_text")
            }

            // Preparation items
            rec.steps?.forEach{ step ->
                newRecipiePrepItem {
                    id(step)
                    item(step)
                    onClickDeleteButton { _ ->
                        newRecipieViewModel.deleteSteps(step!!)
                    }
                }
            }

            // Add prep item button
            newRecipieFloatingButton {
                id("add_prep_item_btn")
                title(getString(R.string.btn_add_new_step))
                onClickFltButton { _ ->
                    newRecipieViewModel.addPrepDialog(requireContext())
                }
            }

            // Save button
            newRecipieSaveBtn {
                id("save")
                onClickSave { _ ->
                    newRecipieViewModel.checkChips(requireContext())//saveRecipie(requireContext())
                    findNavController().navigate(R.id.action_nav_new_recipie_to_nav_recipies)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}