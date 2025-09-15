package com.example.fakeamazon.features.home.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fakeamazon.R
import com.example.fakeamazon.features.home.component.ItemDisplay
import com.example.fakeamazon.model.Recommendation

@Composable
fun TopHomeSection(modifier: Modifier) {
    Row(modifier = modifier) {
        Column {
            Text(text = "More top\npicks for you", style = MaterialTheme.typography.titleLarge)

            val item = Recommendation(R.drawable.item_sandwich_bags, 0.0f)
            ItemDisplay(
                item = item,
                modifier = Modifier.size(140.dp, 200.dp),
            )
        }
    }
}
