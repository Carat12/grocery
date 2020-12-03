package com.example.week3day5.fragments

import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.week3day5.R
import com.example.week3day5.adapters.AdapterOrderItem
import com.example.week3day5.models.Order
import com.example.week3day5.models.OrderSummary
import kotlinx.android.synthetic.main.fragment_order_history_detail.view.*
import kotlinx.android.synthetic.main.layout_item_address.view.*
import kotlinx.android.synthetic.main.layout_item_summary.*
import kotlinx.android.synthetic.main.layout_item_summary.view.*

class OrderHistoryDetailFragment : Fragment() {
    private lateinit var order: Order
    lateinit var adapter: AdapterOrderItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            order = it.getSerializable(Order.KEY_ORDER) as Order
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_order_history_detail, container, false)

        init(view)

        return view
    }

    private fun init(view: View) {
        val orderSummary = order.orderSummary
        val total = orderSummary.orderAmount
        view.text_view_details_date.text = order.date!!.substring(0, 10)
        view.text_view_details_total.text = "\$$total"
        //address
        val address = order.shippingAddress
        view.text_view_name.text = order.user.name
        view.text_view_street.text = address.getAddressPartOne()
        view.text_view_city_state_zipcode.text = address.getAddressPartTwo()
        //items
        val quantity = order.products.size
        view.text_view_item_number.text = "$quantity Items"
        adapter = AdapterOrderItem(activity!!)
        adapter.setData(order.products)
        view.recycler_view_order_items.adapter = adapter
        view.recycler_view_order_items.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        //summary
        view.text_view_subtotal.text = "\$${orderSummary.ourPrice}"
        view.text_view_discount.text = "-\$${orderSummary.discount}"
        view.text_view_delivery.text = "\$${OrderSummary.DEFAULT_DELIVERY_FEE}"
        view.text_view_delivery.paintFlags = if (orderSummary.deliveryCharges == 0.0) Paint.STRIKE_THRU_TEXT_FLAG else Paint.ANTI_ALIAS_FLAG
        view.text_view_total.text = "\$$total"
    }

    companion object {
        @JvmStatic
        fun newInstance(order: Order) =
            OrderHistoryDetailFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(Order.KEY_ORDER, order)
                }
            }
    }
}