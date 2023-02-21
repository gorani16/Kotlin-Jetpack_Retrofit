package com.gorani.jetpack_retrofit.simple_coroutine

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * ViewModelScope 예제
 * ViewModel 에서 비동기로 작동되는 비즈니스 로직을 처리하는 상황
 *
 * 참고 : 수명 주기 인식 구성요소와 함께 Kotlin 코루틴 사용
 * https://developer.android.com/topic/libraries/architecture/coroutines?hl=ko
 */

class CoViewModel : ViewModel() {


    fun a() {
        CoroutineScope(Dispatchers.IO).launch {
            for (i in 1..10) {
                delay(1000)
                Log.d("CoViewModel A : ", i.toString())
            }
        }
    }
    // ㄴ> 두 번째 Activity 에서 빠져나왔을 때도 여전히 코루틴이 동작하여 작업을 수행한다. -> 불필요한 작업
    // ViewModel 이 사라질 때(필요없을 때) 루틴을 중지시켜주는 작업을 해줘야하는데, 이것을 매번 처리해주기 번거롭다.
    // 이런 문제를 해결하기 위해서 ViewModelScope 를 사용한다. (위 주석에서 참고 사이트 참고)

    fun b() {
        viewModelScope.launch {
            for (i in 1..10) {
                delay(1000)
                Log.d("CoViewModel B : ", i.toString())
            }
        }
    }
    // ㄴ> 두 번째 액티비티에 이동했을 때 동작하는것은 fun a, b 서로 같지만
    // b 의 경우 액티비티에서 나가는 순간 동작이 멈추게 된다.
    // 비동기작업을 하는 ViewModel 이 필요가 없어지면 루틴이 중지된다.

}