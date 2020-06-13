package uz.mymax.savvyenglish.di

import org.koin.dsl.module
import uz.mymax.savvyenglish.repository.LessonRepository

val appModule = module {
    single { LessonRepository(errorConverter = get(), api = get()) }
}