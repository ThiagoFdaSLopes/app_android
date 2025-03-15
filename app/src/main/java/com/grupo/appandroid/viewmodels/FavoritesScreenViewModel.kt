import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grupo.appandroid.database.dao.FavoriteCandidate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.grupo.appandroid.database.dao.FavoriteCandidateDao
import com.grupo.appandroid.database.dao.FavoriteJob
import com.grupo.appandroid.database.dao.FavoriteJobDao

enum class UserType {
    USER, COMPANY
}

class FavoritesScreenViewModel(
    private val favoriteCandidateDao: FavoriteCandidateDao,
    private val favoriteJobDao: FavoriteJobDao
) : ViewModel() {

    private val _favoriteCandidates = MutableStateFlow<List<FavoriteCandidate>>(emptyList())
    val favoriteCandidates: StateFlow<List<FavoriteCandidate>> = _favoriteCandidates

    private val _favoriteJobs = MutableStateFlow<List<FavoriteJob>>(emptyList())
    val favoriteJobs: StateFlow<List<FavoriteJob>> = _favoriteJobs

    /**
     * Carrega os favoritos conforme o tipo do usuário e seu código único (userCode ou companyCode).
     *
     * @param userType Define se o usuário é COMPANY ou USER.
     * @param code O identificador único (companyCode para empresa ou userCode para usuário).
     */
    fun loadFavorites(userType: UserType, code: String) {
        viewModelScope.launch {
            when (userType) {
                UserType.COMPANY -> {
                    // Coleta os candidatos favoritados pela empresa.
                    favoriteCandidateDao.getFavoritesByCompany(code).collect { favorites ->
                        _favoriteCandidates.value = favorites
                    }
                }
                UserType.USER -> {
                    // Coleta as vagas favoritadadas pelo usuário.
                    favoriteJobDao.getFavoritesByUser(code).collect { favorites ->
                        _favoriteJobs.value = favorites
                    }
                }
            }
        }
    }
}
