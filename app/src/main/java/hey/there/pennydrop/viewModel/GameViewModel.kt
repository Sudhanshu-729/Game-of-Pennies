package hey.there.pennydrop.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hey.there.pennydrop.Game.GameHandler
import hey.there.pennydrop.Game.TurnEnd
import hey.there.pennydrop.Game.TurnResult
import hey.there.pennydrop.types.Player
import hey.there.pennydrop.types.Slots
import hey.there.pennydrop.types.clear
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameViewModel:ViewModel() {
    private var players:List<Player> = emptyList()
    val slots=MutableLiveData(
        (1..6).map {
            slotNum -> Slots(slotNum, slotNum!=6)
        }
    )
    val currentPlayer=MutableLiveData<Player?>()
    val canRoll=MutableLiveData(false)
    val canPass=MutableLiveData(false)
    val currentTurnText=MutableLiveData("")
    val currentStandingText=MutableLiveData("")
    var clearText:Boolean = true


    fun startGame(playersForNewGame:List<Player>){
        this.players=playersForNewGame

        this.currentPlayer.value=
            this.players.firstOrNull().apply {
                this?.isRolling=true
            }
        this.canRoll.value=true
        canPass.value=false

        slots.value?.clear()
        slots.notifyChange()
        currentTurnText.value="The game has begun !\n"
        currentStandingText.value= generateCurrentStandings(this.players)
    }
    fun roll(){
        slots.value?.let {currentSlots ->
            // comparing against true saves us a null check
            val currentPlayer=players.firstOrNull{it.isRolling}
            if(currentPlayer != null && canRoll.value== true){
                updateFromGameHandler(
                    GameHandler.roll(players, currentPlayer,currentSlots)
                )
            }
        }
    }
    fun pass(){
        val currentPlayer=players.firstOrNull{it.isRolling}
        if(currentPlayer!= null && canPass.value==true){
            updateFromGameHandler(GameHandler.pass(players,currentPlayer))
        }
    }

    private fun updateFromGameHandler(result: TurnResult) {
        if(result.currentPlayer != null){
            currentPlayer.value?.addPennies(result.coinChangeCount ?:0)
            currentPlayer.value=result.currentPlayer
            this.players.forEach {player ->
                player.isRolling=result.currentPlayer==player
            }
        }
        if(result.lastRoll!=null){
            slots.value?.let { currentSlots ->
                updateSlots(result,currentSlots,result.lastRoll)
            }
        }
        currentTurnText.value=generateTurnText(result)
        currentStandingText.value=generateCurrentStandings(this.players)


        canPass.value=result.canPass
        canRoll.value=result.canRoll

        if(!result.isGameOver && result.currentPlayer?.isHuman==false){
            canRoll.value=false
            canPass.value=false
            playAITurn()
        }

    }

    private fun playAITurn() {
        viewModelScope.launch {
            delay(3000)
            slots.value?.let { currentSlots ->
                val currentPlayer=players.firstOrNull { it.isRolling }
                if(currentPlayer != null && !currentPlayer.isHuman){
                    GameHandler.playAITurn(
                        players,
                        currentPlayer,
                        currentSlots,
                        canPass.value==true
                    )?.let{ result ->
                        updateFromGameHandler(result)
                    }
                }
            }
        }
    }

    private fun generateTurnText(result: TurnResult): String {
        if(clearText)currentTurnText.value=""
        clearText=result.turnEnd !=null

        val currentText=currentTurnText.value ?:""
        val currentPlayerName=result.currentPlayer?.playerName ?:"???"
        return when{
            result.isGameOver ->
                """
                | Game Over !
                |$currentPlayerName is the winner!
                |
                |${generateCurrentStandings(this.players, "Final Scores:\n")}
                """.trimMargin()
            result.turnEnd==TurnEnd.Bust -> "${result.previousPlayer?.playerName} has rolled ${result.lastRoll}. Busted!!"

            result.turnEnd==TurnEnd.Pass -> "${result.previousPlayer?.playerName} has passed"
            result.lastRoll !=null -> "$currentText\n$currentPlayerName rolled a ${result.lastRoll}"
            else -> ""
        }
    }

    private fun updateSlots(result: TurnResult, currentSlots: List<Slots>, lastRoll: Int) {
        if(result.clearSlots)
            currentSlots.clear()

        currentSlots.firstOrNull{it.lastRolled}?.apply { lastRolled=false }
        currentSlots.getOrNull(lastRoll-1)?.also { slot ->
            if(!result.clearSlots && slot.canBeFilled)slot.isFilled=true

            slot.lastRolled=true
        }
        slots.notifyChange()
    }


    private fun generateCurrentStandings(
        players:List<Player>,
        headerText:String="Current Standings: "
    )= players.sortedBy { it.pennies }.joinToString(
        separator="\n",
        prefix="$headerText\n"
    ){
        "\t${it.playerName} - ${it.pennies} pennies"
    }

}

private fun <T> MutableLiveData<List<T>>.notifyChange(){
    this.value=this.value
}