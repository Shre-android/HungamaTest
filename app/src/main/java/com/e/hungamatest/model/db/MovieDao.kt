package hungamatest.model.db

import androidx.paging.DataSource
import androidx.room.*
import com.e.hungamatest.model.db.pojo.Movie


@Dao
interface MovieDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMovie(movie: Movie):Long

    @Delete
    fun deleteMovie(movie: Movie):Int

    @Query("SELECT * FROM movie WHERE id = (:movieId)")
    fun getMovie(movieId:Int):Movie?

    @Query("SELECT * FROM movie")
    fun getAll(): DataSource.Factory<Int?, Movie>

}