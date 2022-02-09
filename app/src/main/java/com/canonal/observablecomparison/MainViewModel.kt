package com.canonal.observablecomparison

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    /***
     * LiveData is lifecycle aware component
     */
    private val _liveData = MutableLiveData("Hello Word")
    val liveData: LiveData<String>
        get() = _liveData

    /***
     * - StateFlow is a Flow and because of that
     *   it should be observed in a coroutine scope
     * - Use launchWhenStarted instead of launch
     * - Provides the power of flow operators for later operations
     *   like map and filter. Also they are easier to test with the
     *   background Coroutines capabilities. (Delay of coroutines are skipped during tests)
     * - StateFlow is hot flow. It will keep emitting values even there are no collectors
     * - Cold flows will not emit anything if there are no collectors
     * - If you show a snackbar with StateFlow on button click in our example,
     *   it will be showed once. It won't show the same value again. But if you rotate the device,
     *   it will be showed again. To handle that use SharedFlow
     */
    private val _stateFlow = MutableStateFlow("Hello Word")
    val stateFlow: StateFlow<String>
        get() = _stateFlow.asStateFlow()

    /**
     * - Also a hot flow
     * - SharedFlow is generally used for one time events like snackbar or toast
     * - It must be used inside coroutine scope
     */
    private val _sharedFlow = MutableSharedFlow<String>()
    val sharedFlow: SharedFlow<String>
        get() = _sharedFlow.asSharedFlow()

    fun triggerLiveData() {
        _liveData.value = "LiveData"
    }

    fun triggerStateFlow() {
        _stateFlow.value = "StateFlow"
    }

    /**
     * - Trigger it inside a coroutine scope
     * - You can use launch{}
     * - Flow returns to initial state for example after screen rotation changes
     *   and starts firing from start
     * - It can be used for example for a timer
     */
    fun triggerFlow(): Flow<String> = flow {
        repeat(5) {
            emit("Item $it")
            delay(1000L)
        }
    }

    fun triggerSharedFlow() {
        viewModelScope.launch {
            _sharedFlow.emit("SharedFlow")
        }
    }

}