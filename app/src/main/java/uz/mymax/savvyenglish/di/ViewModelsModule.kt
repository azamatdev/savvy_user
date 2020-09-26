package uz.mymax.savvyenglish.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import uz.mymax.savvyenglish.ui.auth.AuthViewModel
import uz.mymax.savvyenglish.ui.topics.LessonViewModel

val viewModelsModule = module {

    viewModel { LessonViewModel(get()) }

    viewModel { AuthViewModel(get()) }
}