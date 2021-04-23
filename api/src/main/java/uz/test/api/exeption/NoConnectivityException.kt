package uz.test.api.exeption

import java.io.IOException

class NoConnectivityException : IOException(){
    override val message:String?
        get()="Нет связи с  сервером. Возможна отключена сеть"
}