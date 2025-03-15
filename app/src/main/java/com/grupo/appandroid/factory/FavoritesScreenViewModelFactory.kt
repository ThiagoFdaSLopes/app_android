import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.grupo.appandroid.database.dao.AppDatabase
import com.grupo.appandroid.database.dao.FavoriteCandidateDao
import com.grupo.appandroid.database.dao.FavoriteJobDao

class FavoritesScreenViewModelFactory(
    private val favoriteCandidateDao: FavoriteCandidateDao,
    private val favoriteJobDao: FavoriteJobDao,
    private val database: AppDatabase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritesScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavoritesScreenViewModel(favoriteCandidateDao, favoriteJobDao, database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}