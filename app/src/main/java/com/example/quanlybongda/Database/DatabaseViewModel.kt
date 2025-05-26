package com.example.quanlybongda.Database

import android.app.Application
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quanlybongda.Database.DAO.*
import com.example.quanlybongda.Database.DAO.User.*
import com.example.quanlybongda.Database.ReturnTypes.*
import com.example.quanlybongda.Database.Schema.MuaGiai
import com.example.quanlybongda.Database.Schema.User.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.math.exp

@HiltViewModel
class DatabaseViewModel @Inject constructor(application : Application) : ViewModel() {

    companion object {
        private var _currentMuaGiai = MutableStateFlow<MuaGiai?>(null);
        val currentMuaGiai : StateFlow<MuaGiai?> = _currentMuaGiai;
    }

    public val cauThuDAO : CauThuDAO;
    public val doiBongDAO : DoiBongDAO;
    public val lichThiDauDAO : LichThiDauDAO;
    public val muaGiaiDAO : MuaGiaiDAO;
    public val sanNhaDAO : SanNhaDAO;
    public val thamSoDAO : ThamSoDAO;
    public val thePhatDAO : ThePhatDAO;
    public val banThangDAO : BanThangDAO;

    public val userDAO : UserDAO;
    public val userRoleDAO : UserRoleDAO;
    public val userGroupDAO : UserGroupDAO;

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
        userDAO = appDatabase.userDAO;
    }

    suspend fun selectKetQuaThiDau(maTD: Int): KetQuaTranDau? {
        val lichThiDau = lichThiDauDAO.selectLichThiDauMaTD(maTD);
        if (lichThiDau == null)
            return null;
        val doiMot = doiBongDAO.selectDoiBongMaDoi(lichThiDau.doiMot);
        val doiHai = doiBongDAO.selectDoiBongMaDoi(lichThiDau.doiHai);
        if (doiMot == null || doiHai == null)
            return null;
        val tySoDoiMot = banThangDAO.selectSoBanThangDoi(doiMot.maDoi!!);
        val tySoDoiHai = banThangDAO.selectSoBanThangDoi(doiHai.maDoi!!);

        val sanNha = sanNhaDAO.selectSanNhaMaSan(lichThiDau.maSan);
        return KetQuaTranDau(
            doiMot = doiMot,
            doiHai = doiHai,
            tySo = tySoDoiMot.toString() + "-" + tySoDoiHai.toString(),
            san = sanNha?.tenSan ?: "" ,
            ngayGio = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(lichThiDau.ngayGioThucTe).toString()
        )
    }

    fun selectMuaGiai(muaGiai: MuaGiai) {
        _currentMuaGiai.value = muaGiai;
    }

    suspend fun selectBXHDoiNgay(ngay : LocalDate) : List<BangXepHangNgay> {
        // Tat ca cac doi co tran co trung
        var result = mutableListOf<BangXepHangNgay>();

        val selectDoi = lichThiDauDAO.selectLichThiDauTrongNgay(ngay);

        // Chuyen no thanh set
        val doiCoTranTrongNgay = selectDoi.flatMap { listOf(it.doiMot, it.doiHai) }.toSet();
        for (doiCoTran in doiCoTranTrongNgay) {
            val doi = doiBongDAO.selectDoiBongMaDoi(doiCoTran);
            if (doi == null) {
                Log.e("TAG", "Khong ton tai doi bong nay");
                continue;
            }

            val soTran = lichThiDauDAO.countLichThiDauMaDoi(doiCoTran, ngay);
            val soTranThang = lichThiDauDAO.countLichThiDauThangMaDoi(doiCoTran, ngay);
            val soTranThua = lichThiDauDAO.countLichThiDauThuaMaDoi(doiCoTran, ngay);
            val soTranHoa = soTran - soTranThang - soTranThua;
            val doiBXH  = BangXepHangNgay(
                maDoi = doi.maDoi!!,
                tenDoi = doi.tenDoi,
                soTran = soTran,
                soTranThang = soTranThang,
                soTranThua = soTranThua,
                soTranHoa = soTranHoa,
                hieuSo = soTranThang * 3 + soTranHoa * 1 + soTranThua * 0,
                hang = 0
            )
            result.add(doiBXH);
        }
        return result;
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

    suspend fun checkEmailAvailability(email: String) : Boolean {
        val row = userDAO.selectUserFromEmail(email);
        return row == null;
    }

    suspend fun createUser(email: String, username: String, password: String) : User? {
        if (!verifyUsernameInput(username))
            throw java.lang.Exception("UsernameFormat");
        if (!verifyEmailInput(email))
            throw java.lang.Exception("EmailFormat")
        if (checkEmailAvailability(email))
            throw java.lang.Exception("EmailAvailability");
        val passwordHash = hashPassword(password);
        val user = User(
            email = email,
            username = username,
            passwordHash = passwordHash,
        )
        userDAO.upsertUser(user);
        return user;
    }

    suspend fun updateUserPassword(userId: Int, password: String) {
        val passwordHash = hashPassword(password);
        userDAO.updateUserPasswordHash(userId, password);
    }

//    suspend fun updateUser(user: User) {
//        db.update(UserTable)
//            .set({ username: user.username, email: user.email, groupId: user.groupId }).where(eq(UserTable.id, user.id));
//    }

    suspend fun selectUserPasswordHash(userId: Int) : String? {
        return userDAO.selectUserPasswordHash(userId);
    }

    suspend fun selectUserFromEmail(email : String) : User? {
        return userDAO.selectUserFromEmail(email);
    }

    suspend fun selectUserFromUsername(username : String) : User? {
        return userDAO.selectUserFromUsername(username);
    }

    suspend fun selectAllUser() : List<User> {
        return userDAO.selectAllUsers();
    }

    suspend fun deleteUser(userId : Int) {
        userDAO.deleteUser(userId);
    }

    suspend fun signIn(username: String, password: String) : Boolean {
        val passwordHash = hashPassword(password);
        val user = userDAO.selectUserFromUsername(username) ?: return false;
        if (user.passwordHash == passwordHash)
            return true;
        return false;
    }
}