package com.example.gastosdiariosjetapckcompose.mis_ui_screen.home.components_home

import com.example.gastosdiariosjetapckcompose.R

data class TabBarItem(
    val title:String,
    val selectedIcon:Int,
    val unSelectedIcon:Int,
    val badgeAmount: Int? = null
)

val tabBarItems = listOf(
    TabBarItem(title = "Home", R.drawable.ic_home_filled,R.drawable.ic_home_outlined),
    TabBarItem(title = "Statistics", R.drawable.ic_barra_filled,R.drawable.ic_barra_outlined),
    TabBarItem(title = "Menu", R.drawable.ic_menu,R.drawable.ic_menu),
)