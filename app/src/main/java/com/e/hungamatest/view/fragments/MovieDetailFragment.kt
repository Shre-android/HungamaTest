package com.e.hungamatest.view.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.e.hungamatest.AdapterClickHandler
import com.e.hungamatest.R
import com.e.hungamatest.databinding.MovieDetailFragmentBinding
import com.e.hungamatest.model.db.pojo.Movie

import com.e.hungamatest.view.adapters.CastAdapter
import com.e.hungamatest.view.adapters.CrewAdapter
import com.e.hungamatest.view.adapters.SimilarMoviesAdapter
import com.e.hungamatest.viewmodel.MovieDetailViewModel


class MovieDetailFragment : BaseFragment(), AdapterClickHandler {

    private lateinit var viewModel: MovieDetailViewModel
    private lateinit var view: MovieDetailFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view = MovieDetailFragmentBinding.inflate(LayoutInflater.from(requireContext()))
        return view.root
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val item = menu.findItem(R.id.search)
        if (item != null) item.isVisible = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        viewModel = ViewModelProvider(this).get(MovieDetailViewModel::class.java)
        arguments?.getInt("ID")?.let { viewModel.loadData(it) }
        arguments?.getInt("ID")?.let { viewModel.loadCastAndCrew(it) }
        arguments?.getInt("ID")?.let { viewModel.loadSimilarMovies(it) }
        renderUI()
    }

    override fun renderUI() {
        viewModel.movieDetails.observe(viewLifecycleOwner, Observer {
            view.banner.setImageURI(viewModel.getImagePath(it.posterPath))
            view.title.text = it.title
            view.yearLengthCertificate.text = viewModel.getYearCountryCrtString(it.releaseDate,it.adult,it.runtime)
            view.rating.text = getString(R.string.rating,it.voteAverage.toString())
            view.description.text = it.overview
            view.language.text = viewModel.getLanguageString(it.spokenLanguages)
            view.tagLine.text = it.tagline

            it.genres?.let {genres->
                for (genre in genres){
                    if(!genre.name.isNullOrBlank())
                        view.genre.addView(getFlowChild(genre.name))
                }
            }
            hideMessage()
            hideLoader()
        })

        viewModel.castAndCrew.observe(viewLifecycleOwner, Observer {

            view.recycleViewCast.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            val adapterCast = CastAdapter(it.cast)
            view.recycleViewCast.adapter = adapterCast

            view.recycleViewCrew.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            val adapterCrew = CrewAdapter(it.crew)
            view.recycleViewCrew.adapter = adapterCrew
        })

        viewModel.similarMovies.observe(viewLifecycleOwner, Observer {

            view.recycleViewSimilarMovies.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            val adapterSimilarMovies = SimilarMoviesAdapter(it.results)
            view.recycleViewSimilarMovies.adapter = adapterSimilarMovies
        })



        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            showMessage(it.message,it.subMessage)
        })

        view.messageView.messageView.setOnClickListener {
            hideMessage()
            showLoader()
            arguments?.getInt("ID")?.let { viewModel.loadData(it) }
        }

    }

    private fun hideMessage() {
        view.messageView.messageView.visibility = View.GONE
    }


    private fun showMessage(message: String, subMessage: String) {
        view.messageView.messageView.visibility = View.VISIBLE
        view.messageView.message.text = message
        view.messageView.subMessage.text = subMessage
    }

    private fun hideLoader() {
        view.shimmeringView.shimmering.stopShimmer()
        view.shimmeringView.shimmeringView.visibility = View.GONE
    }

    private fun showLoader(){
        view.shimmeringView.shimmering.startShimmer()
        view.shimmeringView.shimmeringView.visibility = View.VISIBLE
    }

    private fun getFlowChild(text:String?):View{
        val flowChild = LayoutInflater.from(requireContext()).inflate(R.layout.flow_item,null,false)
        flowChild.findViewById<TextView>(R.id.name).text = text
        return flowChild
    }

    override fun onItemClick(id: Int) {
        TODO("Not yet implemented")
    }

    override fun setFavourite(movie: Movie, favourite: Boolean, position: Int) {
        TODO("Not yet implemented")
    }

}
