package com.sloupycom.shaper.core.di

import com.sloupycom.shaper.data.source.local.Local
import com.sloupycom.shaper.data.source.local.TaskLocalDataSourceImpl
import com.sloupycom.shaper.data.source.repository.TaskRepository
import com.sloupycom.shaper.data.source.repository.TaskRepositoryImpl
import com.sloupycom.shaper.viewmodel.MainActivityViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val tasksModule = module {

    single<TaskRepository> {
        TaskRepositoryImpl(TaskLocalDataSourceImpl(Local.getInstance(androidApplication()).localDao))
    }

    viewModel { MainActivityViewModel(get()) }
}