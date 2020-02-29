package com.yorkismine.fileloaderapp

import android.os.Looper
import android.util.Log
import com.yorkismine.fileloaderapp.model.DownloadedItem
import com.yorkismine.fileloaderapp.utils.NetworkHelper
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DownloadFileUseCaseImpl : DownloadFileUseCase {
    override fun downloadFile(url: String, dir: String) : ArrayList<DownloadedItem> {
        var length: Long = 0

        val list: ArrayList<DownloadedItem> = ArrayList()

        val l: Long = 1
        Observable.just(l)
            .observeOn(Schedulers.io())
            .map(object : Function<Long, Long> {
                override fun apply(t: Long): Long {
                    var n = t
                    n = NetworkHelper.downloadAndGetSizeOfFile(url, dir)
                    Log.d("YYYY", "n is $n")

                    return n
                }

            })
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Long> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: Long) {
                    Log.d("YYYY", t.toString())
                    length = t
                    Log.d("YYYY", "done!")
                    val site = url.subSequence(0, (url.indexOf("/", 10)) + 1) as String
                    Log.d("YYYY", site)
                    val s = SimpleDateFormat("dd MMM YYYY", Locale.ENGLISH)
                    val d = Date()

                    val item = DownloadedItem(
                        s.format(d), site, length / 1024 / 1024
                    )
                    list.add(item)
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }

            })

        Log.d("YYYY", "${list.size}")

        return list
    }
}