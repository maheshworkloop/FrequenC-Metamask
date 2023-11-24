package com.dev.frequenc.ui_codes.util

import android.app.Activity
import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.dev.frequenc.ui_codes.screens.Dashboard.wallet.WalletViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(ActivityComponent::class)
internal object ViewModelModule {

    @Provides
    fun provideSharedViewModel(activity: FragmentActivity): WalletViewModel {
        return ViewModelProvider(activity).get(WalletViewModel::class.java)
    }
}

