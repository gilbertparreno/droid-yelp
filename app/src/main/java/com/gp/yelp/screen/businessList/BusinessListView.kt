package com.gp.yelp.screen.businessList

interface BusinessListView {
    fun showProgress()
    fun hideProgress()
    fun clearList()
    fun showError()
    fun hideError()
}