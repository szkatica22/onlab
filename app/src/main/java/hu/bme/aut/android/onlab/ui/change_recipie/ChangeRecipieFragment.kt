package hu.bme.aut.android.onlab.ui.change_recipie

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import hu.bme.aut.android.onlab.databinding.FragmentChangeRecipieBinding

class ChangeRecipieFragment : Fragment(){
    private lateinit var changeRecipieViewModel: ChangeRecipieViewModel
    private var _binding: FragmentChangeRecipieBinding? = null

    private lateinit var changerecipieController: ChangeRecipieController

//    private lateinit var adds_btn: FloatingActionButton
//    private lateinit var rec_v: RecyclerView
    var ingredients_list = arrayListOf(ChangeItem("1 cup espresso"), ChangeItem("6 egg yolks"), ChangeItem("6 Tbsp rum"), ChangeItem("30 ladyfingers"))
    var preparation_list = arrayListOf(ChangeItem("step1"), ChangeItem("step2"), ChangeItem("step3"), ChangeItem("step4"))
    var recipie_name: String = "Change Test Recipie"
    var recipie_flags = arrayListOf<String>("Desserst", "Drinks", "Soups", "Main courses")
    var time: String = "30 minutes"
    var abundance: String = "4 servings"
    var btn_ingredient: String = "Add new ingredient"
    var btn_step: String = "Add new step"
    var btn_delete: String = "Delete recipie"

    private val binding get() = _binding!!

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        changeRecipieViewModel =
            ViewModelProvider(this).get(ChangeRecipieViewModel::class.java)

        _binding = FragmentChangeRecipieBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Teszt gombok az elozo fragmentre valtashoz
//        binding.imgBtnEdit.setOnClickListener{
//            findNavController().navigate(R.id.action_nav_change_recipie_to_nav_recipie)
//        }
//        binding.btnDeleteRecipie.setOnClickListener {
//            findNavController().navigate(R.id.action_nav_change_recipie_to_nav_recipie)
//        }

        //Teszt hozzavalokra

//        changeingredientController = ChangeIngredientController(ingr_list)

        changerecipieController = ChangeRecipieController(ingredients_list, preparation_list,
            recipie_name, recipie_flags, time, abundance, btn_ingredient, btn_step, btn_delete)
        binding.ervChangeRecipie.setController(changerecipieController)

        changerecipieController.requestModelBuild()
//        binding.ervChangeRecipie.adapter = changeingredientController.adapter
//        binding.ervChangeRecipie.layoutManager = LinearLayoutManager(this.context)
        binding.ervChangeRecipie.addItemDecoration(DividerItemDecoration(requireActivity(),RecyclerView.VERTICAL))

//        binding.addingBtnRecipieIngredients.setOnClickListener {
//            val infl = inflater.inflate(R.layout.change_recipie_item, null)
//            //binding.llRecipieIngredients.addView(infl, binding.llRecipieIngredients.childCount)
//
//            val v = inflater.inflate(R.layout.add_step, null)
//            val ingredient = v.findViewById<EditText>(R.id.et_new_recipie_ingredient)
//            val add_dialog = AlertDialog.Builder(this.context)
//            add_dialog.setView(v)
//
//            add_dialog.setPositiveButton("Ok"){
//                    dialog,_->
//            var tmp_item = ChangeItem(ingredient.text.toString())
////                TODO: ezt a tmp:item-t kene hozzaadnom az llRecipieIngredients-hez
////            binding.llRecipieIngredients.addView(infl, binding.llRecipieIngredients.childCount)
//            ingr_list.add(ChangeItem(ingredient.text.toString()))
//            binding.llRecipieIngredients.addView(infl, binding.llRecipieIngredients.childCount)
////            changeingredientController.notifyDataSetChanged()
//
//
//                Toast.makeText(this.context, "Adding Ingredient", Toast.LENGTH_SHORT).show()
//                dialog.dismiss()
//            }
//            add_dialog.setNegativeButton("Cancel"){
//                    dialog,_->
//                Toast.makeText(this.context, "Cancel", Toast.LENGTH_SHORT).show()
//            }
//            add_dialog.create()
//            add_dialog.show()
//        }


//        //Add Ingredient
//        rec_v = binding.rvRecipieIngredients
//        rec_v.layoutManager = LinearLayoutManager(this.context)
//        rec_v.adapter = changeingredientAdapter
//
//        binding.addingBtnRecipieIngredients.setOnClickListener {
//            val v = inflater.inflate(R.layout.add_ingredient, null)
//            val ingredient = v.findViewById<EditText>(R.id.et_new_recipie_ingredient)
//            val add_dialog = AlertDialog.Builder(this.context)
//            add_dialog.setView(v)
//            add_dialog.setPositiveButton("Ok"){
//                dialog,_->
//                ingredients_list.add(ChangeItem(ingredient.text.toString()))
//                changeingredientAdapter.notifyDataSetChanged()
//                Toast.makeText(this.context, "Adding Ingredient", Toast.LENGTH_SHORT).show()
//                dialog.dismiss()
//            }
//            add_dialog.setNegativeButton("Cancel"){
//                dialog,_->
//                Toast.makeText(this.context, "Cancel", Toast.LENGTH_SHORT).show()
//            }
//            add_dialog.create()
//            add_dialog.show()
//
//
//        }

//        //Teszt elkeszitesi lepesekre
//
//        changepreparationAdapter = ChangePreparationAdapter(preparation_list)
//        binding.rvRecipiePreparation.adapter = changepreparationAdapter
//        binding.rvRecipiePreparation.layoutManager = LinearLayoutManager(this.context)
//
//        //Add Step
//        rec_v = binding.rvRecipiePreparation
//        rec_v.layoutManager = LinearLayoutManager(this.context)
//        rec_v.adapter = changepreparationAdapter
//
//        binding.addingBtnRecipiePreparations.setOnClickListener {
//            val v = inflater.inflate(R.layout.add_step, null)
//            val step = v.findViewById<EditText>(R.id.et_new_recipie_step)
//            val add_dialog = AlertDialog.Builder(this.context)
//            add_dialog.setView(v)
//            add_dialog.setPositiveButton("Ok"){
//                    dialog,_->
//                preparation_list.add(ChangeItem(step.text.toString()))
//                changepreparationAdapter.notifyDataSetChanged()
//                Toast.makeText(this.context, "Adding Step", Toast.LENGTH_SHORT).show()
//                dialog.dismiss()
//            }
//            add_dialog.setNegativeButton("Cancel"){
//                    dialog,_->
//                Toast.makeText(this.context, "Cancel", Toast.LENGTH_SHORT).show()
//            }
//            add_dialog.create()
//            add_dialog.show()
//
//
//        }


        //recyclerview id: rv_recipie_ingredients

//        binding.addingBtnRecipieIngredients.setOnClickListener {
//
//            val add_dialog = AlertDialog.Builder(this.context)
//
////            add_dialog.setView()
//
//            add_dialog.setPositiveButton("Ok"){
//                dialog,_ ->
////            val title = binding.etNewRecipieIngredient.text.toString()
////                ingredients_list.add(Item("Teszt"))
//            }
//            add_dialog.setNegativeButton("Cancel"){
//                dialog,_ ->
//            }
//            add_dialog.create()
//            add_dialog.show()


//            val title = binding.etNewRecipieIngredient //.text.toString()
//            if(etNewRecipieIngredient.isNotEmpty()){
////                val list_item = Item(etNewRecipieIngredient)
//                ingredientItemAdapter.addItem(Item(etNewRecipieIngredient))
//            }
//        }




        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}