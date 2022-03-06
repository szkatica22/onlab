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
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import hu.bme.aut.android.onlab.*
import hu.bme.aut.android.onlab.data.Recipie
import hu.bme.aut.android.onlab.databinding.FragmentSharedRecipieBinding

class SharedRecipieFragment: Fragment(), MavericksView {
    private val sharedrecipieViewModel: SharedRecipieViewModel by fragmentViewModel()
    private var _binding: FragmentSharedRecipieBinding? = null

    private val binding get() = _binding!!
    private var db = Firebase.firestore
    private lateinit var rec: Recipie

    private lateinit var sharedRecipieController: SharedRecipieController
    private var prep_title: String = "Preparation"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        sharedrecipieViewModel =
//            ViewModelProvider(this).get(SharedRecipieViewModel::class.java)

        _binding = FragmentSharedRecipieBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val rec_name = this.arguments?.get("recipiename").toString()
        rec = sharedrecipieViewModel.getRecipie()

        binding.ervRecipie.withModels {
            // Header
            sharedRecipieHeader {
                recipie(rec)
                onClickUnshareButton { _ ->
                    sharedrecipieViewModel.deleteShare(rec.name, inflater)
                    findNavController().navigate(R.id.action_nav_shared_recipie_to_nav_shares)
                }
            }

            // Informations par 1
            sharedRecipieInformations {
                recipie(rec)
                recipiePhoto(sharedrecipieViewModel.getPhoto(rec, inflater))
            }

            // Chipgroup
            group {
                id("chipgroup")
                layout(R.layout.epoxy_recipie_chigroup)
                recipieChipitem {
                    if(rec.flags != null){
                        for (flag in rec.flags!!){
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
                recipie(rec)
            }

            // Ingredient items
            rec.ingredients?.forEach { ingr ->
                recipeIngredientItem {
                    ingredientTextView(ingr.key)
                    quantityTextView(ingr.value)
                }
            }

            // Preparation items
            rec.steps?.forEach{ step ->
                item {
                    itemTextView(step)
                }
            }

            // Add Cart Button
            addShopListFltBtn {
                onClickAddCart { _ ->
                    sharedrecipieViewModel.saveCart(rec, inflater)
                }
            }
        }

//        db.collection("recipies").whereEqualTo("name", rec_name).get().
//        addOnSuccessListener { snapshot ->
//            if(snapshot.documents.isNotEmpty()){
//                val tmp_data = snapshot.documents[0].data
//                val tmp_rec = Recipie(rec_name, tmp_data?.get("favourite") as Boolean,
//                    tmp_data.get("flags") as List<String?>?, tmp_data.get("imageUrls") as List<String?>?,
//                    tmp_data.get("time").toString(), tmp_data.get("abundance").toString(),
//                    tmp_data.get("author").toString(), tmp_data.get("ingredients") as Map<String?, String?>?,
//                    tmp_data.get("steps") as List<String?>?, tmp_data.get("shares") as List<String?>?)
//
//                sharedRecipieController = SharedRecipieController(this.context, tmp_rec, prep_title, inflater)
//                binding.ervRecipie.setController(sharedRecipieController)
//
//                sharedRecipieController.requestModelBuild()
//                binding.ervRecipie.addItemDecoration(DividerItemDecoration(requireActivity(),
//                    RecyclerView.VERTICAL))
//            }
//        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun invalidate() = withState(sharedrecipieViewModel){ state ->
        TODO("Not yet implemented")
    }
}