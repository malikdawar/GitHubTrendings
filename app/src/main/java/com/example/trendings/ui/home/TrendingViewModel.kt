package com.example.trendings.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trendings.data.remote.model.TrendingRepoCallBack
import com.example.trendings.data.repository.TrendingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TrendingViewModel(private val mTrendingRepo: TrendingRepository) : ViewModel() {
    private val trendingUIData = MutableLiveData<TrendingUIState>()

    fun getTrendingUIData(): LiveData<TrendingUIState> = trendingUIData

    fun getTrendingRepos(refresh: Boolean = false) {
        viewModelScope.launch {
            trendingUIData.postValue(TrendingUIState.Loading)
            when (val repos = mTrendingRepo.getTrendingRepos(refresh)) {
                is TrendingRepoCallBack.Repositories -> {
                    trendingUIData.postValue(TrendingUIState.Success(repos.trending))
                }
                is TrendingRepoCallBack.Error -> {
                    trendingUIData.postValue(TrendingUIState.Failed(repos.error))
                }
            }
        }
    }

    fun onInternet() {
        trendingUIData.postValue(TrendingUIState.InternetRestore)
    }

    fun onInternetLost() {
        trendingUIData.postValue(TrendingUIState.InternetFailure)
    }

    fun deleteTrendingRepos() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mTrendingRepo.deleteLocalTrendingRepos()
            }
            trendingUIData.postValue(TrendingUIState.Clear)
        }
    }
}