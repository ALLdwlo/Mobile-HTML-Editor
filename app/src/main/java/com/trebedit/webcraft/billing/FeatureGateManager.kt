package com.trebedit.webcraft.billing

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FeatureGateManager {
    
    private val _isPremiumUser = MutableStateFlow(false)
    val isPremiumUser: StateFlow<Boolean> = _isPremiumUser.asStateFlow()
    
    fun hasFeature(feature: PremiumFeature): Boolean {
        return when (feature) {
            PremiumFeature.UNLIMITED_PROJECTS -> _isPremiumUser.value
            PremiumFeature.CLOUD_SYNC -> _isPremiumUser.value
            PremiumFeature.ADVANCED_EXPORT -> _isPremiumUser.value
            PremiumFeature.NO_ADS -> _isPremiumUser.value
        }
    }
    
    fun canCreateProject(currentProjectCount: Int): Boolean {
        if (_isPremiumUser.value) {
            return true
        }
        return currentProjectCount < FREE_TIER_PROJECT_LIMIT
    }
    
    fun getRemainingProjects(currentProjectCount: Int): Int {
        if (_isPremiumUser.value) {
            return Int.MAX_VALUE
        }
        return (FREE_TIER_PROJECT_LIMIT - currentProjectCount).coerceAtLeast(0)
    }
    
    companion object {
        const val FREE_TIER_PROJECT_LIMIT = 3
    }
}

enum class PremiumFeature {
    UNLIMITED_PROJECTS,
    CLOUD_SYNC,
    ADVANCED_EXPORT,
    NO_ADS
}
