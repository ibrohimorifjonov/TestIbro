package uz.test.view

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.test.api.di.viewModelsModule
import uz.test.api.dto.GeneralInfo
import uz.test.api.dto.PostAccessToken
import uz.test.api.dto.ResultAccessToken
import uz.test.api.network.Resource
import uz.test.api.repository.IBroRepository
import uz.test.api.utils.Event

class LandingPageViewModel constructor(private val repository: IBroRepository):ViewModel(){
    val postAccessToken=MutableLiveData<Event<Resource<ResultAccessToken>>>()
    val getGeneralData=MutableLiveData<Event<Resource<GeneralInfo>>>()

    fun postAccessToken(body:PostAccessToken){
        viewModelScope.launch {
            repository.postAccessToken(body).onEach {
                Log.d("worksdd","saveValue")
                postAccessToken.value= Event(it)
            }
                .launchIn(viewModelScope)
        }
    }

    fun getGeneralInfo(token:String){
        viewModelScope.launch {
            repository.getGeneralData(token).onEach { getGeneralData.value=Event(it) }
                .launchIn(viewModelScope)
        }
    }
}