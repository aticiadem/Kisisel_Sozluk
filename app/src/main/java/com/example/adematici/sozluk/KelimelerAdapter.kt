package com.example.adematici.sozluk

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class KelimelerAdapter(private val mContext: Context,
                       private var kelimelerListe: List<Kelimeler>,
                       private var vt: VeritabaniYardimcisi)
    : RecyclerView.Adapter<KelimelerAdapter.CardTasarimTutucu>() {

    inner class CardTasarimTutucu(tasarim: View) : RecyclerView.ViewHolder(tasarim) {
        var textViewIngilizce: TextView
        var textViewTurkce: TextView
        var imageViewNokta: ImageView

        init {
            textViewIngilizce = tasarim.findViewById(R.id.textViewIngilizce)
            textViewTurkce = tasarim.findViewById(R.id.textViewTurkce)
            imageViewNokta = tasarim.findViewById(R.id.imageViewNokta)
        }
    } // CardTasarimTutucu

    override fun getItemCount(): Int {
        return kelimelerListe.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardTasarimTutucu {
        val tasarim = LayoutInflater.from(mContext).inflate(R.layout.sozluk_card_tasarim,parent,false)
        return CardTasarimTutucu(tasarim)
    }

    override fun onBindViewHolder(holder: CardTasarimTutucu, position: Int) {
        val kelime = kelimelerListe.get(position)
        holder.textViewIngilizce.text = kelime.kelime_ingilizce
        holder.textViewTurkce.text = kelime.kelime_turkce
        holder.imageViewNokta.setOnClickListener {
            val popupMenu = PopupMenu(mContext,holder.imageViewNokta)
            popupMenu.menuInflater.inflate(R.menu.popup_menu,popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when(menuItem.itemId){
                    R.id.action_sil -> {
                        Snackbar.make(holder.imageViewNokta,"${kelime.kelime_ingilizce} Silinsin Mi?",
                            Snackbar.LENGTH_SHORT)
                            .setAction("EVET",){
                                Kelimelerdao().kelimeSil(vt,kelime.kelime_id)
                                kelimelerListe = Kelimelerdao().tumKelimeler(vt)
                                notifyDataSetChanged() // Arayüzü yeniler
                            }.show()
                        true
                    }
                    R.id.action_guncelle -> {
                        alertGoster(kelime)
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
            popupMenu.show()
        }
    }

    fun alertGoster(kelime: Kelimeler){
        val tasarim = LayoutInflater.from(mContext).inflate(R.layout.alert_tasarim,null)
        val editTextIngilizce = tasarim.findViewById(R.id.editTextIngilizce) as EditText
        val editTextTurkce = tasarim.findViewById(R.id.editTextTurkce) as EditText

        editTextIngilizce.setText(kelime.kelime_ingilizce)
        editTextTurkce.setText(kelime.kelime_turkce)

        val ad = AlertDialog.Builder(mContext)
        ad.setTitle("Kelime Güncelle")
        ad.setView(tasarim)
        ad.setPositiveButton("Güncelle"){ dialogInterface, i ->
            val kelime_ingilizce = editTextIngilizce.text.toString().trim()
            val kelime_turkce = editTextTurkce.text.toString().trim()

            Kelimelerdao().kelimeGuncelle(vt,kelime.kelime_id,kelime_ingilizce,kelime_turkce)
            kelimelerListe = Kelimelerdao().tumKelimeler(vt)
            notifyDataSetChanged()

            Toast.makeText(mContext,"${kelime_ingilizce} - ${kelime_turkce}", Toast.LENGTH_SHORT).show()
        } // setPositiveButton

        ad.setNegativeButton("İptal"){ dialogInterface, i ->
        } // setNegativeButton

        ad.create().show()
    } // alertGoster
}