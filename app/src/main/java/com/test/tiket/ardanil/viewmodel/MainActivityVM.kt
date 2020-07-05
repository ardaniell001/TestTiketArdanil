package com.test.tiket.ardanil.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.tiket.ardanil.configuration.ApiManager
import com.test.tiket.ardanil.model.SearchUsersResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivityVM : ViewModel() {

    private val loading = MutableLiveData<Boolean>()
    private val errorFetchUsers = MutableLiveData<Int?>()
    private val searchUsersResponse: MutableLiveData<SearchUsersResponse?> =
        MutableLiveData()

    fun isLoading(): LiveData<Boolean>? {
        return loading
    }

    fun isError(): LiveData<Int?>? {
        return errorFetchUsers
    }

    fun getSearchUsers(): LiveData<SearchUsersResponse?>? {
        return searchUsersResponse
    }

    private var getUsers: Call<SearchUsersResponse>? = null

    fun fetchSearchUsers(page: String?, query: String?) {
        loading.value = true
        getUsers = ApiManager().getService()?.getSearchUsers(page, query, "30")
        getUsers?.enqueue(object : Callback<SearchUsersResponse?> {
            override fun onResponse(
                call: Call<SearchUsersResponse?>,
                response: Response<SearchUsersResponse?>
            ) {
                loading.value = false
                if (response.code() == 200) {
                    errorFetchUsers.value = null
                    val responses: SearchUsersResponse? = response.body()
                    searchUsersResponse.setValue(responses)
                } else {
                    errorFetchUsers.setValue(response.code())
                }
            }

            override fun onFailure(
                call: Call<SearchUsersResponse?>,
                t: Throwable
            ) {
                loading.value = false
                errorFetchUsers.value = 500
            }
        })
    }
}
