package com.example.dexify.feature.pokedex.pokelist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.dexify.core.designsystem.theme.TypeElectric
import com.example.dexify.core.designsystem.theme.TypeFire
import com.example.dexify.core.designsystem.theme.TypeGrass
import com.example.dexify.core.designsystem.theme.TypePsychic
import com.example.dexify.core.designsystem.theme.TypeRock
import com.example.dexify.core.designsystem.theme.TypeWater
import com.example.dexify.core.designsystem.theme.FavoritePink
import com.example.dexify.feature.pokedex.model.PokedexFilterState

private data class GenerationOption(val id: Int, val label: String)
private data class TypeOption(val id: String, val label: String, val color: Color)
private data class HabitatOption(val id: String, val label: String)

private val generations = listOf(
    GenerationOption(1, "Gen I"),
    GenerationOption(2, "Gen II"),
    GenerationOption(3, "Gen III"),
    GenerationOption(4, "Gen IV"),
    GenerationOption(5, "Gen V+")
)

private val elementalTypes = listOf(
    TypeOption("grass", "Grass", TypeGrass),
    TypeOption("fire", "Fire", TypeFire),
    TypeOption("water", "Water", TypeWater),
    TypeOption("electric", "Electric", TypeElectric),
    TypeOption("psychic", "Psychic", TypePsychic),
    TypeOption("rock", "Rock", TypeRock)
)

private val habitats = listOf(
    HabitatOption("forest", "Forest"),
    HabitatOption("mountain", "Mountain"),
    HabitatOption("sea", "Ocean"),
    HabitatOption("urban", "Town")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    sheetState: SheetState,
    currentFilter: PokedexFilterState,
    onApply: (PokedexFilterState) -> Unit,
    onDismiss: () -> Unit
) {
    var searchQuery by rememberSaveable { mutableStateOf(currentFilter.query) }
    var selectedGen by rememberSaveable {
        mutableStateOf<Int?>(currentFilter.generationId)
    }
    var selectedType by rememberSaveable { mutableStateOf(currentFilter.typeId ?: "") }
    var selectedHabitat by rememberSaveable { mutableStateOf(currentFilter.habitatId ?: "") }
    var showFavoritesOnly by rememberSaveable { mutableStateOf(currentFilter.showFavoritesOnly) }

    fun resetLocal() {
        searchQuery = ""
        selectedGen = null
        selectedType = ""
        selectedHabitat = ""
        showFavoritesOnly = false
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 28.dp)
                .navigationBarsPadding()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Filters",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                val hasActiveFilter = searchQuery.isNotBlank() ||
                        selectedGen != null ||
                        selectedType.isNotBlank() ||
                        selectedHabitat.isNotBlank() ||
                        showFavoritesOnly

                val resetColor by androidx.compose.animation.animateColorAsState(
                    targetValue = if (hasActiveFilter)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f),
                    animationSpec = androidx.compose.animation.core.tween(durationMillis = 300),
                    label = "resetColor"
                )

                TextButton(
                    onClick = { resetLocal() },
                    enabled = hasActiveFilter
                ) {
                    Text(
                        text = "Reset",
                        style = MaterialTheme.typography.labelLarge,
                        color = resetColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            SectionLabel("FAVORITES")
            Spacer(modifier = Modifier.height(8.dp))
            FilterChip(
                selected = showFavoritesOnly,
                onClick = { showFavoritesOnly = !showFavoritesOnly },
                leadingIcon = {
                    Icon(
                        imageVector = if (showFavoritesOnly) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = if (showFavoritesOnly) Color.White else FavoritePink
                    )
                },
                label = {
                    Text(
                        text = "Show Only Favorites",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = if (showFavoritesOnly) FontWeight.Bold else FontWeight.Medium
                    )
                },
                shape = RoundedCornerShape(20.dp),
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = FavoritePink.copy(alpha = 0.12f),
                    labelColor = FavoritePink,
                    selectedContainerColor = FavoritePink,
                    selectedLabelColor = Color.White
                ),
                border = FilterChipDefaults.filterChipBorder(
                    borderColor = FavoritePink.copy(alpha = 0.3f),
                    selectedBorderColor = FavoritePink,
                    enabled = true,
                    selected = showFavoritesOnly
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            SectionLabel("SEARCH BY NAME")
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "E.g. Gyarados",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Mic,
                        contentDescription = "Voice search",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                shape = RoundedCornerShape(14.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            SectionLabel("SELECT GENERATION")
            Spacer(modifier = Modifier.height(8.dp))
            GenerationChips(
                selected = selectedGen,
                onSelect = { id -> selectedGen = if (selectedGen == id) null else id }
            )

            Spacer(modifier = Modifier.height(24.dp))

            SectionLabel("ELEMENTAL TYPE")
            Spacer(modifier = Modifier.height(8.dp))
            TypeChips(
                selected = selectedType,
                onSelect = { id -> selectedType = if (selectedType == id) "" else id }
            )

            Spacer(modifier = Modifier.height(24.dp))

            SectionLabel("HABITAT")
            Spacer(modifier = Modifier.height(8.dp))
            HabitatChips(
                selected = selectedHabitat,
                onSelect = { id -> selectedHabitat = if (selectedHabitat == id) "" else id }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    onApply(
                        PokedexFilterState(
                            query = searchQuery.trim(),
                            generationId = selectedGen,
                            typeId = selectedType.ifBlank { null },
                            habitatId = selectedHabitat.ifBlank { null },
                            showFavoritesOnly = showFavoritesOnly
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Apply Filters",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        letterSpacing = MaterialTheme.typography.labelMedium.letterSpacing
    )
}

@Composable
private fun GenerationChips(selected: Int?, onSelect: (Int) -> Unit) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        generations.forEach { gen ->
            val isSelected = selected == gen.id
            FilterChip(
                selected = isSelected,
                onClick = { onSelect(gen.id) },
                label = {
                    Text(
                        text = gen.label,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                },
                shape = RoundedCornerShape(20.dp),
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                ),
                border = FilterChipDefaults.filterChipBorder(
                    borderColor = Color.Transparent,
                    selectedBorderColor = Color.Transparent,
                    enabled = true,
                    selected = isSelected
                )
            )
        }
    }
}

@Composable
private fun TypeChips(selected: String, onSelect: (String) -> Unit) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        elementalTypes.forEach { type ->
            val isSelected = selected == type.id
            FilterChip(
                selected = isSelected,
                onClick = { onSelect(type.id) },
                leadingIcon = {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(type.color)
                    )
                },
                label = {
                    Text(
                        text = type.label,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                },
                shape = RoundedCornerShape(20.dp),
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = type.color.copy(alpha = 0.12f),
                    labelColor = type.color,
                    selectedContainerColor = type.color,
                    selectedLabelColor = Color.White
                ),
                border = FilterChipDefaults.filterChipBorder(
                    borderColor = type.color.copy(alpha = 0.3f),
                    selectedBorderColor = type.color,
                    enabled = true,
                    selected = isSelected
                )
            )
        }
    }
}

@Composable
private fun HabitatChips(selected: String, onSelect: (String) -> Unit) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        habitats.forEach { habitat ->
            val isSelected = selected == habitat.id
            FilterChip(
                selected = isSelected,
                onClick = { onSelect(habitat.id) },
                label = {
                    Text(
                        text = habitat.label,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                },
                shape = RoundedCornerShape(20.dp),
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                ),
                border = FilterChipDefaults.filterChipBorder(
                    borderColor = Color.Transparent,
                    selectedBorderColor = Color.Transparent,
                    enabled = true,
                    selected = isSelected
                )
            )
        }
    }
}
