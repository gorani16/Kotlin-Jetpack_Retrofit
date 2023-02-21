package com.gorani.jetpack_retrofit.simple_coroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.gorani.jetpack_retrofit.R

class CoMainActivity2 : AppCompatActivity() {

    private val viewModel : CoViewModel by lazy {
        ViewModelProvider(this)[CoViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_co_main2)

        viewModel.a()
        viewModel.b()

    }
}