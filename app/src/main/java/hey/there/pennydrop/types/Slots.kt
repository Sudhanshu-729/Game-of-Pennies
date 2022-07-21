package hey.there.pennydrop.types

data class Slots(
    val number:Int,
    val canBeFilled:Boolean=true,
    var isFilled:Boolean=false,
    var lastRolled:Boolean=false
)

fun List<Slots>.clear()=this.forEach { slot ->
    slot.isFilled=false
    slot.lastRolled=false
}

fun List<Slots>.fullSlots():Int =
    this.count{it.canBeFilled && it.isFilled}
