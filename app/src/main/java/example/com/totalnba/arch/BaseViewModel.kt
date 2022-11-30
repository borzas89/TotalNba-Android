package example.com.totalnba.arch

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy

open class BaseViewModel : ViewModel() {
    protected val compositeDisposable = CompositeDisposable()

    val isLoadingProgress = ObservableField<Boolean>()
    val listViewVisible = ObservableField<Boolean>()
    val emptyViewVisible = ObservableField<Boolean>()

    fun <T : Any> Single<T>.simpleSubscribe(
        onSuccess: (T) -> Unit = {},
        onError: (Throwable) -> Unit = {}
    ) = observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe {
            listViewVisible.set(false)
            isLoadingProgress.set(true)
        }.doOnError {
            emptyViewVisible.set(true)
            listViewVisible.set(false)
        }.doFinally {
            isLoadingProgress.set(false)
        }.subscribeBy(onError, onSuccess)

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}