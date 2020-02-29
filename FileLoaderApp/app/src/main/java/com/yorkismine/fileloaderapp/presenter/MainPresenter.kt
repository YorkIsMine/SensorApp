package com.yorkismine.fileloaderapp.presenter

import android.os.Looper
import android.util.Log
import com.yorkismine.fileloaderapp.DownloadFileUseCase
import com.yorkismine.fileloaderapp.DownloadFileUseCaseImpl
import com.yorkismine.fileloaderapp.model.DownloadedItem
import com.yorkismine.fileloaderapp.utils.NetworkHelper
import com.yorkismine.fileloaderapp.contracts.MainContract
import com.yorkismine.fileloaderapp.contracts.MainContract.Presenter
import com.yorkismine.fileloaderapp.view.HistoryActivity
import kotlin.collections.ArrayList

class MainPresenter(private val view: MainContract.View) : Presenter {
    private var list: ArrayList<DownloadedItem> = ArrayList()

    override fun downloadFile(url: String, dir: String) {
        view.isHistoryButtonClickable(false)

        val df: DownloadFileUseCase = DownloadFileUseCaseImpl()
        list = df.downloadFile(url, dir) as ArrayList<DownloadedItem>

        view.isHistoryButtonClickable(true)
        if (list.size <= 0) view.showError()
        else view.showResult()
    }

    override fun uploadFiles() {
        val pref = view.preferences
        var i = pref.getInt("INT_SAVER", 0)
        if ((i == 0) or (i < list.size)) {
            if (list.isNotEmpty()) {
                while (i <= list.size) {
                    for (item in list) {
                        val editor = pref.edit()
                        editor.putString(HistoryActivity.DATE_ITEM + "$i", item.date)
                        editor.putString(HistoryActivity.SITE_ITEM + "$i", item.site)
                        editor.putLong(HistoryActivity.SIZE_ITEM + "$i", item.sizeOfItem)
                        editor.apply()
                        Log.d("YYYY", "$i is normal")
                    }
                    i++
                }
                val saverI = pref.edit()
                saverI.putInt("INT_SAVER", i)
                saverI.apply()
            }
        } else {
            Log.d("YYYY", "else is called")
            Log.d("YYYY", "size is ${list.size}")
            var j = 0
            i++
            while (j <= list.size){
                for (item in list){
                    val editor = pref.edit()
                    editor.putString(HistoryActivity.DATE_ITEM + "$i", item.date)
                    editor.putString(HistoryActivity.SITE_ITEM + "$i", item.site)
                    editor.putLong(HistoryActivity.SIZE_ITEM + "$i", item.sizeOfItem)
                    editor.apply()
                    Log.d("YYYY", "$i is normal")
                }
                j++
                i++
            }
        }
    }
}