package com.e.hungamatest.view.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.e.hungamatest.AdapterClickHandler
import com.e.hungamatest.R
import com.e.hungamatest.databinding.MoviesFragmentBinding
import com.e.hungamatest.model.db.pojo.Movie
import com.e.hungamatest.view.adapters.MovieItemAdapter
import com.e.hungamatest.viewmodel.MoviesViewModel

class MoviesFragment : BaseFragment(), AdapterClickHandler {

    companion object {
        fun newInstance() = MoviesFragment()
    }

    private lateinit var viewModel: MoviesViewModel
    private lateinit var viewBinder: MoviesFragmentBinding




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinder = MoviesFragmentBinding.inflate(inflater)
        return viewBinder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MoviesViewModel::class.java)

        renderUI()
    }

    fun showLoader(){
        viewBinder.shimmering.shimmering.startShimmer()
        viewBinder.shimmering.shimmering.visibility = View.VISIBLE
    }
    private fun hideLoader(){
        viewBinder.shimmering.shimmering.stopShimmer()
        viewBinder.shimmering.shimmering.visibility = View.GONE
    }

    override fun renderUI() {
        viewBinder.list.layoutManager = GridLayoutManager(requireContext(),2)
        viewBinder.list.setHasFixedSize(true)
        val adapter = MovieItemAdapter(this)
        viewModel.movies.observe(viewLifecycleOwner,Observer {
            adapter.submitList(it)
            if(it.size>20){
                hideLoader()
            }
        })
        viewModel.notifyDataChanges.observe(viewLifecycleOwner, Observer {
            if(it != -1) {
                viewBinder.list.adapter?.notifyDataSetChanged()
                viewModel.resetNotifyDataChanges()
            }
        })
        viewBinder.list.adapter = adapter

        viewBinder.messageView.messageView.setOnClickListener {
            hideMessage()
            showLoader()
            viewModel.retryDataCall()
        }
        viewModel.message.observe(viewLifecycleOwner, Observer {
            showMessage(it.message,it.subMessage)
        })

        viewModel.firstItemLoaded.observe(viewLifecycleOwner, Observer {
            hideMessage()
            hideLoader()
        })

    }

    private fun showMessage(message: String, subMessage: String) {
        viewBinder.messageView.message.text = message
        viewBinder.messageView.subMessage.text = subMessage
        viewBinder.messageView.messageView.visibility = View.VISIBLE
    }

    private fun hideMessage() {
        viewBinder.messageView.messageView.visibility = View.GONE
    }

    override fun onItemClick(id: Int) {
        val bundle = Bundle()
        bundle.putInt("ID",id)
        parentFragment?.findNavController()?.navigate(R.id.action_homeFragment_to_movieDetailFragment,bundle)
    }

    override fun setFavourite(movie: Movie, favourite: Boolean, position:Int) {
        movie.isFavourite = !favourite
        viewModel.setFavourite(movie,favourite,position)
    }

    fun submitQuery(query: String) {
        viewModel.setSearch(query)
    }


}
