package com.example.quanlybongda.Services

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quanlybongda.Services.Data.Competition
import com.example.quanlybongda.Services.Data.Match
import com.example.quanlybongda.Services.Data.Standing
import com.example.quanlybongda.Services.Data.Team
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

open class LoadingState<out T> {
    object Loading : LoadingState<Nothing>()
    data class Success<T>(val data: T) : LoadingState<T>()
    data class Error(val message: String) : LoadingState<Nothing>()
}

@HiltViewModel
class FootballAPIViewModel @Inject constructor() : ViewModel() {
    private var _teams : MutableStateFlow<LoadingState<List<Team>>> = MutableStateFlow(LoadingState.Loading);
    val teams : StateFlow<LoadingState<List<Team>>> get() = _teams;

    private var _matches : MutableStateFlow<LoadingState<List<Match>>> = MutableStateFlow(LoadingState.Loading);
    val matches : StateFlow<LoadingState<List<Match>>> get() = _matches;

    private var _standings : MutableStateFlow<LoadingState<List<Standing>>> = MutableStateFlow(LoadingState.Loading);
    val standings : StateFlow<LoadingState<List<Standing>>> get() = _standings;

    private var _competitions : MutableStateFlow<LoadingState<List<Competition>>> = MutableStateFlow(LoadingState.Loading);
    val competitions : StateFlow<LoadingState<List<Competition>>> get() = _competitions;

    fun loadCompetitions() {
        if (_competitions.value is LoadingState.Success) return
        _competitions.value = LoadingState.Loading
        viewModelScope.launch {
            try {
                val competitionsResponse = FootballAPI.retrofitService.getCompetitions();
                if (competitionsResponse.isSuccessful) {
                    val competitions = competitionsResponse.body()!!.competitions;
                    _competitions.value = LoadingState.Success(competitions)
                } else {
                    _competitions.value = LoadingState.Error(competitionsResponse.message())
                }
            }
            catch (e: Exception) {
                _competitions.value = LoadingState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun loadTeams() {
        if (_teams.value is LoadingState.Success) return
        _teams.value = LoadingState.Loading
        viewModelScope.launch {
            try {
                val teamsResponse = FootballAPI.retrofitService.getCompetitionTeams("PL");
                if (teamsResponse.isSuccessful) {
                    val teams = teamsResponse.body()!!.teams;
                    _teams.value = LoadingState.Success(teams)
                } else {
                    _teams.value = LoadingState.Error(teamsResponse.message())
                }
            }
            catch (e: Exception) {
                _teams.value = LoadingState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun loadMatches() {
        if (_matches.value is LoadingState.Success) return
        _matches.value = LoadingState.Loading
        viewModelScope.launch {
            try {
                val matchesResponse = FootballAPI.retrofitService.getCompetitionMatches("PL");
                if (matchesResponse.isSuccessful) {
                    val matches = matchesResponse.body()!!.matches;
                    _matches.value = LoadingState.Success(matches)
                } else {
                    _matches.value = LoadingState.Error(matchesResponse.message())
                }
            }
            catch (e: Exception) {
                _matches.value = LoadingState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun loadStandings() {
        if (_standings.value is LoadingState.Success) return
        _standings.value = LoadingState.Loading
        viewModelScope.launch {
            try {
                val standingsResponse = FootballAPI.retrofitService.getCompetitionStandings("PL");
                if (standingsResponse.isSuccessful) {
                    val standings = standingsResponse.body()!!.standings;
                    _standings.value = LoadingState.Success(standings)
                } else {
                    _standings.value = LoadingState.Error(standingsResponse.message())
                }
            }
            catch (e: Exception) {
                _standings.value = LoadingState.Error(e.message ?: "Unknown error")
            }
        }
    }
}