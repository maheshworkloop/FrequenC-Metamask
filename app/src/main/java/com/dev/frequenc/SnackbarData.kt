package com.dev.frequenc

data class SnackbarData(val message: String, val action: (() -> Unit)?)