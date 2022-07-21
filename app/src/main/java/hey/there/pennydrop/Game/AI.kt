package hey.there.pennydrop.Game

import hey.there.pennydrop.types.Slots
import hey.there.pennydrop.types.fullSlots

data class AI(val name:String,
              val rollAgain:(slots:List<Slots>) -> Boolean

) {
    override fun toString()=name

    companion object{
        @JvmStatic
        val basicAI= listOf(
            AI("two_Face") {slots ->slots.fullSlots()<=2},
            AI("MissedMe?"){slots ->slots.fullSlots()<=1},
            AI("Riddler"){slots ->slots.fullSlots()<=3},
            AI("Skull Crusher"){slots ->slots.fullSlots()<=2},
            AI("Bond007"){slots ->slots.fullSlots()<=1},
            AI("Black Adam"){slots ->slots.fullSlots()<=3},
            AI("ToothLess"){slots ->slots.fullSlots()<=1},
            AI("Night Fury"){slots ->slots.fullSlots()<=2}
        )

    }
    }
