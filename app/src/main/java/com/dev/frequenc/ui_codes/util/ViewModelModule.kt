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
import dagger.hilt.android.scopes.ActivityScoped

//
//@Module
//@InstallIn(ActivityComponent::class)
//object ViewModelModule {
//
//    @Provides
//    @ActivityScoped
//    fun provideSharedViewModel(): WalletViewModel {
//        return ViewModelProvider().get(WalletViewModel::class.java)
//    }
//}
