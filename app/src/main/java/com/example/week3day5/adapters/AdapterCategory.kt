package com.example.week3day5.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.week3day5.R
import com.example.week3day5.models.Category
import kotlinx.android.synthetic.main.item_category.view.*
import kotlin.collections.ArrayList

class AdapterCategory(var mContext: Context) : RecyclerView.Adapter<AdapterCategory.ViewHolder>(){

    private var catList: ArrayList<Category> = ArrayList()
    private lateinit var listener: onAdapterListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(catList[position], position)
    }

    override fun getItemCount(): Int {
        return catList.size
    }

    fun setData(mList: ArrayList<Category>){
        catList = mList
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(cat: Category, position: Int){
            itemView.txt_view_category.text = cat.catName

            itemView.img_view_category_bg.setImageResource(
                when(cat.catName){
                    Category.CAT_CHICKEN -> R.drawable.chicken
                    Category.CAT_VEG -> R.drawable.veg
                    Category.CAT_FRUIT -> R.drawable.fruit
                    Category.CAT_BEEF -> R.drawable.steak
                    else -> R.drawable.sehri
                }
            )
            if(cat.catName == Category.CAT_BEEF)
                itemView.img_view_category_bg.rotation = 160.0F

            itemView.setOnClickListener {
                listener.onItemClicked(it, position)
            }
        }
    }

    interface onAdapterListener{
        fun onItemClicked(view: View, position: Int)
    }

    fun setOnAdapterListener(adapterListener: onAdapterListener){
        listener = adapterListener
    }

    fun getItemData(position: Int): Category{
        return catList[position]
    }
}