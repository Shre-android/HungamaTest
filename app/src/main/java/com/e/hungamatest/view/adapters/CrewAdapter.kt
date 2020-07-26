package com.e.hungamatest.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.e.hungamatest.databinding.CrewItemBinding
import com.e.hungamatest.model.db.pojo.Crew
import com.e.hungamatest.utility.buildImageURL



class CrewAdapter (private val crew: List<Crew>?) : RecyclerView.Adapter<CrewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CrewItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        crew.let {

            holder.binder.crewImage.setImageURI(crew?.get(position)?.profilePath?.let { it1 ->
                getImagePath(
                    it1
                )
            })
            holder.binder.crewName.setText(crew?.get(position)?.name)
        }


    }

    inner class ViewHolder(val binder: CrewItemBinding) : RecyclerView.ViewHolder(binder.root)

    override fun getItemCount(): Int {
        return crew?.size!!
    }
    //Getting image path and building the URL to load image
    fun getImagePath(posterPath: String): String {
        return buildImageURL(posterPath)
    }



}
