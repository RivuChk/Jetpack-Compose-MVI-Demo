package dev.rivu.mvijetpackcomposedemo.base.presentation.injection

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dev.rivu.mvijetpackcomposedemo.base.presentation.ISchedulerProvider
import dev.rivu.mvijetpackcomposedemo.base.presentation.SchedulerProvider
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppPresentationModule {

    @Provides
    @Singleton
    fun provideSchedulerProvider(): ISchedulerProvider =
        SchedulerProvider()
}