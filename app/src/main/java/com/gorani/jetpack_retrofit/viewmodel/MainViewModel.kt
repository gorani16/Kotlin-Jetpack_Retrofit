package com.gorani.jetpack_retrofit.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gorani.jetpack_retrofit.api.MyApi
import com.gorani.jetpack_retrofit.api.RetrofitInstance
import com.gorani.jetpack_retrofit.model.Post
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val retrofitInstance = RetrofitInstance.getInstance().create(MyApi::class.java)

    private val _word1 = MutableLiveData<String>()
    val word1: LiveData<String> = _word1

    private val _word2 = MutableLiveData<String>()
    val word2: LiveData<String> = _word2

    private val _wordList = MutableLiveData<List<Post>>()
    val wordList: LiveData<List<Post>> = _wordList

    fun getPost1() = viewModelScope.launch {
        val post = retrofitInstance.getPost1()
        Log.d("MainViewModel", post.toString())
        _word1.value = post.title
    }

    fun getPost2(number: Int) = viewModelScope.launch {
        val post = retrofitInstance.getPostNumber(number)
        Log.d("MainViewModel", post.toString())
        _word2.value = post.title
    }

    fun getPostAll() = viewModelScope.launch {
        val postAll = retrofitInstance.getPostAll()
        Log.d("MainViewModel", postAll.toString())
        _wordList.value = postAll

    }

}