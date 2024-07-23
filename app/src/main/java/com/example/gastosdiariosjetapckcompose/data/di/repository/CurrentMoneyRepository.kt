package com.example.gastosdiariosjetapckcompose.data.di.repository

import com.example.gastosdiariosjetapckcompose.data.di.dao.CurrentMoneyDao
import com.example.gastosdiariosjetapckcompose.data.di.entity.CurrentMoneyEntity
import com.example.gastosdiariosjetapckcompose.domain.model.CurrentMoneyModel
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class CurrentMoneyRepository @Inject constructor(private val currentMoneyDao: CurrentMoneyDao) {
    suspend fun getMoney(): CurrentMoneyModel {
        val get = currentMoneyDao.getMoney()
        return if (get != null) CurrentMoneyModel(get.id, get.money, get.isChecked)
        else CurrentMoneyModel(id = 0, money = 0.00, isChecked = true)
    }

    suspend fun insertCurrentMoney(currentMoneyModel: CurrentMoneyModel) {
        currentMoneyDao.insertMoney(currentMoneyModel.toData())
    }

    suspend fun updateCurrentMoney(money: CurrentMoneyModel) {
        currentMoneyDao.updateMoney(money.toData())
    }

    suspend fun deleteCurrentMoneyRepository(item: CurrentMoneyModel) {
        currentMoneyDao.deleteMoney(item.toData())
    }

    suspend fun isDatabaseMoneyEmpty(): Boolean {
       return currentMoneyDao.getMoney() == null
    }

    suspend fun clearAllCurrentMoneyRepository(){
        currentMoneyDao.deleteAllMoney()
    }
}

fun CurrentMoneyModel.toData(): CurrentMoneyEntity {
    return CurrentMoneyEntity(this.id, this.money, this.isChecked)
}
