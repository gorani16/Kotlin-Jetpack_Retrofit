package com.gorani.jetpack_retrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

/**
 * Retrofit
 * 우리는 데이터를 Server 또는 Local DB 로 부터 받아온다.
 * - Server 로부터 데이터를 받아올 때는 Retrofit 을 사용하여 데이터를 받아온다.
 * - Local DB 로부터 데이터를 받아올 때는 ROOM 을 사용하여 데이터를 받아온다.
 *
 * Retrofit 실습
 * - JSONPlaceholder : API 를 사용하여 데이터를 받아 Retrofit 실습할 수 있음.
 * https://jsonplaceholder.typicode.com/
 * https://jsonplaceholder.typicode.com/posts
 *
 * Coroutine (코루틴)
 * - 비동기적으로 실해오디는 코드를 간소화하기 위해 Android 에서 사용할 수 있는 동시 실행 설계 패턴이다.
 * - Android 에서 코루틴은 기본 스레드를 차단하여 앱이 응답하지 않게 만들 수도 있는 장기 실행 작업을 관리하는데 도움이 된다.
 * 참고 : https://developer.android.com/kotlin/coroutines?hl=ko
 *
 * ViewModel 과 Coroutine 을 접목해서 사용해야하는 이유)
 * 예시 상황 : API_ 1, 2, 3, 4 를 사용.
 * 문제 상황 : 앱을 실행할 때마다 데이터의 순서가 달라지는 예외 발생
 * ㄴ> Run1 : API_ 2, 3, 4, 1
 * ㄴ> Run1 : API_ 3, 2, 1, 4
 * 이처럼 API 를 실행시키는 코드의 배치는 순번대로 했지만 실행 시 로그를 확인해보면 실행할 때마다 순서가 달라진다.
 * 그 이유는 비동기적으로 실행되기 때문이다.
 * 즉, API 중 먼저 완료가 되는 것 순서대로 실행되기 때문에 앱이 실행할 때마다 순서가 달라진다.
 *
 * 그렇다면 순서대로 실행시키려면 동기적으로 실행시켜야할까?
 * 만약 동기적으로 실행시킨다고 가정해보자.
 * 먼저 동기적 실행이라함은 순차적으로 실행되는 것이라고 생각하면된다.
 * 1 에서 4 까지 작업이 있다고 했을 때 순차적으로 1번 작업이 완료되면
 * 그 다음 작업인 2번 작업을 수행한다. 이 과정을 4번 작업까지 반복한다.
 * 여기서 포인트는 이전의 작업이 완료되야만 다음 작업이 실행된다는 것이다.
 * 만약에 작업들중 처리가 오래걸리는 작업이 있다면 그 작업이 완료되기전까지 화면이 멈춰있을 것이다.
 * 더 심하면 앱이 중지될 수도 있다.
 * 이것은 안드로이드에서 치명적인 문제로 규정짓고있다. (UI 정지)
 * 이런 문제를 피하기위해 작업시간이 오래 소요되는 작업을 실행시킬때는 비동기적으로 설계하는것이 안정성이 좋다.
 *
 * 그렇다면 비동기적으로 순서대로 실행시켜야하려면 어떻게 해야할까?
 * 이런 경우를 예를 들어보면 아래와 같을 수 있다.
 * 1. User 정보를 가져온다. (userId)
 * 2. userId 를 기반으로 닉네임이 포함된 데이터를 가져오고,
 * 3. 닉네임값을 기반으로 User 의 댓글을 가져와서
 * 4. User 의 댓글을 기반으로 대댓글을 가져온다.
 * 위와 같은 로직을 구현하려면 순차적으로 실행이 되어야할 것이다.
 * 순차적으로 실행시키기 위한 방법 중 제일 간단한 방법은 제일 첫 번째 작업의 onResponse 콜백 내부에
 * 다음 작업의 코드를 배치하는것이다. 이것을 4단계 작업까지 전부 반복한다. 아래 코드를 참고.
 * 대략적인 구조는 아래와 같다.
 * => 1번 작업 성공{ 2번 작업 성공 { 3번 작업 성공 { 4번 작업 성공 { } } } }
 * 이렇게 배치하면 결과값은 기대한대로 1번에서 4번까지 순차적으로 실행이 된다.
 * 하지만 콜백이 덕지덕지 붙어서 코드가 지저분해져서 가독성이 극도로 떨어진다.
 * 이때 Coroutine 을 사용하면 가독성과 효율성을 챙기면서 해결할 수 있다.
 *
 */

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val api = RetrofitInstance.getInstance().create(MyApi::class.java)
        // 1번 작업
        api.getPost1().enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                Log.d("API_1", response.body().toString())

                // 2번 작업
                api.getPostNumber(2).enqueue(object : Callback<Post> {
                    override fun onResponse(call: Call<Post>, response: Response<Post>) {
                        Log.d("API_2", response.body().toString())

                        // 3번 작업
                        api.getPostNumber(3).enqueue(object : Callback<Post> {
                            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                                Log.d("API_3", response.body().toString())

                                // 4번 작업
                                api.getPostNumber(4).enqueue(object : Callback<Post> {
                                    override fun onResponse(call: Call<Post>, response: Response<Post>) {
                                        Log.d("API_4", response.body().toString())
                                    }

                                    override fun onFailure(call: Call<Post>, t: Throwable) {
                                        Log.d("API_4", "Failed !")
                                    }

                                })
                            }

                            override fun onFailure(call: Call<Post>, t: Throwable) {
                                Log.d("API_3", "Failed !")
                            }

                        })
                    }

                    override fun onFailure(call: Call<Post>, t: Throwable) {
                        Log.d("API_2", "Failed !")
                    }

                })
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                Log.d("API_1", "Failed !")
            }

        })

    }
}