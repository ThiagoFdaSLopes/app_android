package com.grupo.appandroid.database.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.grupo.appandroid.model.Company
import com.grupo.appandroid.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [User::class, Company::class, FavoriteJob::class, FavoriteCandidate::class], version = 5, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDAO
    abstract fun companyDao(): CompanyDAO
    abstract fun favoriteCandidateDao(): FavoriteCandidateDao
    abstract fun favoriteJobDao(): FavoriteJobDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                INSTANCE?.let { database ->
                                    val sampleUsers = getSampleUsers()
                                    database.userDao().insertAll(sampleUsers)
                                    // Optionally log or verify insertion
                                    println("Inserted ${sampleUsers.size} sample users into the database")
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private fun getSampleUsers() = listOf(
            User(
                userCode = 1L, // Changed to String for consistency
                name = "Carlos Eduardo",
                phone = "11987654321",
                email = "carlos@example.com",
                password = "hashedPass123", // Consider hashing in a real app
                document = "123.456.789-00",
                location = "São Paulo - SP",
                skills = "Desenvolvedor FullStack",
                description = "5 anos",
                academyLevel = "Bacharelado",
                academyCourse = "Ciência da Computação",
                academyInstitution = "USP",
                academyLastYear = "2018"
            ),

            User(
                userCode = 3L,
                name = "João Pedro",
                phone = "31985432178",
                email = "joao.pedro@example.com",
                password = "hashedPass789",
                document = "456.789.123-11",
                location = "Belo Horizonte - MG",
                skills = "Engenheiro de Dados",
                description = "4 anos",
                academyLevel = "Mestrado",
                academyCourse = "Engenharia de Software",
                academyInstitution = "UFMG",
                academyLastYear = "2019"
            ),
            User(
                userCode = 4L,
                name = "Mariana Silva",
                phone = "41987651234",
                email = "mariana.silva@example.com",
                password = "hashedPass101",
                document = "321.654.987-22",
                location = "Curitiba - PR",
                skills = "Analista de Marketing Digital",
                description = "2 anos",
                academyLevel = "Graduação",
                academyCourse = "Publicidade e Propaganda",
                academyInstitution = "UFPR",
                academyLastYear = "2021"
            ),
            User(
                userCode = 5L,
                name = "Lucas Almeida",
                phone = "51998765432",
                email = "lucas.almeida@example.com",
                password = "hashedPass202",
                document = "654.321.987-33",
                location = "Porto Alegre - RS",
                skills = "Desenvolvedor Mobile",
                description = "6 anos",
                academyLevel = "Bacharelado",
                academyCourse = "Sistemas de Informação",
                academyInstitution = "UFRGS",
                academyLastYear = "2017"
            )
        )
    }
}