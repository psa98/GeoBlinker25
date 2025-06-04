package com.example.geoblinker.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import com.example.geoblinker.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SubscriptionViewModel: ViewModel() {
    val subscriptionOptions = listOf(
        Subscription(600, 1, "1 месяц", R.drawable.one_month),
        Subscription(1100, 3, "3 месяца", R.drawable.three_months),
        Subscription(1800, 6, "6 месяцев", R.drawable.six_months),
        Subscription(2800, 12, "12 месяцев", R.drawable.twelve_months)
    )

    private val _pickSubscription = MutableStateFlow(subscriptionOptions[0])
    val pickSubscription: StateFlow<Subscription> = _pickSubscription.asStateFlow()

    fun setPickSubscription(index: Int) {
        _pickSubscription.value = subscriptionOptions[index]
    }

    /**
     * Возвращает вердикт о выполении оплаты: true - успешно, false - ошибка
     * @return Boolean
     */
    fun paySubscription(): Boolean {
        // TODO: Добавить оплату
        return true
    }
}