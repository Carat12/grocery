package com.example.week3day5.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.week3day5.R
import com.example.week3day5.activities.OrderHistoryDetailActivity
import com.example.week3day5.models.Order
import kotlinx.android.synthetic.main.item_order_history.view.*

class AdapterOrders(var mContext: Context): RecyclerView.Adapter<AdapterOrders.ViewHolder>() {

    private var orderList: ArrayList<Order> = ArrayList()
    lateinit var listener: OnAdapterListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_order_history, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(orderList[position], position)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    fun setData(data: ArrayList<Order>) {
        for(i in data.size-1 downTo 0)
            orderList.add(data[i])
        //orderList = data
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(order: Order, position: Int) {
            itemView.text_view_status.text = order.orderStatus
            itemView.text_view_date.text = order.date!!.substring(0, 10)
            itemView.text_view_total.text = "Total: \$${order.orderSummary.orderAmount.toString()}"

            val adapter = AdapterOrderItem(mContext)
            adapter.setData(order.products)
            itemView.recycler_view_order_items.adapter = adapter
            itemView.recycler_view_order_items.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)

            //detail button
            itemView.btn_order_history_detail.setOnClickListener {
                listener.onButtonClicked(it, position)
            }
        }
    }

    interface OnAdapterListener{
        fun onButtonClicked(view: View, position: Int)
    }

    fun setOnAdapterListener(adapterListener: OnAdapterListener){
        listener = adapterListener
    }

    fun getItemData(position: Int): Order{
        return orderList[position]
    }
}