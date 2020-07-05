package com.test.tiket.ardanil.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.tiket.ardanil.viewmodel.MainActivityVM

class MainActivityVMF : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainActivityVM() as T
    }

}
