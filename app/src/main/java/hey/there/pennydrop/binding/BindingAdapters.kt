package hey.there.pennydrop.binding

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("isHidden")
fun BindIsHidden(view:View,isInvisible:Boolean){
        view.visibility=if(isInvisible) View.INVISIBLE else View.VISIBLE
}