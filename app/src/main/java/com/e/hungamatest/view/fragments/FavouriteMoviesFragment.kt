package com.e.hungamatest.view.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.e.hungamatest.AdapterClickHandler
import com.e.hungamatest.R
import com.e.hungamatest.databinding.FavouriteMoviesFragmentBinding
import com.e.hungamatest.model.db.pojo.Movie
import com.e.hungamatest.view.adapters.MovieItemAdapter
import com.e.hungamatest.viewmodel.FavouriteMoviesViewModel


class FavouriteMoviesFragment : BaseFragment(), AdapterClickHandler {

    companion object {
        fun newInstance() =
            FavouriteMoviesFragment()
    }

    private lateinit var viewModel: FavouriteMoviesViewModel
    private lateinit var viewBinding: FavouriteMoviesFragmentBinding
    val isDeletedFavIcon :Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FavouriteMoviesFragmentBinding.inflate(LayoutInflater.from(requireContext()))
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(FavouriteMoviesViewModel::class.java)
        renderUI()
    }
/*
Method to load the UI elements
 */
    override fun renderUI() {
        viewBinding.list.layoutManager = GridLayoutManager(requireContext(),2)
        viewBinding.list.setHasFixedSize(true)
        val adapter = MovieItemAdapter(this)
        viewModel.movies.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
        viewBinding.list.adapter = adapter

        viewModel.firstItemLoaded.observe(viewLifecycleOwner, Observer {
            hideMessage()
        })

        viewModel.message.observe(viewLifecycleOwner, Observer {
            showMessage(it.message,it.subMessage)
        })

    }
/*
Clicking opetation on single item or recycle view
 */
    override fun onItemClick(id: Int) {
        val bundle = Bundle()
        bundle.putInt("ID",id)
        parentFragment?.findNavController()?.navigate(R.id.action_homeFragment_to_movieDetailFragment,bundle)
    }
/*
Method to set favorite
 */
    override fun setFavourite(movie: Movie, favourite: Boolean, position: Int) {
        viewModel.deleteMovie(movie)
    }
/*
Hide the message view
 */
    fun hideMessage(){
        viewBinding.messageView.messageView.visibility = View.GONE
    }
/*
Show the message view
 */
    fun showMessage(message:String,subMessage:String){
        viewBinding.messageView.message.text = message
        viewBinding.messageView.subMessage.text = subMessage
        viewBinding.messageView.messageView.visibility = View.VISIBLE
    }


}
