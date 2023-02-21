package com.gorani.jetpack_retrofit.simple_coroutine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.gorani.jetpack_retrofit.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 간단한 코루틴 + ViewModelScope
 *
 * 코루틴 공식문서 한글로 읽기 (안드로이드 공식문서 내용을 한글로 번역한 블로그)
 * https://myungpyo.medium.com/reading-coroutine-official-guide-thoroughly-part-0-20176d431e9d
 *
 * 코루틴 공식 문서
 * https://developer.android.com/kotlin/coroutines?hl=ko
 *
 * suspend : 유예하다. 중지하다.
 *
 *
 */

class CoMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_co_main)

        findViewById<Button>(R.id.goToSecond).setOnClickListener {
            val intent = Intent(this, CoMainActivity2::class.java)
            startActivity(intent)
        }

        /**
        // CoroutineScope : 코루틴이 실행하는 범위
        Log.d("Coroutine_TEST", "START")
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("Coroutine_TEST", "CoroutineScope_START")
            a()
            b()
            Log.d("Coroutine_TEST", "CoroutineScope_END")
        }
        Log.d("Coroutine_TEST", "END")
         */

        /**
         * Log 가 뜨는 순서)
         * START > END > CO_START > AP1 > AP2 > BP1 > BP2 > CO_END
         */
    }

    suspend fun a() {
        delay(1000)
        Log.d("Coroutine_TEST", "A_Point_1")

        delay(1000)
        Log.d("Coroutine_TEST", "A_Point_2")
    }

    suspend fun b() {
        delay(1000)
        Log.d("Coroutine_TEST", "B_Point_1")

        delay(1000)
        Log.d("Coroutine_TEST", "B_Point_2")
    }

}