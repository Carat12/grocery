package com.example.week3day5.app

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration

class Divider{

    companion object{
        fun getDivider(context: Context, orientation: Int, drawable: Int): DividerItemDecoration{
            val divider = DividerItemDecoration(context,orientation)
            divider.setDrawable(ContextCompat.getDrawable(context, drawable)!!)
            return divider
        }
    }
}