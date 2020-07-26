package com.e.hungamatest.model.datasource
/*
Class for getting the movies page wise
 */
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.e.hungamatest.model.db.pojo.Movie
import com.e.hungamatest.model.db.pojo.MoviesResponse
import com.e.hungamatest.model.network.APIProvider

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val firstPage =  1
const val pageSize =  20

class MovieDataSource(private val query:String?): PageKeyedDataSource<Int, Movie>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Movie>
    ) {
        val request = if(query.isNullOrBlank()){
            APIProvider.api.getMovies(page = firstPage)
        }else{
            APIProvider.api.searchMovie(page = firstPage,query = query)
        }
        request.enqueue(object : Callback<MoviesResponse>{
            override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
                callback.onResult(ArrayList(),null,null)
            }
            override fun onResponse(
                call: Call<MoviesResponse>,
                response: Response<MoviesResponse>
            ) {
                response.body()?.results?.let {
                    callback.onResult(it,null, firstPage +1)
                }
            }
        })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {

        val request = if(query.isNullOrBlank()){
            APIProvider.api.getMovies(page = params.key)
        }else{
            APIProvider.api.searchMovie(page = params.key,query = query)
        }
        request.enqueue(object : Callback<MoviesResponse>{
            override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
                Log.i("API",t.message ?: "")
            }
            override fun onResponse(
                call: Call<MoviesResponse>,
                response: Response<MoviesResponse>
            ) {
                response.body()?.let {
                    val nextPage:Int? = if(params.key<it.totalPages) params.key + 1
                    else null
                    it.results?.let {movies->
                        callback.onResult(movies,nextPage)
                    }
                }
            }
        })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        val request = if(query.isNullOrBlank()){
            APIProvider.api.getMovies(page = params.key)
        }else{
            APIProvider.api.searchMovie(page = params.key,query = query)
        }
        request.enqueue(object : Callback<MoviesResponse>{
            override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
                Log.i("API",t.message ?: "")
            }
            override fun onResponse(
                call: Call<MoviesResponse>,
                response: Response<MoviesResponse>
            ) {
                response.body()?.results?.let {
                    val prePage:Int? = if(params.key>1 ) params.key - 1
                    else null
                    callback.onResult(it,prePage)
                }
            }
        })
    }
}

 class MovieDataSourceFactory(private val query:String) : DataSource.Factory<Int,Movie>(){
    private val _movieDataSource = MutableLiveData<PageKeyedDataSource<Int,Movie>>()
    val movieDataSource: LiveData<PageKeyedDataSource<Int, Movie>>
    get() = _movieDataSource

    override fun create(): DataSource<Int, Movie> {
        val dataSource = MovieDataSource(query)
        _movieDataSource.postValue(dataSource)
        return dataSource
    }
}