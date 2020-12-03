package com.example.week3day5.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.week3day5.R
import com.example.week3day5.app.EndPoints
import com.example.week3day5.models.OrderItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_order_items.view.*

class AdapterOrderItem(var mContext: Context): RecyclerView.Adapter<AdapterOrderItem.ViewHolder>() {

    private var itemList: ArrayList<OrderItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_order_items, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun setData(orderItems: ArrayList<OrderItem>) {
        itemList = orderItems
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(orderItem: OrderItem) {
            Picasso
                .get()
                .load(EndPoints.getImageUrl(orderItem.image))
                .placeholder(R.drawable.ic_baseline_image_24)
                .error(R.drawable.ic_baseline_broken_image_24)
                .into(itemView.img_view_product)
            itemView.text_view_order_item_count.text = orderItem.quantity.toString()
            itemView.text_view_product_info.text = "${orderItem.productName}: \$${orderItem.price}"
        }

    }
}