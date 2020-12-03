package com.example.week3day5.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.week3day5.fragments.ProductListFragment
import com.example.week3day5.models.SubCategory

class AdapterSubCategory(fm: FragmentManager) : FragmentPagerAdapter(fm){

    private var subCatList: ArrayList<SubCategory> = ArrayList()
    lateinit var fragmentList: ArrayList<Fragment>

    override fun getCount(): Int {
        return subCatList.size
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
        //return ProductListFragment.newInstance(subCatList[position])   //this create a new fragment every time swipe the view pager
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return subCatList[position].subName
    }

    fun setData(mList: ArrayList<SubCategory>){
        subCatList = mList
        fragmentList = ArrayList()
        for(subCat in subCatList)
            fragmentList.add(ProductListFragment.newInstance(subCat))
        notifyDataSetChanged()
    }
}