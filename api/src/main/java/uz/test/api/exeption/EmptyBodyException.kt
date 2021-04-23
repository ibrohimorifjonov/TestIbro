package uz.test.api.exeption

import java.lang.Exception

class EmptyBodyException: Exception(){
    override val message: String?
        get()="Empty body exception message"
}