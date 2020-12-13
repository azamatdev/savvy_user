package uz.mymax.savvyenglish.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.inject
import uz.mymax.savvyenglish.ui.auth.AuthViewModel
import uz.mymax.savvyenglish.ui.question.QuestionSetViewModel
import uz.mymax.savvyenglish.ui.question.SharedViewModel
import uz.mymax.savvyenglish.ui.tests.TestViewModel
import uz.mymax.savvyenglish.ui.topics.TopicViewModel

val viewModelsModule = module {

    viewModel { TopicViewModel(get()) }

    viewModel { AuthViewModel(get()) }

    viewModel { TestViewModel(get()) }

    viewModel { QuestionSetViewModel(get()) }

    single { SharedViewModel() }
}