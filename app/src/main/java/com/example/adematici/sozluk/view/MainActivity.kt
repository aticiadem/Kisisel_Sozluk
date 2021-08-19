package com.example.adematici.sozluk.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adematici.sozluk.*
import com.example.adematici.sozluk.adapter.WordsAdapter
import com.example.adematici.sozluk.database.WordsDatabase
import com.example.adematici.sozluk.databinding.ActivityMainBinding
import com.example.adematici.sozluk.databinding.WordUpdateDialogBinding
import com.example.adematici.sozluk.model.WordsModel

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var wordList: List<WordsModel>
    private lateinit var adapter: WordsAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var wordsDatabase: WordsDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = "Kişisel Sözlük"
        setSupportActionBar(binding.toolbar)

        wordsDatabase = WordsDatabase.getDatabase(this)!!

        wordList = wordsDatabase.wordsDao().getAllWords()
        adapter = WordsAdapter(this)
        adapter.setData(wordList)

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.fab.setOnClickListener {
            showDialog()
        }

    }

    private fun showDialog(){
        val dialog = Dialog(this)
        val dialogBinding = WordUpdateDialogBinding.inflate(
            LayoutInflater.from(this)
        )
        dialog.setContentView(dialogBinding.root)

        dialogBinding.button.setText(R.string.add)

        dialogBinding.button.setOnClickListener {
            if (dialogBinding.editTextEnglish.text.isNotEmpty()
                && dialogBinding.editTextTurkish.text.isNotEmpty()){
                val english = dialogBinding.editTextEnglish.text.toString()
                val turkish = dialogBinding.editTextTurkish.text.toString()
                val word = WordsModel(0,wordEnglish = english, wordTurkish = turkish)
                wordsDatabase.wordsDao().addWord(word)
                adapter.setData(wordsDatabase.wordsDao().getAllWords())
                Toast.makeText(this,"Ekleme Başarılı.",Toast.LENGTH_SHORT).show()

                dialog.cancel()
            } else {
                Toast.makeText(this,"Boşlukları Doldurun!",Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)

        val item = menu?.findItem(R.id.actionSearch)
        val searchView = item?.actionView as SearchView
        searchView.setOnQueryTextListener(this)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        searchWord(query)
        return true
    }

    override fun onQueryTextChange(newText: String): Boolean {
        searchWord(newText)
        return true
    }

    private fun searchWord(word: String){
        adapter.setData(wordsDatabase.wordsDao().findWord(word))
    }

}