package com.example.fakeamazon.features.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.fakeamazon.R

@Composable
fun SearchScreenRoot(modifier: Modifier = Modifier) {
    val mainContentPadding = dimensionResource(R.dimen.main_content_padding_horizontal)

    Surface(modifier = modifier) {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            itemsIndexed(searchItems) { index, searchItem ->
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = mainContentPadding, vertical = 4.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            contentDescription = null,
                            painter = painterResource(R.drawable.ic_sharp_history_24)
                        )

                        Spacer(modifier = Modifier.width(mainContentPadding))

                        Text(
                            modifier = Modifier.fillMaxWidth(1.0f),
                            text = searchItem,
                        )
                    }

                    HorizontalDivider(
                        modifier = Modifier
                            .padding(horizontal = mainContentPadding / 2)
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}

val searchItems = listOf("board games", "popcorn", "almonds low sodium", "adidas sneakers")
