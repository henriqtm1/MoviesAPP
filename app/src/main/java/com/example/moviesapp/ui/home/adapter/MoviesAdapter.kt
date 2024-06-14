import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.api.models.movies.Result
import com.example.moviesapp.databinding.ItemMovieBinding
import com.example.moviesapp.utils.DecimalFormatRating
import com.example.moviesapp.utils.GlideImage

class MoviesAdapter(
    private val mMovies: List<Result>,
    private val mItemClickListener: (Result) -> Unit
) : RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(aParent: ViewGroup, aViewType: Int): MovieViewHolder {
        val lBinding = ItemMovieBinding.inflate(LayoutInflater.from(aParent.context), aParent, false)
        return MovieViewHolder(lBinding)
    }

    override fun onBindViewHolder(aHolder: MovieViewHolder, aPosition: Int) {
        aHolder.bind(mMovies[aPosition], mItemClickListener)
    }

    override fun getItemCount(): Int = mMovies.size

    class MovieViewHolder(private val aBinding: ItemMovieBinding) : RecyclerView.ViewHolder(aBinding.root) {

        fun bind(aMovie: Result, clickListener: (Result) -> Unit) {
            aBinding.txtMovieTitle.text = aMovie.title
            aBinding.txtMovieRating.text = DecimalFormatRating.mDecimalFormat.format(aMovie.vote_average)
            GlideImage.GlideImageTransform(aBinding.imgMovie.context, aMovie.poster_path,aBinding.imgMovie )

            aBinding.root.setOnClickListener { clickListener(aMovie) }
        }
    }
}