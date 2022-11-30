package example.com.totalnba.util

import androidx.databinding.Observable
import androidx.databinding.ObservableField
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun <T> ObservableField<T>.toFlowable(): Flowable<T> {
    return Flowable.create({ emitter ->
        get()?.let { nextValue -> emitter.onNext(nextValue) }
        val callback = object : Observable.OnPropertyChangedCallback() {

            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                get()?.let { nextValue -> emitter.onNext(nextValue) }
            }
        }
        addOnPropertyChangedCallback(callback)
        emitter.setCancellable { removeOnPropertyChangedCallback(callback) }
    }, BackpressureStrategy.BUFFER)
}

fun Disposable.disposedBy(bag: CompositeDisposable) {
    bag.add(this)
}