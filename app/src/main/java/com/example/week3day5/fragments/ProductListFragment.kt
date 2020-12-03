package com.example.week3day5.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.week3day5.R
import com.example.week3day5.activities.ProductDetailActivity
import com.example.week3day5.activities.SubCategoryActivity
import com.example.week3day5.adapters.AdapterProduct
import com.example.week3day5.app.Config
import com.example.week3day5.app.Divider
import com.example.week3day5.app.EndPoints
import com.example.week3day5.database.DBHelper
import com.example.week3day5.models.Product
import com.example.week3day5.models.ResponseProduct
import com.example.week3day5.models.SubCategory
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_product.view.*
import kotlinx.android.synthetic.main.layout_update_order_item_count.view.*
import org.json.JSONObject


class ProductListFragment : Fragment(), AdapterProduct.onAdapterListener {
    lateinit var subCat: SubCategory
    lateinit var adapter: AdapterProduct
    lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            subCat = it.getSerializable(SubCategory.KEY_VALUE) as SubCategory
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_product, container, false)

        init(view)

        return view
    }

    override fun onStart() {
        super.onStart()
        adapter.notifyDataSetChanged()
    }

    private fun init(view: View) {
        dbHelper = (activity as SubCategoryActivity).dbHelper//DBHelper(activity!!)
        adapter = AdapterProduct(activity!!, dbHelper)
        adapter.setOnAdapterListener(this)

        view.recycler_view_product.adapter = adapter
        view.recycler_view_product.layoutManager = LinearLayoutManager(activity)

        val divider = Divider.getDivider(
            activity!!,
            DividerItemDecoration.VERTICAL,
            R.drawable.divider_product
        )
        view.recycler_view_product.addItemDecoration(divider)

        getProduct(view)
    }

    private fun getProduct(view: View) {
        val request = StringRequest(Request.Method.GET, EndPoints.getProductUrl(subCat.subId),
            {
                view.progress_bar.visibility = View.GONE

                val responseProduct = Gson().fromJson(it, ResponseProduct::class.java)
                adapter.setData(responseProduct.data)
            },
            {
                val response = String(it.networkResponse.data)
                Toast.makeText(
                    activity,
                    JSONObject(response).getString(Config.KEY_ERROR_MESSAGE),
                    Toast.LENGTH_SHORT
                ).show()
            })
        Volley.newRequestQueue(activity).add(request)
    }

    companion object {
        @JvmStatic
        fun newInstance(param: SubCategory) =
            ProductListFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(SubCategory.KEY_VALUE, param)
                }
            }
    }

    override fun onItemClicked(itemView: View, position: Int) {
        val intent = Intent(activity, ProductDetailActivity::class.java)
        intent.putExtra(Product.KEY_VALUE, adapter.getItemData(position))
        activity!!.startActivity(intent)
    }

    override fun onAddButtonClicked(itemView: View, position: Int) {
        dbHelper.addOrderItem(adapter.getItemData(position), 1)
        adapter.notifyItemChanged(position)
        activity!!.invalidateOptionsMenu()
    }

    override fun onIncreaseButtonClicked(itemView: View, position: Int) {
        val quantityInCart = itemView.text_view_count.text.toString().toInt()
        dbHelper.updateOrderItemCount(adapter.getItemData(position)._id, quantityInCart + 1)
        adapter.notifyItemChanged(position)
    }

    override fun onDecreaseButtonClicked(itemView: View, position: Int) {
        val quantityInCart = itemView.text_view_count.text.toString().toInt()
        val product = adapter.getItemData(position)
        if (quantityInCart == 1) {
            dbHelper.deleteOrderItem(product._id)
            activity!!.invalidateOptionsMenu()
        } else
            dbHelper.updateOrderItemCount(product._id, quantityInCart - 1)
        adapter.notifyItemChanged(position)
    }
}