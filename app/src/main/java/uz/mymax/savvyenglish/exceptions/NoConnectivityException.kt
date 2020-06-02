package uz.mymax.savvyenglish.exceptions
import java.io.IOException

class NoConnectivityException : IOException(){
    override val message: String?
        get() = "Нет связи с сервером. \\n Возможно отключена сеть!"
}