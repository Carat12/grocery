package com.example.week3day5.activities

import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.week3day5.R
import com.example.week3day5.app.Config
import com.example.week3day5.app.EndPoints
import com.example.week3day5.database.DBHelper
import com.example.week3day5.helpers.MenuManager
import com.example.week3day5.models.OrderItem
import com.example.week3day5.models.Product
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.android.synthetic.main.layout_product_information.*
import kotlinx.android.synthetic.main.layout_update_order_item_count.*
import kotlinx.android.synthetic.main.tool_bar.*

class ProductDetailActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var dbHelper: DBHelper
    lateinit var product: Product
    private var quantityInCart: Int = 0
    private val BUTTON_ALPHA = 0.6F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        init()
    }

    override fun onStart() {
        super.onStart()
        invalidateOptionsMenu()
        updateQuantityInCart()
    }

    private fun init() {
        //tool bar
        tool_bar.title = Config.TITLE_PRODUCT
        setSupportActionBar(tool_bar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        //product information
        product = intent.getSerializableExtra(Product.KEY_VALUE) as Product
        Picasso
            .get()
            .load(EndPoints.getImageUrl(product.image))
            .placeholder(R.drawable.ic_baseline_image_24)
            .error(R.drawable.ic_baseline_broken_image_24)
            .into(img_view_product_detail)
        txt_view_product_name.text = product.productName
        txt_view_product_price.text = "\$${product.price}"
        txt_view_product_name.textSize = 28F
        txt_view_product_price_original.text = "\$${product.mrp}"
        txt_view_product_price_original.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        txt_view_product_price_save.text =
            "Save: \$${Product.roundPrice(product.mrp - product.price)}"
        txt_view_product_desc.text = "Description: \n ${product.description}"

        //add to cart
        dbHelper = DBHelper(this)
        updateQuantityInCart()
        //update number
        btn_increment_count.setOnClickListener(this)
        btn_decrement_count.setOnClickListener(this)
        //add to cart
        btn_update_cart.setOnClickListener(this)
    }

    private fun updateQuantityInCart(){
        quantityInCart = dbHelper.getOrderItemQuantity(product)
        if (quantityInCart == 0) {
            text_view_count.text = "1"
            enableUpdateCartButton()
            btn_update_cart.text = OrderItem.ADD_TO_CART
        } else {
            text_view_count.text = quantityInCart.toString()
            disableUpdateCartButton()
            btn_update_cart.text = OrderItem.IN_CART
        }
        enableDecreaseQuantityButton()
    }

    private fun enableUpdateCartButton(){
        btn_update_cart.isEnabled = true
        btn_update_cart.alpha = 1F
    }

    private fun disableUpdateCartButton(){
        btn_update_cart.isEnabled = false
        btn_update_cart.alpha = BUTTON_ALPHA
    }

    private fun enableDecreaseQuantityButton(){
        btn_decrement_count.isEnabled = true
        btn_decrement_count.alpha = 1F
    }

    private fun disableDecreaseQuantityButton(){
        btn_decrement_count.isEnabled = false
        btn_decrement_count.alpha = BUTTON_ALPHA
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return MenuManager.menuInit(this, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        return MenuManager.updateCartItemQuantity(menu!!)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return MenuManager.menuOptionSelected(this, item)
    }

    override fun onClick(v: View?) {
        var number = text_view_count.text.toString().toInt()
        val status = btn_update_cart.text.toString()
        when (v) {
            btn_increment_count -> {
                number++
                text_view_count.text = number.toString()
                if (status == OrderItem.IN_CART) {
                    btn_update_cart.text = OrderItem.UPDATE_QUANTITY
                    enableUpdateCartButton()
                }
                if (number == 1) {
                    enableDecreaseQuantityButton()
                    if (status == OrderItem.REMOVE)
                        btn_update_cart.text = OrderItem.UPDATE_QUANTITY
                    else { //enable add to cart
                        enableUpdateCartButton()
                    }
                }
                if (number == quantityInCart) {
                    btn_update_cart.text = OrderItem.IN_CART
                    disableUpdateCartButton()
                }
            }
            btn_decrement_count -> {
                number--
                text_view_count.text = number.toString()
                if (status == OrderItem.IN_CART || status == OrderItem.UPDATE_QUANTITY) {
                    if (number == quantityInCart) {
                        btn_update_cart.text = OrderItem.IN_CART
                        disableUpdateCartButton()
                    } else {
                        enableUpdateCartButton()
                        if (number == 0) {
                            disableDecreaseQuantityButton()
                            btn_update_cart.text = OrderItem.REMOVE
                        } else
                            btn_update_cart.text = OrderItem.UPDATE_QUANTITY
                    }
                } else {
                    if (number == 0) {
                        disableDecreaseQuantityButton()
                        disableUpdateCartButton()
                    }
                }
            }
            btn_update_cart -> {
                when (status) {
                    OrderItem.ADD_TO_CART -> {
                        dbHelper.addOrderItem(product, number)
                        invalidateOptionsMenu()
                    }
                    OrderItem.REMOVE -> {
                        dbHelper.deleteOrderItem(product._id)
                        invalidateOptionsMenu()
                    }
                    OrderItem.UPDATE_QUANTITY -> {
                        dbHelper.updateOrderItemCount(product._id, number)
                    }
                }
                updateQuantityInCart()
            }
        }
    }
}