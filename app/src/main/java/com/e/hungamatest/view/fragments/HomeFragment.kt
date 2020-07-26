package com.e.hungamatest.view.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.e.hungamatest.databinding.HomeFragmentBinding
import com.e.hungamatest.viewmodel.HomeFragmentViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayoutMediator.TabConfigurationStrategy


class HomeFragment : BaseFragment() {

    private lateinit var viewBinding: HomeFragmentBinding
    private val fragmentList = ArrayList<FragmentData>().also{
        it.add(FragmentData("Movies", MoviesFragment.newInstance()))
        it.add(FragmentData("Favourite", FavouriteMoviesFragment.newInstance()))
    }

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var fragmentViewModel: HomeFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = HomeFragmentBinding.inflate(inflater)
        return viewBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        fragmentViewModel = ViewModelProvider(this).get(HomeFragmentViewModel::class.java)
        renderUI()
    }

    override fun renderUI() {
        viewBinding.pager.adapter = PagerAdapter(this,fragmentList)
        TabLayoutMediator(viewBinding.tabLayout, viewBinding.pager,
            TabConfigurationStrategy { tab: TabLayout.Tab, position: Int ->
                tab.text = fragmentList[position].title
            }
        ).attach()
    }

    fun submitQuery(query: String) {
        (fragmentList[0].fragment as MoviesFragment).submitQuery(query)
        viewBinding.pager.setCurrentItem(0,true)
    }

}


class PagerAdapter(host:Fragment,private val fragmentList:ArrayList<FragmentData>) : FragmentStateAdapter(host){

    override fun getItemCount(): Int {
        return fragmentList.size
    }
    override fun createFragment(position: Int): Fragment {
        return fragmentList[position].fragment
    }
}

data class FragmentData(val title:String,val fragment:Fragment)
