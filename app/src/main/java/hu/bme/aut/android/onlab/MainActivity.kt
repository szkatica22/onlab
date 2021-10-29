package hu.bme.aut.android.onlab

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import hu.bme.aut.android.onlab.databinding.ActivityMainBinding
import hu.bme.aut.android.onlab.databinding.RegistrationBinding
import hu.bme.aut.android.onlab.extensions.validateNonEmpty
import androidx.navigation.fragment.findNavController
import hu.bme.aut.android.onlab.ui.recipies.RecipiesFragment

class MainActivity : BaseActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnRegister.setOnClickListener { registerClick() }
        binding.btnLogin.setOnClickListener { loginClick() }

    }

    // Ha regisztralni akarunk ez hivodik meg
    private fun registerClick() {
//        if (!validateForm()) {
//            return
//        }

//        showProgressDialog()

        //val infl = LayoutInflater.from(this).inflate(R.layout.change_recipie_item, null)
        //binding.llRecipieIngredients.addView(infl, binding.llRecipieIngredients.childCount)

        val v = LayoutInflater.from(this).inflate(R.layout.registration, null)

        val email = v.findViewById<EditText>(R.id.etregEmail)
        val pass1 = v.findViewById<EditText>(R.id.etregPassword)
        val pass2 = v.findViewById<EditText>(R.id.etregPassword2)
        val add_dialog = AlertDialog.Builder(this)
        add_dialog.setView(v)

        add_dialog.setPositiveButton("Ok") { dialog, _ ->
//            Log.d("REG:", "Email: ${email.validateNonEmpty()} , ${email.text.toString()}")
//            Log.d("REG:", "Pass1: ${pass1.validateNonEmpty()} , ${pass1.text.toString()}")
//            Log.d("REG:", "Pass2: ${pass2.validateNonEmpty()} , ${pass2.text.toString()}")
            if (email.validateNonEmpty() && pass1.validateNonEmpty() && pass2.validateNonEmpty() && pass1.text.toString() == pass2.text.toString()) {
                firebaseAuth
                    .createUserWithEmailAndPassword(email.text.toString(), pass1.text.toString())
                    .addOnSuccessListener { result ->
                        hideProgressDialog()

                        val firebaseUser = result.user
                        val profileChangeRequest = UserProfileChangeRequest.Builder()
                            .setDisplayName(firebaseUser?.email?.substringBefore('@'))
                            .build()
                        firebaseUser?.updateProfile(profileChangeRequest)

                        toast("Registration successful")
                    }
                    .addOnFailureListener { exception ->
                        hideProgressDialog()

                        toast(exception.message)
                    }
//                var tmp_item = ChangeItem(ingredient.text.toString())
//                //                TODO: ezt a tmp:item-t kene hozzaadnom az llRecipieIngredients-hez
//                //            binding.llRecipieIngredients.addView(infl, binding.llRecipieIngredients.childCount)
//                ingr_list.add(ChangeItem(ingredient.text.toString()))
//                binding.llRecipieIngredients.addView(infl, binding.llRecipieIngredients.childCount)
//                //            changeingredientController.notifyDataSetChanged()


                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }else{
                Toast.makeText(this, "Incorrect registration", Toast.LENGTH_SHORT).show()
            }

        }
        add_dialog.setNegativeButton("Cancel") { dialog, _ ->
            Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show()
        }
        add_dialog.create()
        add_dialog.show()

//        showProgressDialog()

//        firebaseAuth
//            .createUserWithEmailAndPassword(binding.etEmail.text.toString(), binding.etPassword.text.toString())
//            .addOnSuccessListener { result ->
//                hideProgressDialog()
//
//                val firebaseUser = result.user
//                val profileChangeRequest = UserProfileChangeRequest.Builder()
//                    .setDisplayName(firebaseUser?.email?.substringBefore('@'))
//                    .build()
//                firebaseUser?.updateProfile(profileChangeRequest)
//
//                toast("Registration successful")
//            }
//            .addOnFailureListener { exception ->
//                hideProgressDialog()
//
//                toast(exception.message)
//            }
    }

    private fun loginClick() {
        if (!validateForm()) {
            return
        }

        showProgressDialog()

        firebaseAuth
            .signInWithEmailAndPassword(binding.etEmail.text.toString(), binding.etPassword.text.toString())
            .addOnSuccessListener {
                hideProgressDialog()
                binding.btnLogin.setOnClickListener {
                    findNavController().navigate(R.id.nav_host_fragment_content_main)
                }

//                supportFragmentManager.findFragmentByTag(RecipiesFragment::class.java.simpleName)
//                startActivity(Intent(this@MainActivity, RecipiesFragment::class.java))
                finish()
            }
            .addOnFailureListener { exception ->
                hideProgressDialog()

                toast(exception.localizedMessage)
            }
    }


    private fun validateForm(): Boolean = binding.etEmail.validateNonEmpty()
            && binding.etPassword.validateNonEmpty()

}

//class MainActivity : AppCompatActivity() {
//
//    private lateinit var appBarConfiguration: AppBarConfiguration
//    private lateinit var binding: ActivityMainBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        setSupportActionBar(binding.appBarMain.toolbar)
//
//        val drawerLayout: DrawerLayout = binding.drawerLayout
//        val navView: NavigationView = binding.navView
//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.nav_recipies, R.id.nav_favourites, R.id.nav_shopping_list
//            ), drawerLayout
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.main, menu)
//        return true
//    }
//
//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
//    }
//}