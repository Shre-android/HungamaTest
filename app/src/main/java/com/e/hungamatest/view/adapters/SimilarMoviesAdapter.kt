package com.e.hungamatest.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.e.hungamatest.databinding.SimilarMoviesItemBinding
import com.e.hungamatest.model.db.pojo.SimilarMovieDetails
import com.e.hungamatest.utility.buildImageURL



class SimilarMoviesAdapter (private val similar: List<SimilarMovieDetails>?) : RecyclerView.Adapter<SimilarMoviesAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SimilarMoviesItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        similar.let {
            holder.binder.similarMovies.setImageURI(similar?.get(position)?.posterPath?.let { it1 ->
                getImagePath(
                    it1
                )
            })

            holder.binder.simMovieName.setText(similar?.get(position)?.title)
            holder.binder.simMovrating.setText(similar?.get(position)?.voteAverage.toString())
            holder.binder.simMovlaunchYear.setText(similar?.get(position)?.releaseDate)
        }


    }

    inner class ViewHolder(val binder: SimilarMoviesItemBinding) : RecyclerView.ViewHolder(binder.root)

    override fun getItemCount(): Int {
        return similar?.size!!
    }
    fun getImagePath(posterPath: String): String {
        return buildImageURL(posterPath)
    }



}