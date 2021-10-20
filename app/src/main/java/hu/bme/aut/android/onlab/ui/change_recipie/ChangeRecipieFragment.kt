package hu.bme.aut.android.onlab.ui.change_recipie

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import hu.bme.aut.android.onlab.R
import hu.bme.aut.android.onlab.databinding.FragmentChangeRecipieBinding

class ChangeRecipieFragment : Fragment(){
    private lateinit var changeRecipieViewModel: ChangeRecipieViewModel
    private var _binding: FragmentChangeRecipieBinding? = null

    private lateinit var changeingredientAdapter: ChangeIngredientAdapter
    private lateinit var changepreparationAdapter: ChangePreparationAdapter

    private lateinit var adds_btn: FloatingActionButton
    private lateinit var rec_v: RecyclerView
    private var ingr_list = ArrayList<ChangeItem>()
    var ingredients_list = mutableListOf(ChangeItem("1 cup espresso"), ChangeItem("6 egg yolks"), ChangeItem("6 Tbsp rum"), ChangeItem("30 ladyfingers"))
    var preparation_list = mutableListOf(ChangeItem("step1"), ChangeItem("step2"), ChangeItem("step3"), ChangeItem("step4"))

    // This property is only valid between onCreateView and
    // onDestroyView.
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
        binding.imgBtnEdit.setOnClickListener{
            findNavController().navigate(R.id.action_nav_change_recipie_to_nav_recipie)
        }
        binding.btnDeleteRecipie.setOnClickListener {
            findNavController().navigate(R.id.action_nav_change_recipie_to_nav_recipie)
        }

        //Teszt hozzavalokra

        changeingredientAdapter = ChangeIngredientAdapter(ingr_list)
        binding.addingBtnRecipieIngredients.setOnClickListener {
            val infl = inflater.inflate(R.layout.change_recipie_item, null)
            //binding.llRecipieIngredients.addView(infl, binding.llRecipieIngredients.childCount)

            val v = inflater.inflate(R.layout.add_step, null)
            val ingredient = v.findViewById<EditText>(R.id.et_new_recipie_ingredient)
            val add_dialog = AlertDialog.Builder(this.context)
            add_dialog.setView(v)

            add_dialog.setPositiveButton("Ok"){
                    dialog,_->
            var tmp_item = ChangeItem(ingredient.text.toString())
//                TODO: ezt a tmp:item-t kene hozzaadnom az llRecipieIngredients-hez
//            binding.llRecipieIngredients.addView(infl, binding.llRecipieIngredients.childCount)
            ingr_list.add(ChangeItem(ingredient.text.toString()))
            binding.llRecipieIngredients.addView(infl, binding.llRecipieIngredients.childCount)
            changeingredientAdapter.notifyDataSetChanged()


                Toast.makeText(this.context, "Adding Ingredient", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            add_dialog.setNegativeButton("Cancel"){
                    dialog,_->
                Toast.makeText(this.context, "Cancel", Toast.LENGTH_SHORT).show()
            }
            add_dialog.create()
            add_dialog.show()
        }

//        changeingredientAdapter = ChangeIngredientAdapter(ingredients_list)
//        binding.rvRecipieIngredients.adapter = changeingredientAdapter
//        binding.rvRecipieIngredients.layoutManager = LinearLayoutManager(this.context)

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