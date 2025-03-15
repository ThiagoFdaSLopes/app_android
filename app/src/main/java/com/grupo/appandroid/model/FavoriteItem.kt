package com.grupo.appandroid.model

import com.grupo.appandroid.database.dao.FavoriteCandidate
import com.grupo.appandroid.database.dao.FavoriteJob

sealed class FavoriteItem {
    data class CandidateFavorite(val candidate: FavoriteCandidate) : FavoriteItem()
    data class JobFavorite(val job: FavoriteJob) : FavoriteItem()
}
