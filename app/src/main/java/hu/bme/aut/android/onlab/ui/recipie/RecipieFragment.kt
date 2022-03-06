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
import com.google.android.material.chip.Chip
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import hu.bme.aut.android.onlab.*
import hu.bme.aut.android.onlab.data.Recipie
import hu.bme.aut.android.onlab.databinding.FragmentRecipieBinding

class RecipieFragment: Fragment(), MavericksView {
    private val recipieViewModel: RecipieViewModel by fragmentViewModel()
    private var _binding: FragmentRecipieBinding? = null

    private val binding get() = _binding!!
    private var db = Firebase.firestore
    private val firebaseUser: FirebaseUser?
        get() = FirebaseAuth.getInstance().currentUser

    private lateinit var rec: Recipie

    private var other_users: List<String> = emptyList()

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        recipieViewModel =
//            ViewModelProvider(this).get(RecipieViewModel::class.java)

        _binding = FragmentRecipieBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val rec_name = this.arguments?.get("recipiename").toString()
        rec = recipieViewModel.getRecipie()

        binding.ervRecipie.withModels/*(recipieViewModel) { state ->*/ {

            // Header
            recipieHeader {
                recipie(rec)
                onClickDeleteButton { _ ->
                    recipieViewModel.deleteRecipie(rec, inflater)
                    findNavController().navigate(R.id.action_nav_recipie_to_nav_recipies)
                }
                onClickEditButton { _ ->
                    findNavController()
                        .navigate(R.id.action_nav_recipie_to_nav_change_recipie,
                            recipieViewModel.getBundle(rec))
                }
            }

            // Informations part 1
            recipieInformations {
                recipie(rec)
                recipiePhoto(recipieViewModel.getPhoto(rec, inflater))
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

            // Share & Add Cart Buttons
            addShopListFltBtn {
                onClickShare{ _ ->
                    recipieViewModel.shareDialog(rec, inflater)
                }

                onClickAddCart { _ ->
                    recipieViewModel.saveCart(rec, inflater)
                }
            }

        // Save the other users into a list
//        db.collection("recipies").whereNotEqualTo("author", firebaseUser?.email).
//        get().addOnSuccessListener { snapshots ->
//            if(snapshots.documents.isNotEmpty()){
//                for(doc in snapshots.documents){
//                    if(doc["author"] != firebaseUser?.email && !other_users.contains(doc["author"].
//                        toString())){
//                        other_users += doc["author"].toString()
//                    }
//                }
//            }
//            db.collection("recipies").whereEqualTo("name", rec_name).get().
//            addOnSuccessListener { snapshot ->
//                if(snapshot.documents.isNotEmpty()){
//                    val tmp_data = snapshot.documents[0].data
//                    val tmp_rec = Recipie(rec_name, tmp_data?.get("favourite") as Boolean,
//                        tmp_data.get("flags") as List<String?>?, tmp_data.get("imageUrls") as List<String?>?,
//                        tmp_data.get("time").toString(), tmp_data.get("abundance").toString(),
//                        tmp_data.get("author").toString(), tmp_data.get("ingredients") as Map<String?, String?>?,
//                        tmp_data.get("steps") as List<String?>?, tmp_data.get("shares") as List<String?>?)
//
////                    recipieViewModel =
////                        ViewModelProvider(this).get(RecipieViewModel::class.java)
//                    binding.ervRecipie.withModels/*(recipieViewModel) { state ->*/ {
//
//                        // Header
//                        recipieHeader {
//                            onClickDeleteButton { _ ->
//                                recipieViewModel.deleteRecipie(tmp_rec, inflater)
//                                findNavController().navigate(R.id.action_nav_recipie_to_nav_recipies)
//                            }
//                            onClickEditButton { _ ->
//                                findNavController()
//                                    .navigate(R.id.action_nav_recipie_to_nav_change_recipie,
//                                        recipieViewModel.getBundle(tmp_rec))
//                            }
//                        }
//
//                        // Informations part 1
//                        recipieInformations {
//                            recipiePhoto(recipieViewModel.getPhoto(tmp_rec, inflater))
//                        }
//
//                        // Chipgroup
//                        group {
//                            id("chipgroup")
//                            layout(R.layout.epoxy_recipie_chigroup)
//                            recipieChipitem {
//
//                            }
//                        }
//
//                        // Chipgroup
////                        recipieChipgroup {
////
////                        }
//
//                        // Informations part 2
////                        recipieCookingInfo {
////                            tvRecipieTime.text =
////                        }
//
//                        // Ingredient items
//                        tmp_rec.ingredients?.forEach { ingr ->
//                            recipeIngredientItem {
//                                ingredientTextView(ingr.key)
//                                quantityTextView(ingr.value)
//                            }
//                        }
//
//                        // Preparation items
//                        tmp_rec.steps?.forEach{ step ->
//                            item {
//                                itemTextView(step)
//                            }
//                        }
//
//                        // Share & Add Cart Buttons
//
//                        addShopListFltBtn {
//                            onClickShare{ _ ->
//                                recipieViewModel.shareDialog(tmp_rec, inflater)
//                            }
//
//                            onClickAddCart { _ ->
//                                recipieViewModel.saveCart(tmp_rec, inflater)
//                            }
//                        }
//
//
//                    }
//
////                    recipieController = RecipieController(this, tmp_rec, prep_title, other_users, inflater)
////                    binding.ervRecipie.setController(recipieController)
////
////                    recipieController.requestModelBuild()
////
////                    binding.ervRecipie.addItemDecoration(DividerItemDecoration(requireActivity(),
////                        RecyclerView.VERTICAL))
//                }
//            }
        }
        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun invalidate() = withState(recipieViewModel){ state ->
//        tvRecipieName.text = "${state.name}"

//        binding.ervRecipie.requestModelBuild()
    }
}