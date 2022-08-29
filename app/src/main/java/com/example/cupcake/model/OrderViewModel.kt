package com.example.cupcake.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

// criando uma constante somente como leitura
private const val PRICE_PER_CUPCAKE = 2.00
private const val PRICE_FOR_SAME_DAY_PICKUP = 3.00

class OrderViewModel : ViewModel() {

    // propriedades que ser[a observáveis e a IU
    // poderá ser atualizada quando os dados de origem do modelo de visualização mudarem

    private val _quantity = MutableLiveData<Int>()
    val quantity: LiveData<Int> = _quantity

    private val _flavor = MutableLiveData<String>()
    val flavor: LiveData<String> = _flavor

    // Possible date options
    val dateOptions: List<String> = getPickupOptions()

    private val _date = MutableLiveData<String>()
    val date: LiveData<String> = _date

    private val _price = MutableLiveData<Double>()
    val price: LiveData<String> = Transformations.map(_price) {
        NumberFormat.getCurrencyInstance().format(it)
    }

    //inicializando propriedade quando uma instancia do orderviewmodel é criada
    init {
        resetOrder()
    }


    fun setQuantity(numberCupcakes: Int) {
        _quantity.value = numberCupcakes
        updatePrice()
    }

    fun setFlavor(desiredFlavor: String) {
        _flavor.value = desiredFlavor
    }

    fun setDate(pickupDate: String) {
        _date.value = pickupDate
        updatePrice()
    }

    //verificar se o sabor do pedido foi definido ou nã
    fun hasNoFlavorSet(): Boolean {
        return _flavor.value.isNullOrEmpty()
    }

    //criar e retornar a lista de datas de retirada.
    private fun getPickupOptions(): List<String> {
        val options = mutableListOf<String>()

        // string formato data
        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())

        //contem data e hora atuais
        val calendar = Calendar.getInstance()
        // Crie uma lista de datas começando com a data atual e as 3 datas seguintes
        repeat(4) {
            options.add(formatter.format(calendar.time))
            calendar.add(Calendar.DATE, 1)
        }
        return options
    }

    fun resetOrder() {
        _quantity.value = 0
        _flavor.value = ""
        _date.value = dateOptions[0]
        _price.value = 0.0
    }

    //método auxiliar para calcular o preço total
    private fun updatePrice() {
        _price.value = (quantity.value ?: 0) * PRICE_PER_CUPCAKE
        var calculatedPrice = (quantity.value ?: 0) * PRICE_PER_CUPCAKE
        // Se o usuário selecionou a primeira opção (hoje) para retirada, adiciona a sobretaxa
        if (dateOptions[0] == _date.value) {
            calculatedPrice += PRICE_FOR_SAME_DAY_PICKUP
        }
        _price.value = calculatedPrice
    }


}