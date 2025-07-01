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
import java.time.LocalDateTime
import javax.inject.Inject

open class LoadingState<out T> {
    object Loading : LoadingState<Nothing>()
    data class Success<T>(val data: T) : LoadingState<T>()
    data class Error(val message: String) : LoadingState<Nothing>()
}

@HiltViewModel
class FootballAPIViewModel @Inject constructor() : ViewModel() {
    private var _currentSeason : MutableStateFlow<Int> = MutableStateFlow(LocalDateTime.now().year);
    val currentSeason : StateFlow<Int> get() = _currentSeason;

    private var _currentCompetition : MutableStateFlow<Competition?> = MutableStateFlow(null);
    val currentCompetition : StateFlow<Competition?> get() = _currentCompetition;

    private var _teams : MutableStateFlow<LoadingState<List<Team>>> = MutableStateFlow(LoadingState.Loading);
    val teams : StateFlow<LoadingState<List<Team>>> get() = _teams;

    private var _matches : MutableStateFlow<LoadingState<List<Match>>> = MutableStateFlow(LoadingState.Loading);
    val matches : StateFlow<LoadingState<List<Match>>> get() = _matches;

    private var _standings : MutableStateFlow<LoadingState<List<Standing>>> = MutableStateFlow(LoadingState.Loading);
    val standings : StateFlow<LoadingState<List<Standing>>> get() = _standings;

    private var _competitions : MutableStateFlow<LoadingState<List<Competition>>> = MutableStateFlow(LoadingState.Loading);
    val competitions : StateFlow<LoadingState<List<Competition>>> get() = _competitions;

    private var _currentSeasonCompetitions : MutableStateFlow<LoadingState<List<Competition>>> = MutableStateFlow(LoadingState.Loading);
    val currentSeasonCompetitions : StateFlow<LoadingState<List<Competition>>> get() = _currentSeasonCompetitions;

    private var _loadingSeasonDetail : MutableStateFlow<Boolean> = MutableStateFlow(false);
    val loadingSeasonsDetail : StateFlow<Boolean> get() = _loadingSeasonDetail;

    fun setCurrentSeason(season: Int) {
        _currentSeason.value = season;
        _teams.value = LoadingState.Loading;
        _matches.value = LoadingState.Loading;
        _standings.value = LoadingState.Loading;
    }

    fun setCurrentCompetition(competition: Competition?) {
        _currentCompetition.value = competition;
        _teams.value = LoadingState.Loading;
        _matches.value = LoadingState.Loading;
        _standings.value = LoadingState.Loading;
    }

    fun loadCompetitions() {
        if (_competitions.value is LoadingState.Success) return
        _competitions.value = LoadingState.Loading
        viewModelScope.launch {
            try {
                _loadingSeasonDetail.value = true;
                val competitionsResponse = FootballAPI.retrofitService.getCompetitions();
                if (competitionsResponse.body() == null) {
                    throw RuntimeException("There was an error fetching competitions data. Please try again later.");
                }
                if (competitionsResponse.isSuccessful) {
                    val competitions = competitionsResponse.body()!!.competitions.toMutableList();
                    _competitions.value = LoadingState.Success(competitions)
                    loadCompetitionCurrentSeason();

                    // Load competition seasons details
                    for (i in competitions.indices) {
                        val competitionResponse =
                            FootballAPI.retrofitService.getCompetition(competitions[i].code).body();
                        if (competitionResponse != null) {
                            competitions[i].seasons = competitionResponse.seasons?.subList(0, 5);
                        }
                    }
                    _competitions.value = LoadingState.Success(competitions)

                } else {
                    _competitions.value = LoadingState.Error(competitionsResponse.message())
                }
                _loadingSeasonDetail.value = false;
            }
            catch (e: Exception) {
                _competitions.value = LoadingState.Error(e.message ?: "Unknown error")
            }
            loadCompetitionCurrentSeasonDetail();
        }
    }

    fun loadCompetitionCurrentSeasonDetail() {
        if (competitions.value is LoadingState.Error) {
            _currentSeasonCompetitions.value = competitions.value;
            return;
        }
        if (competitions.value !is LoadingState.Success) return;
        viewModelScope.launch {
            _currentSeasonCompetitions.value = LoadingState.Loading;
            val result = mutableListOf<Competition>();
            for (competition in (competitions.value as LoadingState.Success<List<Competition>>).data) {
                for (season in competition.seasons ?: emptyList()) {
                    if (season.startDate.year == currentSeason.value) {
                        result.add(competition.copy(currentSeason = season));
                        break;
                    }
                }
            }
            _currentSeasonCompetitions.value = LoadingState.Success(result);
        }
    }

    fun loadCompetitionCurrentSeason() {
        if (competitions.value !is LoadingState.Success) return;
        viewModelScope.launch {
            _currentSeasonCompetitions.value = LoadingState.Loading;
            val result = mutableListOf<Competition>();
            for (competition in (competitions.value as LoadingState.Success<List<Competition>>).data) {
                if (competition.currentSeason.startDate.year == currentSeason.value) {
                    result.add(competition);
                    break;
                }
            }
            _currentSeasonCompetitions.value = LoadingState.Success(result);
        }
    }

    fun loadTeams() {
        if (_teams.value is LoadingState.Success) return
        _teams.value = LoadingState.Loading
        viewModelScope.launch {
            try {
                val teamsResponse = FootballAPI.retrofitService.getCompetitionTeams(currentCompetition.value!!.code, currentSeason.value);
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
                val matchesResponse = FootballAPI.retrofitService.getCompetitionMatches(currentCompetition.value!!.code, currentSeason.value);
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
                val standingsResponse = FootballAPI.retrofitService.getCompetitionStandings(currentCompetition.value!!.code, currentSeason.value);
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