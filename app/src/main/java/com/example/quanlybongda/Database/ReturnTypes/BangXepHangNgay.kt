package com.example.quanlybongda.Database.ReturnTypes

import androidx.room.Ignore
import com.example.quanlybongda.Database.Schema.DoiBong

data class BangXepHangNgay(
    val maDoi: Int,
    val tenDoi: String,
    val soTran: Int,
    val soTranThang: Int,
    val soTranThua: Int,
    val soTranHoa: Int,
    val hieuSo: Int,
    val hang: Int,
    val imageURL : String,

) {
}
