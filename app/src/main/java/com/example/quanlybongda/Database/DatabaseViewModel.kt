package com.example.quanlybongda.Database

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quanlybongda.Database.DAO.BanThangDAO
import com.example.quanlybongda.Database.DAO.CauThuDAO
import com.example.quanlybongda.Database.DAO.DoiBongDAO
import com.example.quanlybongda.Database.DAO.LichThiDauDAO
import com.example.quanlybongda.Database.DAO.MuaGiaiDAO
import com.example.quanlybongda.Database.DAO.SanNhaDAO
import com.example.quanlybongda.Database.DAO.ThamSoDAO
import com.example.quanlybongda.Database.DAO.ThePhatDAO
import com.example.quanlybongda.Database.Schema.CauThu
import com.example.quanlybongda.Database.Schema.Loai.SanNha
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DatabaseViewModel @Inject constructor(application : Application) : ViewModel() {

    private val cauThuDAO : CauThuDAO;
    private val doiBongDAO : DoiBongDAO;
    private val lichThiDauDAO : LichThiDauDAO;
    private val muaGiaiDAO : MuaGiaiDAO;
    private val sanNhaDAO : SanNhaDAO;
    private val thamSoDAO : ThamSoDAO;
    private val thePhatDAO : ThePhatDAO;
    private val banThangDAO : BanThangDAO;

    init {
        val appDatabase = AppDatabase.getDatabase(application);
        cauThuDAO = appDatabase.cauThuDAO;
        doiBongDAO = appDatabase.doiBongDAO;
        lichThiDauDAO = appDatabase.lichThiDauDAO;
        muaGiaiDAO = appDatabase.muaGiaiDAO;
        sanNhaDAO = appDatabase.sanNhaDAO;
        thamSoDAO = appDatabase.thamSoDAO;
        thePhatDAO = appDatabase.thePhatDAO;
        banThangDAO = appDatabase.banThangDAO;
    }

    suspend fun selectAllCauThu(): List<CauThu> {
        return cauThuDAO.selectAlLCauThu();
    }

}