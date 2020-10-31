package com.zedevstuds.jsonfakepics.utils

import android.view.View

// Устанаавливает видимость элементов состояния загрузки
fun setProgressViews(status: LoadingStatus, progressBar: View, textView: View) {
    when(status) {
        LoadingStatus.LOADING -> {
            progressBar.visibility = View.VISIBLE
            textView.visibility = View.GONE
        }
        LoadingStatus.DONE -> {
            progressBar.visibility = View.GONE
            textView.visibility = View.GONE
        }
        LoadingStatus.ERROR -> {
            progressBar.visibility = View.GONE
            textView.visibility = View.VISIBLE
        }
        else -> {
            progressBar.visibility = View.GONE
            textView.visibility = View.VISIBLE
        }
    }
}