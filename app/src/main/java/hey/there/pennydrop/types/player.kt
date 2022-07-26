package hey.there.pennydrop.types

import hey.there.pennydrop.Game.AI

data class Player(
    val playerName:String="",
    val isHuman:Boolean=true,
    val selectedAI:AI?=null
) {
    var pennies:Int=defaultPennyCount
    fun addPennies(count:Int=1){
        pennies+=count
    }

    var isRolling=false
    fun penniesLeft(subtractPenny:Boolean=false)=
        (pennies-(if(subtractPenny) 1 else 0))>0

    companion object{
        const val defaultPennyCount=10
    }
}