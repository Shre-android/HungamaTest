package com.e.hungamatest.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.e.hungamatest.databinding.CastItemBinding
import com.e.hungamatest.model.db.pojo.Cast
import com.e.hungamatest.utility.buildImageURL



class CastAdapter(private val cast: List<Cast>?) : RecyclerView.Adapter<CastAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CastItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        cast.let {

            holder.binder.castImage.setImageURI(cast?.get(position)?.profilePath?.let { it1 ->
                getImagePath(
                    it1
                )
            })
            holder.binder.castName.setText(cast?.get(position)?.name)
        }


    }

    inner class ViewHolder(val binder: CastItemBinding) : RecyclerView.ViewHolder(binder.root)

    override fun getItemCount(): Int {
        return cast?.size!!
    }
    fun getImagePath(posterPath: String): String {
        return buildImageURL(posterPath)
    }



}




