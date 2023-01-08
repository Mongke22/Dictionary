package com.example.yourdictionary.presentation

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.yourdictionary.R
import com.example.yourdictionary.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        setupListeners()
        viewModel.dataLoaded.observe(this){ wordWasFound ->
            if(wordWasFound == false){
                Toast.makeText(this, "Слово не было найдено", Toast.LENGTH_LONG).show()
            }
            else{
                Log.i("findword","found")
                val fragment =  WordDetailFragment.newInstance(viewModel, binding.etSearch.etYandex.text.toString())
                launchFragment(fragment)
            }
            binding.etSearch.etYandex.isEnabled = true
            WordsListFragment.hideProgressDialog()
        }
        val fragment =  WordsListFragment.newInstance(viewModel)
        launchFragment(fragment)
    }
    private fun setupListeners(){
        binding.etSearch.ivSearch.setOnClickListener{
            Toast.makeText(this,"clicked", Toast.LENGTH_LONG).show()
            viewModel.addWord("${viewModel.language.value}-ru", binding.etSearch.etYandex.text.toString())
            WordsListFragment.showCustomProgressDialog(this)
            hideKeyBoard()
            binding.etSearch.etYandex.isEnabled = false
        }
        //Function to search by enter key
        binding.etSearch.etYandex.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (event.action == KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    binding.etSearch.ivSearch.callOnClick()
                    binding.etSearch.etYandex.clearFocus()
                    binding.etSearch.etYandex.isCursorVisible = false

                    return true
                }
                return false
            }
        })
    }

    // Function to hide keyboard after searching
    private fun hideKeyBoard(){
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(
            binding.etSearch.etYandex.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu);
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
         when (item.itemId) {
            R.id.action_words -> {
                val fragment =  WordsListFragment.newInstance(viewModel)
                launchFragment(fragment)
            }
            R.id.action_settings -> {
               val fragment =  SettingsFragment.newInstance(this, viewModel)
                launchFragment(fragment)
            }
            else -> throw RuntimeException("Unknown menu item")
        }
        return super.onOptionsItemSelected(item)
    }

    private fun launchFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.event_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}