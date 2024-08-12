package com.example.gastosdiariosjetapckcompose.eventshandler

sealed class EventsHandler {
    data class GetCurrentMoney(val money: String) : EventsHandler()
    data class SetCurrentMoney(val money: String) : EventsHandler()
}