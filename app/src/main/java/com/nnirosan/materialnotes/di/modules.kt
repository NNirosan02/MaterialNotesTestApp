package com.nnirosan.materialnotes.di

import com.nnirosan.materialnotes.data.note.NoteRepository
import com.nnirosan.materialnotes.ui.shared.SharedViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { NoteRepository(get()) }
}

val viewModelModule = module {
    viewModel { SharedViewModel(get())}
}