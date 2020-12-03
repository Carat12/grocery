package com.example.week3day5.adapters

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.week3day5.R
import com.example.week3day5.app.Config
import com.example.week3day5.models.OrderItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_cart.view.*
import kotlinx.android.synthetic.main.layout_update_order_item_count.view.*

class AdapterCart(var mContext: Context): RecyclerView.Adapter<AdapterCart.ViewHolder>(){

    lateinit var orderItemList: ArrayList<OrderItem>
    lateinit var listener: onAdapterListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view  = LayoutInflater.from(mContext).inflate(R.layout.item_cart, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(orderItemList[position], position)
    }

    override fun getItemCount(): Int {
        return orderItemList.size
    }

    fun setData(orderItems: ArrayList<OrderItem>) {
        orderItemList = orderItems
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(orderItem: OrderItem, position: Int) {
            itemView.txt_view_product_name.text = orderItem.productName
            itemView.txt_view_product_price.text = "\$${orderItem.price}"
            itemView.txt_view_product_price_original.text = "\$${orderItem.mrp}"
            itemView.txt_view_product_price_original.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            Picasso
                .get()
                .load(Config.IMAGE_URL + orderItem.image)
                .placeholder(R.drawable.ic_baseline_image_24)
                .error(R.drawable.ic_baseline_broken_image_24)
                .into(itemView.img_view_product)
            val count = orderItem.quantity
            itemView.text_view_count.text = count.toString()

            val btnDecreCount = itemView.btn_decrement_count
            if(count == 1){
                btnDecreCount.setImageResource(R.drawable.ic_baseline_delete_outline_24)
                itemView.btn_remove_order_item.visibility = View.INVISIBLE
            }else{
                btnDecreCount.setImageResource(R.drawable.ic_baseline_horizontal_rule_24)
                itemView.btn_remove_order_item.visibility = View.VISIBLE
            }
            //increment count
            itemView.btn_increment_count.setOnClickListener {
                listener.onButtonClicked(it, position)
            }
            //decrement count
            itemView.btn_decrement_count.setOnClickListener {
                listener.onButtonClicked(it, position)
            }
            //remove orderItem
            itemView.btn_remove_order_item.setOnClickListener {
                listener.onButtonClicked(it, position)
            }
        }

    }

    interface onAdapterListener{
        fun onButtonClicked(view: View, position: Int)
    }

    fun setOnAdapterListener(adapterListener: onAdapterListener){
        listener = adapterListener
    }

    fun getItemData(position: Int): OrderItem{
        return orderItemList[position]
    }
}