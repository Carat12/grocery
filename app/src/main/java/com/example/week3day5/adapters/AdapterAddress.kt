package com.example.week3day5.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.week3day5.R
import com.example.week3day5.helpers.SessionManager
import com.example.week3day5.models.Address
import kotlinx.android.synthetic.main.item_address.view.*
import kotlinx.android.synthetic.main.layout_item_address.view.*

class AdapterAddress(var mContext: Context): RecyclerView.Adapter<AdapterAddress.ViewHolder>(){

    private var addressList: ArrayList<Address> = ArrayList()
    lateinit var listener: onAdapterListener
    var lastSelectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_address, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(addressList[position], position)
    }

    override fun getItemCount(): Int {
        return addressList.size
    }

    fun setData(data: ArrayList<Address>) {
        addressList = data
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(address: Address, position: Int) {
            itemView.text_view_name.text = SessionManager.userLoggedIn.firstName
            itemView.text_view_street.text = address.getAddressPartOne()
            itemView.text_view_city_state_zipcode.text = address.getAddressPartTwo()

            if(lastSelectedPosition == position){
                listener.onItemSelected(itemView, position)
            }
            else
                itemView.material_card_address.strokeColor = 0
            itemView.setOnClickListener {
                listener.onItemSelected(it, position)
            }
        }
    }

    interface onAdapterListener{
        fun onItemSelected(view: View, position: Int)
    }

    fun setOnAdapterListener(onAdapterListener: onAdapterListener){
        listener = onAdapterListener
    }

    fun getItemData(position: Int): Address{
        return addressList[position]
    }
}