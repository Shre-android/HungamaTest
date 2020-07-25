package com.e.hungamatest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.e.hungamatest.model.datasource.pageSize
import com.e.hungamatest.model.db.DBProvider
import com.e.hungamatest.model.db.pojo.Message
import com.e.hungamatest.model.db.pojo.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavouriteMoviesViewModel : ViewModel() {

    private val _firstItemLoaded = MutableLiveData<Any>()
    val firstItemLoaded:LiveData<Any>
        get() = _firstItemLoaded

    private val _message = MutableLiveData<Message>()
    val message:LiveData<Message>
        get() = _message

    val movies: LiveData<PagedList<Movie>>
    init{
        val dataSourceFactory = DBProvider.db.getMovieDao().getAll()
        val config = PagedList.Config.Builder().setEnablePlaceholders(false).setPageSize(pageSize).build()
        movies = LivePagedListBuilder(dataSourceFactory,config).setBoundaryCallback(object: PagedList.BoundaryCallback<Movie>(){
            override fun onZeroItemsLoaded() {
                super.onZeroItemsLoaded()
                _message.postValue(Message("No Data to Show","Add Favourite by Clicking Heart"))
            }

            override fun onItemAtFrontLoaded(itemAtFront: Movie) {
                super.onItemAtFrontLoaded(itemAtFront)
                _firstItemLoaded.postValue(Any())
            }
        }).build()
    }

    fun deleteMovie(movie: Movie) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                DBProvider.db.getMovieDao().deleteMovie(movie)
            }
        }
    }

}
