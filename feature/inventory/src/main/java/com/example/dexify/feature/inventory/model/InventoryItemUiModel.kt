package com.example.dexify.feature.inventory.model

data class InventoryItemUiModel(
    val id: Int,
    val name: String,
    val imageUrl: String?,
    val category: InventoryCategory,
    val description: String,
    val cost: Int,
    val effectRate: String
)

enum class InventoryCategory(val label: String, val badgeLabel: String) {
    ITEMS("Items", "ITEM"),
    BERRIES("Berries", "BERRY"),
    MEDICINE("Medicine", "RECOVERY")
}
