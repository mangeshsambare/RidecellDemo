package com.ridecell.ridecelldemo.network

import android.text.TextUtils
import android.util.Log
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

abstract class BaseClient protected constructor() { // to achieve singleton instance & create protected version

    protected var retrofit: Retrofit
    var networkRepository: NetworkRepository
        protected set // the setter is protected and has the default implementation

    init {
        networkRepository = NetworkRepository.instance

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)    //we can set authenticator here & manage auto authentication also redirection
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .sslSocketFactory(getSSLSocketFactory(), getTrustManager())
            .hostnameVerifier(getHostNameVerifier())
//            .cookieJar(getCookieJar())
            .followRedirects(true)
            .build()


        val gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setLenient()
            .create()

        retrofit = Retrofit.Builder()
            .baseUrl(networkRepository.baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .callbackExecutor(Executors.newSingleThreadExecutor())
            .client(okHttpClient)
            .build()
    }


    protected fun <T> makeRequest(request: Observable<Response<T>>,
                                  callBack: ApiCallback<T>)
    {
        callBack.requestSent()
        request
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Response<T>> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                    callBack.subscribeRequest(d!!)
                }

                override fun onNext(response: Response<T>) {
                    when (response.code()) {
                        200, 204, 404 -> {
                            val responseBody = response.body()
                            if (responseBody != null) {
                                callBack.success(responseBody)
                            } else {
                                callBack?.error(getError(response))
                            }
                        }
                        401 -> {
                            Log.d("BaseClient", "makeRequest() : Unauthorize token")
                            //TODO have to change this, update request parameter here, this one is temporary solution

                        }
                        else -> {
                            callBack?.error(getError(response))
                        }
                    }
                }

                override fun onError(e: Throwable) {
                    callBack.error(NetworkError(message = e.message))
                }

            })


    }

    private fun getError(response: Response<*>): NetworkError {
        return when (response.code()) {
            else -> {
                var nwError: NetworkError
                val errorConverter =
                    retrofit.responseBodyConverter<NetworkError>(
                        NetworkError::class.java, arrayOfNulls(0)
                    )
                try {
                    val responseBody = response.errorBody()
                    if (responseBody != null)
                    {
                        nwError = errorConverter.convert(responseBody)!!
                        if (nwError.code == 0){
                            val message = response.message()
                            if(nwError.message == "Something might be wrong"){
                                nwError.message = message
                            }
                            nwError.code = response.code()
                            nwError.error = response.message()
                        }
                        else if (TextUtils.isEmpty(nwError.error)) {
                            if (nwError.code == 1213) {
                                nwError.error = "Error"
                                nwError.message = "Error Message"
                            }
                        }
                    } else {
                        nwError = NetworkError(response.code(), error = response.message(), message = response.message())
                    }
                    nwError
                } catch (e: IOException) {
                    e.printStackTrace()
                    nwError = NetworkError(message = e.message)
                    nwError
                }
            }
        }
    }

    private fun getSSLSocketFactory(): SSLSocketFactory {
        val trustAllCerts = arrayOf<TrustManager>(
            getTrustManager()
        )
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, SecureRandom())
        // Create an ssl socket factory with our all-trusting manager

        // Create an ssl socket factory with our all-trusting manager
        return sslContext.socketFactory
    }

    private fun getTrustManager(): X509TrustManager {
        return object : X509TrustManager {
            @Throws(CertificateException::class)
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
                Log.d("BaseClient", "chain : , $chain")
            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
                Log.d("BaseClient","chain : $chain")
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        }
    }

    /*host name verifier*/
    private fun getHostNameVerifier() : HostnameVerifier {
        return HostnameVerifier { s, sslSession -> true }
    }
}