package com.example.week3day5.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.example.week3day5.R
import com.example.week3day5.app.Config
import com.example.week3day5.models.Category
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_category_img.view.*

class AdapterCatImg(var mContext: Context): PagerAdapter(){

    private var catList: ArrayList<Category> = ArrayList()

    override fun getCount(): Int {
        return catList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_category_img, container,false)
        val cat = catList[position]

        Picasso
            .get()
            .load(Config.IMAGE_URL + cat.catImage)
            .placeholder(R.drawable.ic_baseline_image_24)
            .error(R.drawable.ic_baseline_broken_image_24)
            .into(view.img_view_category_slider)

        container.addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

    fun setData(data: ArrayList<Category>) {
        catList = data
        notifyDataSetChanged()
    }


}