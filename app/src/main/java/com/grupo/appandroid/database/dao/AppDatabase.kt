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

@Database(entities = [User::class, Company::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDAO
    abstract fun companyDao(): CompanyDAO

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
                                    database.userDao().insertAll(getSampleUsers())
                                    database.companyDao().insertAll(getSampleCompanies())
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
                userCode = 1L,
                name = "Carlos Eduardo",
                phone = "11987654321",
                email = "carlos@example.com",
                password = "hashedPass123",
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
                userCode = 2L,
                name = "Ana Clara",
                phone = "21976543210",
                email = "ana.clara@example.com",
                password = "hashedPass456",
                document = "987.654.321-00",
                location = "Rio de Janeiro - RJ",
                skills = "Designer UX/UI",
                description = "3 anos",
                academyLevel = "Graduação",
                academyCourse = "Design Gráfico",
                academyInstitution = "UFRJ",
                academyLastYear = "2020"
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

        private fun getSampleCompanies() = listOf(
            Company(
                companyCode = 1L,
                companyName = "TechCorp",
                phone = "11999999999",
                email = "contact@techcorp.com",
                password = "hashedPassCorp1",
                document = "12.345.678/0001-99",
                location = "São Paulo - SP",
                industry = "Tecnologia",
                description = "Desenvolvedor FullStack - Remoto - R$ 8.000"
            ),
            Company(
                companyCode = 2L,
                companyName = "InovaLabs",
                phone = "21988884444",
                email = "hr@inovalabs.com",
                password = "hashedPassCorp2",
                document = "98.765.432/0001-88",
                location = "Rio de Janeiro - RJ",
                industry = "Inovação",
                description = "Designer UX/UI - Presencial - R$ 6.500"
            ),
            Company(
                companyCode = 3L,
                companyName = "DataFlow",
                phone = "31977773333",
                email = "jobs@dataflow.com",
                password = "hashedPassCorp3",
                document = "45.678.123/0001-77",
                location = "Belo Horizonte - MG",
                industry = "Big Data",
                description = "Engenheiro de Dados - Híbrido - R$ 9.500"
            ),
            Company(
                companyCode = 4L,
                companyName = "CreativeMind",
                phone = "41966662222",
                email = "careers@creativemind.com",
                password = "hashedPassCorp4",
                document = "32.154.987/0001-66",
                location = "Curitiba - PR",
                industry = "Marketing",
                description = "Analista de Marketing Digital - Remoto - R$ 5.000"
            ),
            Company(
                companyCode = 5L,
                companyName = "MobileWorks",
                phone = "51955551111",
                email = "recruit@mobileworks.com",
                password = "hashedPassCorp5",
                document = "65.432.198/0001-55",
                location = "Porto Alegre - RS",
                industry = "Desenvolvimento Mobile",
                description = "Desenvolvedor Mobile - Presencial - R$ 7.800"
            )
        )
    }
}
