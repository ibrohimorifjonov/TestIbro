package uz.test.api.di

import org.koin.dsl.module
import uz.test.api.repository.IBroRepository

val appModule= module {
    single { IBroRepository(api=get()) }
}