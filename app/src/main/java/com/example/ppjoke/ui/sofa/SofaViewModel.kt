package com.example.ppjoke.ui.sofa

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * @author: zangjin
 * @date: 2022/4/4
 */
class SofaViewModel: ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is sofa Fragment"
    }
    val text: LiveData<String> = _text
}