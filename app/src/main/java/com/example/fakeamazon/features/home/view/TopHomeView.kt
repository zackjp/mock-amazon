package com.example.fakeamazon.features.home.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.example.fakeamazon.R
import com.example.fakeamazon.features.home.component.ItemDisplay
import com.example.fakeamazon.features.home.model.DisplayableItem

@Composable
fun TopHomeSection(modifier: Modifier) {
    Row(modifier = modifier) {
        val cardWidth = dimensionResource(R.dimen.top_home_card_width)
        val cardHeight = dimensionResource(R.dimen.top_home_card_height)
        val paddingMedium = dimensionResource(R.dimen.padding_medium)

        Card(modifier = Modifier.size(cardWidth, cardHeight)) {
            Column(modifier = Modifier.padding(paddingMedium)) {
                Text(text = "More top\npicks for you", style = MaterialTheme.typography.titleLarge)

                Spacer(Modifier.height(paddingMedium))

                val item = DisplayableItem(imageId = R.drawable.item_sandwich_bags)
                ItemDisplay(
                    item = item,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}
