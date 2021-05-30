package com.example.studycase.features.home

import android.content.Context
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.studycase.R
import com.example.studycase.data.model.PopulerMovieResultItem
import com.example.studycase.databinding.RecyclerHomeItemBinding
import com.squareup.picasso.Picasso


class HomeAdapter(
    private val context: Context,
    private val listener: OnItemClickListener,
    public var movieList: List<PopulerMovieResultItem>
) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    var currentPosition = 0
    private var lastPosition = -1
    private var selectedPos = RecyclerView.NO_POSITION

    inner class HomeViewHolder(private val binding: RecyclerHomeItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {


        fun bind(popularMovies: PopulerMovieResultItem, context: Context) {
            try {
                val handler = Handler()
                handler.postDelayed(Runnable {
                    Picasso.get()
                        .load("https://image.tmdb.org/t/p/w500/" + popularMovies.poster_path)
                        .into(binding.imgNews)

                }, 100)

                binding.txtTitle.text = popularMovies.title
                if(popularMovies.release_date != null){
                    val date = popularMovies.release_date.split("-").toTypedArray()
                    binding.txtReleaseDate.text = date[0]
                }
                binding.txtVoteCount.text = popularMovies.vote_average.toString() + " / 10"
                if (popularMovies.vote_average!! > 9) {
                    binding.txtVoteCount.setTextColor(context.resources.getColor(R.color.green))
                    binding.imgStar.setBackgroundResource(R.drawable.green_star)
                } else if (popularMovies.vote_average > 7 && popularMovies.vote_average < 9) {
                    binding.txtVoteCount.setTextColor(context.resources.getColor(R.color.orange))
                    binding.imgStar.setBackgroundResource(R.drawable.orange_star)
                } else if (popularMovies.vote_average < 7) {
                    binding.txtVoteCount.setTextColor(context.resources.getColor(R.color.red))
                    binding.imgStar.setBackgroundResource(R.drawable.red_star)
                }
            } catch (ex: Exception) {
                Log.e("TAG", ex.message.toString())
            }
        }

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            selectedPos = layoutPosition
            listener.onItemClickListenerMovies(movieList.get(selectedPos))
        }
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val currentItem = movieList.get(position)
        if (currentItem != null) {
            holder.bind(currentItem, context)
            currentPosition = position
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding =
            RecyclerHomeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(binding)
    }


    interface OnItemClickListener {
        fun onItemClickListenerMovies(currentItem: PopulerMovieResultItem)
    }

    override fun getItemCount(): Int {
        return movieList.size
    }
}