package com.gp.yelp.screen.main

interface MainView {
    fun hideOptionMenus()
    fun showMenus(vararg optionButtons: MainActivity.OptionButton)
    fun showHeaderLogo()
    fun setHeaderText(title: String)
    fun hideToolbar()
    fun showToolbar()
}