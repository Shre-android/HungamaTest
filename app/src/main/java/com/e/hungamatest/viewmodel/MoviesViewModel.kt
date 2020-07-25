package com.e.hungamatest.viewmodel

import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.e.hungamatest.model.db.DBProvider
import com.e.hungamatest.model.db.pojo.Message
import com.e.hungamatest.model.db.pojo.Movie
import com.e.hungamatest.model.datasource.MovieDataSourceFactory
import com.e.hungamatest.model.datasource.pageSize

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.ParsePosition

class MoviesViewModel() : ViewModel() {

    var movies:LiveData<PagedList<Movie>>
    lateinit var dataSource:LiveData<PageKeyedDataSource<Int,Movie>>
    private val _notifyDataChanges = MutableLiveData<Int>()
    val notifyDataChanges:LiveData<Int>
        get() = _notifyDataChanges

    private val _message = MutableLiveData<Message>()
    val message:LiveData<Message>
        get() = _message

    private val _firstItemLoaded = MutableLiveData<Any>()
    val firstItemLoaded:LiveData<Any>
    get() = _firstItemLoaded

    private val _searchQuery = MutableLiveData<String>("")
    private val searchQuery:LiveData<String>
        get() = _searchQuery

    init {
        movies = Transformations.switchMap(searchQuery) {
            val dataSourceFactory = MovieDataSourceFactory(it)
            dataSource = dataSourceFactory.movieDataSource
            val config = PagedList.Config.Builder().setEnablePlaceholders(false).setPageSize(pageSize).build()
            return@switchMap LivePagedListBuilder(dataSourceFactory,config)
                .setBoundaryCallback(object: PagedList.BoundaryCallback<Movie>(){
                    override fun onZeroItemsLoaded() {
                        super.onZeroItemsLoaded()
                        if(!searchQuery.value.isNullOrBlank())
                            _message.postValue(Message("No Data Found","Try another word"))
                        else
                            _message.postValue(Message("Not Connected","Try again later"))
                    }
                    override fun onItemAtEndLoaded(itemAtEnd: Movie) {
                        super.onItemAtEndLoaded(itemAtEnd)
                    }
                    override fun onItemAtFrontLoaded(itemAtFront: Movie) {
                        super.onItemAtFrontLoaded(itemAtFront)
                        _firstItemLoaded.postValue(Any())
                    }
                }).build()
        }
    }


    fun setFavourite(movie: Movie, favourite: Boolean,position: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val status = if(favourite){
                    DBProvider.db.getMovieDao().deleteMovie(movie).toLong()
                }else{
                    DBProvider.db.getMovieDao().addMovie(movie)
                }
                if(status != 0L){
                    _notifyDataChanges.postValue(position)
                }
            }
        }

    }

    fun resetNotifyDataChanges() {
        _notifyDataChanges.value = -1
    }

    fun setSearch(query: String) {
        _searchQuery.value = query
    }

    fun retryDataCall(){
        dataSource.value?.invalidate()
    }

}
