package com.test.tiket.ardanil

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.test.tiket.ardanil.adapter.UserAdapter
import com.test.tiket.ardanil.model.ItemsItem
import com.test.tiket.ardanil.viewmodel.MainActivityVM
import com.test.tiket.ardanil.viewmodelfactory.MainActivityVMF
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var viewModel: MainActivityVM? = null
    private var page = 1
    private var totalData = 0
    private var isLoadMore = true
    private var userAdapter: UserAdapter? = null
    private var users: ArrayList<ItemsItem?>? = null
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModelFactory = MainActivityVMF()
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get<MainActivityVM>(MainActivityVM::class.java)
        observeLiveData()

        this.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                page = 1
                isLoadMore = true
                if (users != null && users!!.size > 0){
                    users?.clear()
                    userAdapter?.notifyDataSetChanged()
                }
                viewModel!!.fetchSearchUsers("1", s.toString())
            }
        })

        val linearLayoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL, false
        )
        rvItem.layoutManager = linearLayoutManager
        rvItem.setOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val total = linearLayoutManager.itemCount
                val lastVisibleItemCount = linearLayoutManager.findLastVisibleItemPosition()
                Log.i("isloadmore", isLoadMore.toString())
                Log.i("isLoading", isLoading.toString())
                if (isLoadMore && !isLoading) {
                    if (total > 0 && total - 1 == lastVisibleItemCount) {
                        page++
                        viewModel!!.fetchSearchUsers(page.toString(), etSearch.text.toString())
                    }
                }
            }
        })
    }

    private fun observeLiveData() {
        viewModel?.isLoading()?.observe(this, Observer<Boolean> {
            if (it != null) {
                isLoading = it
                this.progressBar.visibility = if (it) View.VISIBLE else View.GONE
            }
        })

        viewModel?.isError()?.observe(this, Observer {
            if (it != null) {
                when (it) {
                    401 -> {
                        showSnackbar("Oops..! You are unauthorized to access this content")
                    }
                    403 -> {
                        showSnackbar("Oops..! API rate limit exceeded, Try Again Later")
                    }
                    else -> {
                        showSnackbar("Oops..! Something Went Wrong")
                    }
                }
            }
        })

        viewModel?.getSearchUsers()?.observe(this, Observer {
            if (it != null) {
                if (page == 1) {
                    totalData = it.items?.size!!
                } else {
                    totalData += it.items?.size!!
                }

                isLoadMore = if (it.total_count != null){
                    it.total_count > totalData
                }else{
                    false
                }

                if (page == 1) {
                    rvItem.isNestedScrollingEnabled = true
                    userAdapter = UserAdapter()
                    users = it.items as ArrayList<ItemsItem?>?
                    userAdapter?.setItem(users)
                    rvItem.adapter = userAdapter
                } else {
                    for (item in it.items) {
                        users?.add(item)
                        userAdapter?.notifyDataSetChanged()
                    }
                }
                if (totalData > 0) {
                    rvItem.visibility = View.VISIBLE
                    llEmptyState.visibility = View.GONE
                } else {
                    rvItem.visibility = View.GONE
                    llEmptyState.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun showSnackbar(s: String) {
        Snackbar.make(this.linearLayout, s, Snackbar.LENGTH_LONG).show()
    }
}