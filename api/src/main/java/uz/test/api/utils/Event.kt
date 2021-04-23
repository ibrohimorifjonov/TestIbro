package uz.test.api.utils

open class Event <out T>(private val content:T){
    var hasBeenHandled=false
        private set
    fun getContentIfNotHandled():T?{
        return if(hasBeenHandled){
            null
        }else{
            hasBeenHandled=true
            content
        }
    }
    fun handleIfNotHandled():Boolean{
        val originalState=hasBeenHandled
        hasBeenHandled=true

        return originalState
    }
    fun peekContent():T=content
}