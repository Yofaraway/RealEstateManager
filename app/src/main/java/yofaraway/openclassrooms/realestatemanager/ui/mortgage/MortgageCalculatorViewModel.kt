package yofaraway.openclassrooms.realestatemanager.ui.mortgage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MortgageCalculatorViewModel : ViewModel() {

    var amount = MutableLiveData<String>()
    var interest = MutableLiveData<String>()
    var years = MutableLiveData(1)
    var currency = MutableLiveData<String>("Dollar")


}
