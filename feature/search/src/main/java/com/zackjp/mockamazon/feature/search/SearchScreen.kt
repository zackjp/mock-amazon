package com.zackjp.mockamazon.feature.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zackjp.mockamazon.shared.R as SharedR

@Composable
fun SearchScreenRoot(
    modifier: Modifier = Modifier,
    onPerformSearch: (searchString: String) -> Unit = {},
    viewModel: SearchScreenViewModel = hiltViewModel(),
) {
    val searchItems by viewModel.searchItems.collectAsStateWithLifecycle()

    val mainContentPadding = dimensionResource(SharedR.dimen.main_content_padding_horizontal)

    Surface(modifier = modifier) {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            itemsIndexed(searchItems) { index, searchItem ->
                Column(
                    modifier = Modifier
                        .clickable(onClick = { onPerformSearch(searchItem) })
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = mainContentPadding, vertical = 4.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            contentDescription = null,
                            painter = painterResource(SharedR.drawable.ic_sharp_history_24)
                        )

                        Spacer(modifier = Modifier.width(mainContentPadding))

                        Text(
                            modifier = Modifier.weight(1.0f),
                            text = searchItem,
                        )

                        // Shrink the touch target to decrease row height
                        CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
                            IconButton(
                                onClick = { viewModel.onRemoveQuery(searchItem) }
                            ) {
                                Icon(
                                    modifier = Modifier.size(18.dp),
                                    contentDescription = null,
                                    painter = painterResource(SharedR.drawable.ic_outline_close_24),
                                )
                            }
                        }
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
