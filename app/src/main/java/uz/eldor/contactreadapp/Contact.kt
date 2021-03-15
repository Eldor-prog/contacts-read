package uz.eldor.contactreadapp

data class Contact(
    val name :String?,
    val photo:String?,
    var id:String?,
    val numbers:MutableList<Number> = mutableListOf()
)
data class Number(
    val number:String?
)