package com.linglingdr00.searchfunctionality

import android.content.SearchRecentSuggestionsProvider

class MySuggestionProvider: SearchRecentSuggestionsProvider() {
    init {
        setupSuggestions(AUTHORITY, MODE)
    }

    companion object {
        const val AUTHORITY = "com.linglingdr00.searchfunctionality.MySuggestionProvider"
        const val MODE: Int = SearchRecentSuggestionsProvider.DATABASE_MODE_QUERIES
    }
}