package com.example.adematici.sozluk.adapter

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.adematici.sozluk.R
import com.example.adematici.sozluk.database.WordsDatabase
import com.example.adematici.sozluk.databinding.DictionaryCardLayoutBinding
import com.example.adematici.sozluk.databinding.WordUpdateDialogBinding
import com.example.adematici.sozluk.model.WordsModel
import com.google.android.material.snackbar.Snackbar

class WordsAdapter(private val mContext: Context): RecyclerView.Adapter<WordsAdapter.CardViewHolder>() {

    private var wordList = emptyList<WordsModel>()

    private val wordsDatabase = WordsDatabase.getDatabase(mContext)!!

    inner class CardViewHolder(val itemBinding: DictionaryCardLayoutBinding)
        : RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = DictionaryCardLayoutBinding.inflate(LayoutInflater.from(mContext),parent,false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val currentItem = wordList[position]
        holder.itemBinding.textViewEnglish.text = currentItem.wordEnglish
        holder.itemBinding.textViewTurkish.text = currentItem.wordTurkish
        holder.itemBinding.imageViewDots.setOnClickListener {
            val popupMenu = PopupMenu(mContext,holder.itemBinding.imageViewDots)
            popupMenu.menuInflater.inflate(R.menu.popup_menu,popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when(menuItem.itemId){
                    R.id.actionDelete -> {
                        Snackbar.make(
                            holder.itemBinding.imageViewDots,
                            "${currentItem.wordEnglish} Silinsin Mi?",
                            Snackbar.LENGTH_SHORT
                        ).setAction("EVET",){
                            wordsDatabase.wordsDao().deleteWord(currentItem)
                            setData(wordsDatabase.wordsDao().getAllWords())
                        }.show()
                        true
                    }
                    R.id.actionUpdate -> {
                        showDialog(currentItem)
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }
    }

    override fun getItemCount(): Int {
        return wordList.size
    }

    private fun showDialog(currentItem: WordsModel){
        val dialog = Dialog(mContext)
        val dialogBinding = WordUpdateDialogBinding.inflate(
            LayoutInflater.from(mContext)
        )
        dialog.setContentView(dialogBinding.root)
        dialogBinding.editTextEnglish.setText(currentItem.wordEnglish)
        dialogBinding.editTextTurkish.setText(currentItem.wordTurkish)

        dialogBinding.button.setText(R.string.update)

        dialogBinding.button.setOnClickListener {
            val english = dialogBinding.editTextEnglish.text.toString()
            val turkish = dialogBinding.editTextTurkish.text.toString()
            val word = WordsModel(currentItem.wordId,english,turkish)
            wordsDatabase.wordsDao().updateWord(word)
            setData(wordsDatabase.wordsDao().getAllWords())
            Toast.makeText(mContext,"Güncelleme Başarılı.",Toast.LENGTH_SHORT).show()
            dialog.cancel()
        }
        dialog.show()
    }

    fun setData(newWordList: List<WordsModel>){
        this.wordList = newWordList
        notifyDataSetChanged()
    }

}