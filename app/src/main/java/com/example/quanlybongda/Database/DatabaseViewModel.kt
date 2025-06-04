package com.example.quanlybongda.Database

import android.app.Application
import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.quanlybongda.Database.DAO.*
import com.example.quanlybongda.Database.DAO.User.*
import com.example.quanlybongda.Database.ReturnTypes.*
import com.example.quanlybongda.Database.Schema.MuaGiai
import com.example.quanlybongda.Database.Schema.User.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import com.example.quanlybongda.Database.Exceptions.*
import java.security.SecureRandom
import java.time.LocalDateTime

@HiltViewModel
class DatabaseViewModel @Inject constructor(application : Application) : ViewModel() {

    companion object {
        private var _currentMuaGiai = MutableStateFlow<MuaGiai?>(null);
        val currentMuaGiai : StateFlow<MuaGiai?> = _currentMuaGiai;
    }

    private var _user = MutableStateFlow<User?>(null);
    val user : StateFlow<User?> = _user;

    private var _session = MutableStateFlow<Session?>(null);
    val session : StateFlow<Session?> = _session;

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
                hang = 0,
                imageURL = doi.imageURL ?: ""
            )
            result.add(doiBXH);
        }
        return result;
    }

    suspend fun selectBXHDoiMuaGiai(maMG: Int) : List<BangXepHangNgay> {
        // Tat ca cac doi co tran co trung
        var result = mutableListOf<BangXepHangNgay>();

        val selectDoi = lichThiDauDAO.selectLichThiDauMaMG(maMG);

        // Chuyen no thanh set
        val doiCoTranTrongNgay = selectDoi.flatMap { listOf(it.doiMot, it.doiHai) }.toSet();
        for (doiCoTran in doiCoTranTrongNgay) {
            val doi = doiBongDAO.selectDoiBongMaDoi(doiCoTran);
            if (doi == null) {
                Log.e("TAG", "Khong ton tai doi bong nay");
                continue;
            }

            val soTran = lichThiDauDAO.countLichThiDauMaMG(doiCoTran, maMG);
            val soTranThang = lichThiDauDAO.countLichThiDauThangMaMG(doiCoTran, maMG);
            val soTranThua = lichThiDauDAO.countLichThiDauThuaMaMG(doiCoTran, maMG);
            val soTranHoa = soTran - soTranThang - soTranThua;
            val doiBXH  = BangXepHangNgay(
                maDoi = doi.maDoi!!,
                tenDoi = doi.tenDoi,
                soTran = soTran,
                soTranThang = soTranThang,
                soTranThua = soTranThua,
                soTranHoa = soTranHoa,
                hieuSo = soTranThang * 3 + soTranHoa * 1 + soTranThua * 0,
                hang = 0,
                imageURL = doi.imageURL ?: ""
            )
            result.add(doiBXH);
        }
        return result;
    }

    suspend fun checkPageViewable(groupId : Int, pageName: String) : Boolean {
        val roles = userRoleDAO.selectRolesInGroup(groupId);
        for (role in roles)
            if (role.viewablePage.toRegex(RegexOption.IGNORE_CASE).containsMatchIn(pageName))
                return true;
        return false;
    }

    suspend fun checkPageEditable(groupId : Int, pageName: String) : Boolean {
        val roles = userRoleDAO.selectRolesInGroup(groupId);
        for (role in roles) {
            if (role.viewablePage.toRegex(RegexOption.IGNORE_CASE).containsMatchIn(pageName))
                return role.canEdit;
        }
        return false;
    }


    fun generateSecureRandomBytes(size: Int): ByteArray {
        val secureRandom = SecureRandom()
        val bytes = ByteArray(size)
        secureRandom.nextBytes(bytes)
        return bytes
    }

    fun generateSessionToken() : String {
        val bytes = generateSecureRandomBytes(20);
        val token = Base64.encode(bytes, Base64.NO_PADDING);
        return token.toString().lowercase();
    }

    suspend fun createSession(token: String, userId: Int) : Session {
        val sessionId = sha256EncodeLowercase(token);
        val session = Session(
            sessionId = sessionId,
            userId = userId,
            expiresAt = LocalDateTime.now().plusDays(30)
        );
        userDAO.upsertSession(session);
        return session;
    }

    /*
    Sessions are validated in 2 steps:

    Does the session exist in your database?
    Is it still within expiration?
    */

    suspend fun validateSessionToken(token: String) : SessionValidationResult {
        val expiredTime : Long = 2592000L; // 30 days;
        val sessionId = sha256EncodeLowercase(token);
        val result = userDAO.selectUserSession(sessionId);

        _user.value = null;
        _session.value = null;

        if (result.size == 0) {
            return SessionValidationResult();
        }

        val (user, session) = result[0];

        if (session == null || user == null) {
            return SessionValidationResult();
        }

        if (LocalDateTime.now() >= session.expiresAt) {
            userDAO.deleteSession(session.sessionId);
            return SessionValidationResult();
        }

        // If expiresAt is less than 15days update to 30days
        if (LocalDateTime.now() >= session.expiresAt.minusSeconds(expiredTime / 2)) {
            session.expiresAt = LocalDateTime.now().plusSeconds(expiredTime);
            userDAO.updateSessionExpiration(session.sessionId, session.expiresAt);
        }

        _user.value = user;
        _session.value = session;
        return SessionValidationResult(user, session);
    }

    suspend fun invalidateSession(sessionId: String) {
        userDAO.deleteSession(sessionId);
    }

    suspend fun invalidateAllSessions(userId: Int) {
        userDAO.deleteAllUserSession(userId);
    }

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

    suspend fun isEmailAvailable(email: String) : Boolean {
        val row = userDAO.selectUserFromEmail(email);
        return row != null;
    }

    suspend fun createUser(email: String, username: String, password: String) : String {
        if (!verifyUsernameInput(username))
            throw UsernameFormat();
        if (!verifyEmailInput(email))
            throw EmailFormat()
        if (isEmailAvailable(email))
            throw EmailAvailability();
        val passwordHash = hashPassword(password);
        val user = User(
            email = email,
            username = username,
            passwordHash = passwordHash,
        )
        userDAO.upsertUser(user);

        return loginUser(username, password); // Return SessionToken
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

    suspend fun loginUser(username: String, password: String) : String {
        val passwordHash = hashPassword(password);
        val user = userDAO.selectUserFromUsername(username) ?: throw IncorrectUsername();
        if (user.passwordHash != passwordHash)
            throw IncorrectPassword();

        val sessionToken = generateSessionToken();
        createSession(sessionToken, user.id);
        validateSessionToken(sessionToken);

        return sessionToken;
    }
}