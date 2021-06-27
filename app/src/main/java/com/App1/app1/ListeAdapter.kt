package com.App1.app1

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.App1.app1.model.Liste

class ListeAdapter(val pseudo: String, val apiHandler: APIHandler, var listes: MutableList<Liste> = ArrayList()) : RecyclerView.Adapter<ListeAdapter.ListeViewHolder>() {

    inner class ListeViewHolder(view: View?): RecyclerView.ViewHolder(view!!) {
        private val titreListeView = view?.findViewById(R.id.liste_name) as? TextView
        private val deleteButtonView = view?.findViewById(R.id.liste_remove) as? ImageView
        private var listeId: Int = 0

        fun bindListe(liste: Liste) {
            titreListeView?.text = liste.titreListeToDo
            listeId = liste.id
        }

        init {
            titreListeView?.setOnClickListener {
                val listeIntent = Intent(view?.context, ShowListActivity::class.java)
                listeIntent.putExtra("listName", titreListeView.text.toString())
                listeIntent.putExtra("listeId", listeId)
                listeIntent.putExtra("pseudo", pseudo)
                listeIntent.putExtra("baseUrl", apiHandler.baseUrl)
                listeIntent.putExtra("hashToken", apiHandler.hashToken)
                view?.context?.startActivity(listeIntent)
            }
        }

        fun getDeleteButton(): ImageView? {
            return deleteButtonView
        }
    }

    override fun getItemCount(): Int {
        return listes.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListeViewHolder {
        val context = parent.context
        val view = LayoutInflater.from(context)?.inflate(R.layout.liste, parent, false)

        return ListeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListeViewHolder, position: Int) {
        holder.bindListe(listes[position])
        holder.getDeleteButton()?.setOnClickListener {
            if (apiHandler.deleteListe(listeId = listes[position].id)) {
                listes.removeAt(index = position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, listes.size)
            }
        }
    }

    fun addData(newListe: List<Liste>) {
        listes.addAll(newListe)
        notifyItemChanged(listes.size)
    }
}