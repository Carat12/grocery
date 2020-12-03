package com.example.week3day5.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.week3day5.R
import com.example.week3day5.activities.ProductDetailActivity
import com.example.week3day5.app.Config
import com.example.week3day5.database.DBHelper
import com.example.week3day5.models.Product
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_product.view.*
import kotlinx.android.synthetic.main.layout_product_information.view.*
import kotlinx.android.synthetic.main.layout_update_order_item_count.view.*

class AdapterProduct(var mContext: Context, var dbHelper: DBHelper) : RecyclerView.Adapter<AdapterProduct.ViewHolder>() {

    private var proList: ArrayList<Product> = ArrayList()
    lateinit var listener: onAdapterListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(proList[position], position)
    }

    override fun getItemCount(): Int {
        return proList.size
    }

    fun setData(data: ArrayList<Product>) {
        proList = data
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(product: Product, position: Int) {
            itemView.txt_view_product_name.text = product.productName
            itemView.txt_view_product_price.text = "\$${product.price}"
            itemView.txt_view_product_price_original.text = "\$${product.mrp}"
            itemView.txt_view_product_price_original.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            itemView.txt_view_product_price_save.text =
                "Save: \$${Product.roundPrice(product.mrp - product.price)}"
            Picasso
                .get()
                .load(Config.IMAGE_URL + product.image)
                .placeholder(R.drawable.ic_baseline_image_24)
                .error(R.drawable.ic_baseline_broken_image_24)
                .into(itemView.img_view_product)

            itemView.setOnClickListener {
                listener.onItemClicked(it, position)
            }

            val quantityInCart = dbHelper.getOrderItemQuantity(product)
            if (quantityInCart == 0) {
                itemView.layout_update_item_count.isVisible = false
                itemView.btn_add_product.isVisible = true
                itemView.btn_add_product.setOnClickListener {
                    listener.onAddButtonClicked(it, position)
                }
            } else {
                itemView.btn_add_product.isVisible = false
                itemView.layout_update_item_count.isVisible = true
                itemView.text_view_count.text = quantityInCart.toString()
                itemView.btn_decrement_count.setImageResource(
                    if (quantityInCart == 1)
                        R.drawable.ic_baseline_delete_outline_24
                    else
                        R.drawable.ic_baseline_horizontal_rule_24
                )
                itemView.btn_increment_count.setOnClickListener {
                    listener.onIncreaseButtonClicked(itemView, position)
                }
                itemView.btn_decrement_count.setOnClickListener {
                    listener.onDecreaseButtonClicked(itemView, position)
                }
            }
        }
    }

    interface onAdapterListener {
        fun onItemClicked(itemView: View, position: Int)
        fun onAddButtonClicked(itemView: View, position: Int)
        fun onIncreaseButtonClicked(itemView: View, position: Int)
        fun onDecreaseButtonClicked(itemView: View, position: Int)
    }

    fun setOnAdapterListener(adapterListener: onAdapterListener) {
        listener = adapterListener
    }

    fun getItemData(position: Int): Product {
        return proList[position]
    }
}