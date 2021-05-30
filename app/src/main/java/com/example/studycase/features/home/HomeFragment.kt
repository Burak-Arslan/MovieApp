package com.example.studycase.features.home


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studycase.R
import com.example.studycase.data.model.PopularMovieResponse
import com.example.studycase.data.model.PopulerMovieResultItem
import com.example.studycase.databinding.FragmentHomeBinding
import com.example.testappp.data.state.UIState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment @Inject constructor() : Fragment(R.layout.fragment_home),
    HomeAdapter.OnItemClickListener {

    lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()
    var pastVisiblesItems: Int = 0
    var visibleItemCount: Int = 0
    var totalItemCount: Int = 0
    var paging: Int = 1
    var callSerivceControl: Boolean = true
    var homeAdapter: HomeAdapter? = null
    var movieList: List<PopulerMovieResultItem>? = mutableListOf()


    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        binding = FragmentHomeBinding.bind(view)
        setRecyclerviewAnim()
        callService()
        events()
    }

    private fun events() {
        binding.homeRecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    visibleItemCount = recyclerView.getChildCount()
                    totalItemCount = binding.homeRecyclerview.layoutManager?.getItemCount()!!
                    pastVisiblesItems =
                        (recyclerView.layoutManager as LinearLayoutManager?)!!.findFirstVisibleItemPosition()


                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        if (callSerivceControl) {
                            callSerivceControl = false
                            homeViewModel.language = "en-US"
                            homeViewModel.page = ++paging
                            if (homeViewModel.page < 6) {
                                if (isOnline(requireContext())) {
                                    homeViewModel.popularMovies.load()
                                }
                            }
                        }
                    }
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

    private fun setRecyclerviewAnim() {
        val animation = AnimationUtils.loadLayoutAnimation(
            requireContext(),
            R.anim.layout_animation_slide_right
        )
        val linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        linearLayoutManager.reverseLayout = true
        binding.homeRecyclerview.layoutManager = linearLayoutManager
        binding.homeRecyclerview.layoutAnimation = animation
        binding.homeRecyclerview.scheduleLayoutAnimation()
    }

    fun callService() {
        try {
            homeViewModel.language = "en-US"
            homeViewModel.page = 1

            homeViewModel.popularMovies.load()
            homeViewModel.popularMovies.state.observe(viewLifecycleOwner, popularMoviesObserver)

        } catch (ex: Exception) {
            Log.e("callService", ex.message.toString())
        }
    }

    private val popularMoviesObserver = Observer<UIState<PopularMovieResponse>> { state ->
        when (state) {
            is UIState.Loading -> {
                if (state.isLoading) {

                } else {

                }
            }
            is UIState.Failure -> {
                callSerivceControl = true
                val homeAdapter = HomeAdapter(
                    requireContext(),
                    this,
                    homeViewModel.getAllMovies()!!
                )
                binding.apply {
                    homeRecyclerview.apply {
                        adapter = homeAdapter
                        layoutManager = LinearLayoutManager(requireContext())
                    }
                }

            }
            is UIState.Success -> {
                try {
                    var movieListSuccess: List<PopulerMovieResultItem>? =
                        state.data.results as List<PopulerMovieResultItem>
                    callSerivceControl = true
                    movieList = movieList?.plus(movieListSuccess!!)
                    homeViewModel.deleteAllMovies()
                    homeViewModel.insertPopularMovie(movieList as List<PopulerMovieResultItem>)

                    if (homeAdapter == null) {
                        homeAdapter = HomeAdapter(requireContext(), this, movieList!!)
                        binding.homeRecyclerview.adapter = homeAdapter

                        binding.homeRecyclerview.layoutManager =
                            LinearLayoutManager(requireContext())
                    }
                    binding.homeRecyclerview.adapter?.notifyDataSetChanged()
                    (homeAdapter as HomeAdapter).movieList =
                        movieList as List<PopulerMovieResultItem>
                    homeAdapter!!.notifyDataSetChanged()
                } catch (e: Exception) {
                    Log.e("popularMoviesObserver", e.message.toString())
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        homeAdapter = null
    }

    override fun onItemClickListenerMovies(currentItem: PopulerMovieResultItem) {
        val bundle = bundleOf("title" to currentItem.title)
        findNavController().navigate(R.id.action_homeFragment_to_homeDetailFragment, bundle)
    }
}