package example.com.totalnba.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import example.com.totalnba.navigator.AppNavigator
import example.com.totalnba.navigator.AppNavigatorImpl

@InstallIn(ActivityComponent::class)
@Module
abstract class NavigationModule {

    @Binds
    abstract fun bindNavigator(navigator: AppNavigatorImpl): AppNavigator
}