package com.example.geoblinker.ui.faq

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geoblinker.network.Api
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FaqViewModel : ViewModel() {
    
    private val _faqContent = MutableStateFlow<String?>(null)
    val faqContent: StateFlow<String?> = _faqContent.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    fun loadFaqContent() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            try {
                Log.d("FaqViewModel", "🔥 Отправляем запрос: GET /data?data.lang_vls=1")
                val response = Api.retrofitService.getData("1")
                
                Log.d("FaqViewModel", "📋 Ответ API: code=${response.code}")
                Log.d("FaqViewModel", "📋 Полный ответ: $response")
                
                if (response.code == "200") {
                    Log.d("FaqViewModel", "📋 response.data: ${response.data}")
                    Log.d("FaqViewModel", "📋 response.data.data: ${response.data.data}")
                    
                    val langValues = response.data.data?.langValues
                    if (langValues != null) {
                        Log.d("FaqViewModel", "📋 Найдено langValues, размер: ${langValues.size}")
                        Log.d("FaqViewModel", "📋 Все ключи: ${langValues.keys}")
                        
                        val faqEntry = langValues["geo_blinker_faq"]
                        Log.d("FaqViewModel", "📋 Поиск 'geo_blinker_faq': $faqEntry")
                        
                        if (faqEntry != null && !faqEntry.value.isNullOrBlank()) {
                            Log.d("FaqViewModel", "✅ FAQ найдено: ${faqEntry.value}")
                            _faqContent.value = faqEntry.value
                        } else {
                            Log.w("FaqViewModel", "❌ FAQ не найдено или пустое")
                            _errorMessage.value = "FAQ не найдено в ответе API"
                        }
                    } else {
                        Log.w("FaqViewModel", "❌ langValues = null")
                        _errorMessage.value = "langValues отсутствует в ответе"
                    }
                } else {
                    Log.e("FaqViewModel", "❌ API вернул код: ${response.code}")
                    _errorMessage.value = "Ошибка API: ${response.code}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка соединения: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
