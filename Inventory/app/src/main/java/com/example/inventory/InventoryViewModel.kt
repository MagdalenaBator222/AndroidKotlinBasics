package com.example.inventory

import androidx.lifecycle.*
import com.example.inventory.data.Item
import com.example.inventory.data.ItemDao
import kotlinx.coroutines.launch

class InventoryViewModel(private val itemDao: ItemDao) : ViewModel() {
    val allItems: LiveData<List<Item>> = itemDao.getItems().asLiveData()

    // inserts a new item into the database
    private fun insertItem(item: Item) {
        // coroutine for interacting with the database off the main thread
        viewModelScope.launch {
            itemDao.insert(item)
        }
    }

    // creates a new Item object from the user's input
    private fun getNewItemEntry(itemName: String, itemPrice: String, itemCount: String): Item {
        return Item(
            itemName = itemName,
            itemPrice = itemPrice.toDouble(),
            quantityInStock = itemCount.toInt()
        )
    }

    // puts everything into practice :>
    fun addNewItem(itemName: String, itemPrice: String, itemCount: String) {
        val newItem = getNewItemEntry(itemName, itemPrice, itemCount)
        insertItem(newItem)
    }

    // verifies if all the text fields are filled before adding an item to the database
    fun isEntryValid(itemName: String, itemPrice: String, itemCount: String): Boolean {
        if(itemName.isBlank() || itemPrice.isBlank() || itemCount.isBlank()){
            return false
        }
        return true
    }

    fun retrieveItem(id: Int) : LiveData<Item> {
        return itemDao.getItem(id).asLiveData()
    }

    private fun updateItem(item: Item) {
        viewModelScope.launch {
            itemDao.update(item)
        }
    }

    fun sellItem(item: Item) {
        if(item.quantityInStock > 0) {
            // decrease the quantity by 1
            val newItem = item.copy(quantityInStock = item.quantityInStock - 1)
            updateItem(newItem)
        }
    }

    fun isStockAvailable(item: Item) : Boolean {
        if(item.quantityInStock > 0){
            return true
        }
        return false
    }

    fun deleteItem(item: Item) {
        viewModelScope.launch {
            itemDao.delete(item)
        }
    }

    private fun getUpdatedItemEntry(itemId: Int,
                                    itemName: String,
                                    itemPrice: String,
                                    itemCount: String)
    : Item {
        return Item(id = itemId,
        itemName = itemName,
        itemPrice = itemPrice.toDouble(),
        quantityInStock = itemCount.toInt())
    }

    fun updateItem(itemId: Int,
                   itemName: String,
                   itemPrice: String,
                   itemCount: String) {
        val updatedItem = getUpdatedItemEntry(itemId, itemName, itemPrice, itemCount)
        updateItem(updatedItem)
    }
}

// instantiates the InventoryViewModel instance
// mostly boilerplate code, can be reused in the future :>
class InventoryViewModelFactory(private val itemDao: ItemDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // checks if the modelClass is the same as InventoryViewModel class
        if(modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InventoryViewModel(itemDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

