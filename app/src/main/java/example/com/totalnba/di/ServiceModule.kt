package example.com.totalnba.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import example.com.totalnba.data.remote.*

@InstallIn(SingletonComponent::class)
@Module
abstract class ServiceModule {

    @Binds
    abstract fun bindPredictionService(impl: PredictionServiceImpl): PredictionService

    @Binds
    abstract fun bindAdjustmentService(impl: AdjustmentServiceImpl): AdjustmentService

    @Binds
    abstract fun bindPlayerStatService(impl: PlayerStatServiceImpl): PlayerStatService

    @Binds
    abstract fun bindResultService(impl: ResultServiceImpl): ResultService
}