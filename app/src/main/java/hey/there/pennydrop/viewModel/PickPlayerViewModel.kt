package hey.there.pennydrop.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hey.there.pennydrop.types.NewPlayer

class PickPlayerViewModel:ViewModel() {
        val players=MutableLiveData<List<NewPlayer>>().apply {
                this.value= (1..6).map{
                        NewPlayer(
                                canBeRemoved = it>2,
                                canBeToggled = it>1
                        )
                }
        }
}