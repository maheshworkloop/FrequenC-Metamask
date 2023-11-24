package com.dev.frequenc.ui_codes.screens.Search

interface SearchNavigator {

    public fun onSearch(charsequence : CharSequence )
    public fun showMessage(localizedMessage : String)
    public fun searchResults(selectedSuggestionPos: Int)

}