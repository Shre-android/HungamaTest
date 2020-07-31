package com.e.hungamatest.view.fragments


import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.e.hungamatest.AdapterClickHandler
import com.e.hungamatest.R
import com.e.hungamatest.databinding.MovieDetailFragmentBinding
import com.e.hungamatest.model.db.pojo.Cast
import com.e.hungamatest.model.db.pojo.Movie
import com.e.hungamatest.view.adapters.CastAdapter
import com.e.hungamatest.view.adapters.CrewAdapter
import com.e.hungamatest.view.adapters.SimilarMoviesAdapter
import com.e.hungamatest.viewmodel.MovieDetailViewModel
import kotlinx.android.synthetic.main.movie_detail_fragment.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class MovieDetailFragment : BaseFragment(), AdapterClickHandler {

    private lateinit var viewModel: MovieDetailViewModel
    public lateinit var view: MovieDetailFragmentBinding
    var list : ArrayList<Cast> = ArrayList()


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


        //call for Async Task
        val task = HTTPReqTask(requireContext(),list,view.recycleViewCast)
        task.execute(arguments?.getInt("ID").toString())
        renderUI()
    }

    override fun renderUI() {
        //Loading the movie details
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





        //loading the cast and crew
        viewModel.castAndCrew.observe(viewLifecycleOwner, Observer {

//            view.recycleViewCast.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
//            val adapterCast = CastAdapter(it.cast as ArrayList<Cast>?)
//            view.recycleViewCast.adapter = adapterCast

            view.recycleViewCrew.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            val adapterCrew = CrewAdapter(it.crew)
            view.recycleViewCrew.adapter = adapterCrew
        })

        //loading the similar movies
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

/*
Async task for HTTP url connection
 */
    private  class HTTPReqTask(
    context: Context,
    list: ArrayList<Cast>,
    recycleViewCast: RecyclerView

) :
        AsyncTask<String?, String?, String?>() {
        val progressDialog = ProgressDialog(context)
        val listCastDetails : ArrayList<Cast> = list
        val recVw : RecyclerView = recycleViewCast
    val cntxt : Context = context


        override fun onPreExecute() {
            super.onPreExecute()
            progressDialog.setTitle("Please wait....")
            progressDialog.setMessage("Loading...")
            progressDialog.show()

        }

        protected override fun doInBackground(vararg p0: String?): String? {

            var urlConnection: HttpURLConnection? = null
            val id = p0[0]
            val urlStr =
                "https://api.themoviedb.org/3/movie/$id/credits?api_key=1acb6020cf2e009f86eb99ecbe57fc9a"
            Log.i("urlString", urlStr)

            // GET
            try {
                val url = URL(urlStr)
                urlConnection = url.openConnection() as HttpURLConnection
                Log.i("urlStringFinal", url.toString())
                val code = urlConnection.responseCode
                if (code != 200) {
                    throw IOException("Invalid response from server: $code")
                }
                val rd = BufferedReader(
                    InputStreamReader(
                        urlConnection.inputStream
                    )
                )

                var line: String?
                while (rd.readLine().also { line = it } != null) {
                    Log.i("data", line)
                    val json_response:JSONObject = JSONObject(line)
                    val jsonarray_info: JSONArray = json_response.getJSONArray("cast")
                    for (i in 0.. jsonarray_info.length()-1) {
                        val json_objectdetail:JSONObject=jsonarray_info.getJSONObject(i)
                        val model:Cast= Cast()
                        model.castId=json_objectdetail.getInt("id")
                        model.name==json_objectdetail.getString("name")
                        model.profilePath=json_objectdetail.getString("profile_path")
                        listCastDetails.add(model)


                    }
                }


            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                urlConnection?.disconnect()
            }
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            progressDialog.dismiss()

            recVw.layoutManager = LinearLayoutManager(cntxt,LinearLayoutManager.HORIZONTAL,false)
            val adapterCast = CastAdapter(listCastDetails)
            recVw.adapter = adapterCast

        }
    }



    //Hide message method
    private fun hideMessage() {
        view.messageView.messageView.visibility = View.GONE
    }

    //show message method
    private fun showMessage(message: String, subMessage: String) {
        view.messageView.messageView.visibility = View.VISIBLE
        view.messageView.message.text = message
        view.messageView.subMessage.text = subMessage
    }

    //hide the shimmering effect method
     fun hideLoader() {
        view.shimmeringView.shimmering.stopShimmer()
        view.shimmeringView.shimmeringView.visibility = View.GONE
    }

    //show loader method
     fun showLoader(){
        view.shimmeringView.shimmering.startShimmer()
        view.shimmeringView.shimmeringView.visibility = View.VISIBLE
    }

    //get the flow item view
    private fun getFlowChild(text:String?):View{
        val flowChild = LayoutInflater.from(requireContext()).inflate(R.layout.flow_item,null,false)
        flowChild.findViewById<TextView>(R.id.name).text = text
        return flowChild
    }

    override fun onItemClick(id: Int) {
    }

    override fun setFavourite(movie: Movie, favourite: Boolean, position: Int) {
    }

}
