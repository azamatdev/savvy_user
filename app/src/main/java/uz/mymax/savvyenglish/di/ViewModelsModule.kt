package uz.mymax.savvyenglish.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import uz.mymax.savvyenglish.ui.lessons.LessonViewModel

val viewModelsModule = module {

    viewModel { LessonViewModel(get()) }
}