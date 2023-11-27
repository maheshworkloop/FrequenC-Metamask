package com.dev.frequenc.ui_codes.connect.Profile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.dev.frequenc.R

class ViewPager2ProfileAdapter (val mcontext : Context, val mList : List<Int>)
    : RecyclerView.Adapter<ViewPager2ProfileAdapter.viewPager2ViewHolder>()
{

    inner class viewPager2ViewHolder(itemView: View):  RecyclerView.ViewHolder(itemView)
  {
      var ivImage : ImageView

      init {
          ivImage = itemView.findViewById(R.id.ivImage)

      }

  }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewPager2ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_viewpager_profile,parent,false)
        return viewPager2ViewHolder(view)
    }


    override fun onBindViewHolder(holder: viewPager2ViewHolder, position: Int) {

        var item = mList[position]


        holder.ivImage.setBackgroundResource(item)



    }

    override fun getItemCount(): Int {
        return mList.size
    }
}