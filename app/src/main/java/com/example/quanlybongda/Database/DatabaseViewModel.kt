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
import com.example.quanlybongda.Database.DAO.User.UserGroupDAO
import com.example.quanlybongda.Database.DAO.User.UserRoleDAO
import com.example.quanlybongda.Database.ReturnTypes.KetQuaTranDau
import com.example.quanlybongda.Database.ReturnTypes.UserGroupRoles
import com.example.quanlybongda.Database.Schema.User.HasRole
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.format.DateTimeFormatter
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

    private val userRoleDAO : UserRoleDAO;
    private val userGroupDAO : UserGroupDAO;

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
        userRoleDAO = appDatabase.userRoleDAO;
        userGroupDAO = appDatabase.userGroupDAO;
    }

    suspend fun selectKetQuaThiDau(maTD: Int): KetQuaTranDau? {
        val lichThiDau = lichThiDauDAO.selectLichThiDauMaTD(maTD);
        if (lichThiDau == null)
            return null;
        val doiMot = doiBongDAO.selectDoiBongMaDoi(lichThiDau.doiMot);
        val doiHai = doiBongDAO.selectDoiBongMaDoi(lichThiDau.doiHai);
        if (doiMot == null || doiHai == null)
            return null;
        val tySoDoiMot = banThangDAO.selectSoBanThangDoi(doiMot.maDoi);
        val tySoDoiHai = banThangDAO.selectSoBanThangDoi(doiHai.maDoi);

        val sanNha = sanNhaDAO.selectSanNhaMaSan(lichThiDau.maSan);
        return KetQuaTranDau(
            doiMot = doiMot,
            doiHai = doiHai,
            tySo = tySoDoiMot.toString() + "-" + tySoDoiHai.toString(),
            san = sanNha?.tenSan ?: "" ,
            ngayGio = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(lichThiDau.ngayGioThucTe).toString()
        )
    }

//    suspend fun checkPageViewable(groupId : Int, pageName: String) : Boolean {
//        val roles = userRoleDAO.selectRolesInGroup(groupId);
//        roles.observe(this) { role ->
//            if (pageName.matches(Regex(role.viewablePage)))
//                return true;
//        }
//        return false;
//    }
//
//    suspend fun checkPageEditable(groupId : Int, pageName: String) : Boolean {
//        val roles = userRoleDAO.selectRolesInGroup(groupId);
//        for (role in roles)
//            if (pageName.matches(Regex(role.viewablePage)))
//                return role.canEdit;
//        return false;
//    }

    suspend fun selectAllUserGroupWithRole() : List<UserGroupRoles> {
        val groups = userGroupDAO.selectAllUserGroup();
        var groupRoles = mutableListOf<UserGroupRoles>();
        for (group in groups) {
            val roles = userRoleDAO.selectRoleIdsInGroup(group.groupId);
            groupRoles.add(UserGroupRoles(groupId = group.groupId, groupName = group.groupName, roles = roles));
        }
        return groupRoles;
    }

    suspend fun updateRolesToGroup(groupId: Int, roles : List<Int>) {
        val groupRoles = userRoleDAO.selectRoleIdsInGroup(groupId);
        var rolesExcluded = roles.filter { value -> !groupRoles.contains(value); };
        if (rolesExcluded.size > 0) {
            userRoleDAO.upsertHasRole(rolesExcluded.map { value -> HasRole(groupId = groupId, roleId = value) });
        }

        rolesExcluded = groupRoles.filter { value -> !roles.contains(value) };
        if (rolesExcluded.size > 0) {
            userRoleDAO.deleteHasRole(rolesExcluded.map { value -> HasRole(groupId = groupId, roleId = value) });
        }

    }

}