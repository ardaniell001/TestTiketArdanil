package com.test.tiket.ardanil.model

import com.squareup.moshi.Json

data class SearchUsersResponse(

	@Json(name="total_count")
	val total_count: Int? = null,

	@Json(name="incomplete_results")
	val incomplete_results: Boolean? = null,

	@Json(name="items")
	val items: MutableList<ItemsItem?>? = null
)