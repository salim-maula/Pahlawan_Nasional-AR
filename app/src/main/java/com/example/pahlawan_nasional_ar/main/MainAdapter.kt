package com.example.pahlawan_nasional_ar.main

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.pahlawan_nasional_ar.R
import com.example.pahlawan_nasional_ar.detail.DetailActivity
import kotlinx.android.synthetic.main.list_nama_pahlawan.view.*
import java.util.ArrayList

class MainAdapter(var context: Context, var modelMainList: MutableList<ModelMain>) :
    RecyclerView.Adapter<MainAdapter.MainViewHolder>(), Filterable {

    var modelMainFilterList: List<ModelMain> = ArrayList(modelMainList)

    private val modelFilter: Filter = object : Filter() {
        override fun performFiltering(constrain: CharSequence): FilterResults {
            val filteredList: MutableList<ModelMain> = ArrayList()
            if (constrain == null || constrain.length == 0) {
                filteredList.addAll(modelMainFilterList)
            } else {
                val filterPattern = constrain.toString().lowercase()
                for (modelMainFilter in modelMainFilterList) {
                    if (modelMainFilter.nama.lowercase()
                            .contains(filterPattern)
                        || modelMainFilter.namaLengkap.lowercase().contains(filterPattern)
                    ) {
                        filteredList.add(modelMainFilter)
                    }
                }
            }
            val result = FilterResults()
            result.values = filteredList
            return result
        }

        override fun publishResults(constraint: CharSequence, result: FilterResults) {
            modelMainList.clear()
            modelMainList.addAll(result.values as List<ModelMain>)
            notifyDataSetChanged()
        }

    }

    class MainViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var cvListMain: CardView
        var tvNamaPahlawan: TextView
        var tvNamaLengkap: TextView
        var tvKategori: TextView
        var imagePahlawan: ImageView

        init {
            cvListMain = itemView.cvListMain
            tvNamaPahlawan = itemView.tvNamaPahlawan
            tvNamaLengkap = itemView.tvNamaLengkap
            tvKategori = itemView.tvKategori
            imagePahlawan = itemView.imagePahlawan
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_nama_pahlawan, parent, false)
        return MainViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val data = modelMainList[position]

        holder.tvNamaPahlawan.text = data.nama
        holder.tvNamaLengkap.text = data.namaLengkap
        holder.tvKategori.text = data.kategori

        Glide.with(context)
            .load(data.image)
            .transform(CenterCrop(), RoundedCorners(25))
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.imagePahlawan)

        //send data to detail activity
        holder.cvListMain.setOnClickListener { view: View? ->
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.DETAIL_PAHLAWAN, modelMainList[position])
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return modelMainList.size
    }

    override fun getFilter(): Filter {
        return modelFilter
    }
}