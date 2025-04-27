package com.example.quanlybongda.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.quanlybongda.Database.DAO.*
import com.example.quanlybongda.Database.DAO.User.UserDAO
import com.example.quanlybongda.Database.Schema.*
import com.example.quanlybongda.Database.Schema.Loai.*
import com.example.quanlybongda.Database.Schema.User.*

@Database(
    entities = [
        // User
        User::class,
        HasRole::class,
        UserGroup::class,
        UserRole::class,
        ThamSo::class,

        SanNha::class,
        LoaiBT::class,
        LoaiCT::class,
        LoaiTP::class,
        ViTri::class,
        VongTD::class,

        MuaGiai::class,

        CauThu::class,
        DoiBong::class,

//        ThamGiaDB::class,
        ThamGiaTD::class,

        LichThiDau::class,
        BanThang::class,
        ThePhat::class,
        TrongTai::class,

        // Backup
        SanNhaBackup::class,
        LoaiBTBackup::class,
        LoaiCTBackup::class,
        LoaiTPBackup::class,
        ViTriBackup::class,
        VongTDBackup::class,

        MuaGiaiBackup::class,

        CauThuBackup::class,
        DoiBongBackup::class,

//        ThamGiaDBBackup::class,
        ThamGiaTDBackup::class,

        LichThiDauBackup::class,
        BanThangBackup::class,
        ThePhatBackup::class,
        TrongTaiBackup::class,

    ], version = 1)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val userDAO: UserDAO;
    abstract val userSettingsDAO: ThamSoDAO;

    abstract val cauThuDAO: CauThuDAO;
    abstract val doiBongDAO: DoiBongDAO;
    abstract val muaGiaiDAO: MuaGiaiDAO;
    abstract val sanNhaDAO: SanNhaDAO;

//    abstract val thamGiaDBDAO: ThamGiaDBDAO;
    abstract val lichThiDauDAO: LichThiDauDAO;
    abstract val banThangDAO: BanThangDAO;
    abstract val thePhatDAO: ThePhatDAO;


    companion object {
        @Volatile
        private var INSTANCE : AppDatabase? = null;

        fun getDatabase(context : Context) : AppDatabase {
            if (INSTANCE != null)
                return INSTANCE as AppDatabase;

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .createFromAsset("database/db.sqlite")
//                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance;
                return instance;
            }
        }
    }

}