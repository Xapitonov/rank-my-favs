package com.dessalines.rankmyfavs.ui.components.favlist.favlistanddetails

import androidx.compose.foundation.BasicTooltipBox
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberBasicTooltipState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.dessalines.rankmyfavs.R
import com.dessalines.rankmyfavs.db.FavList
import com.dessalines.rankmyfavs.ui.components.common.LARGE_PADDING
import com.dessalines.rankmyfavs.ui.components.common.SimpleTopAppBar
import com.dessalines.rankmyfavs.ui.components.common.ToolTip
import com.dessalines.rankmyfavs.utils.SelectionVisibilityState
import kotlin.collections.orEmpty

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun FavListsPane(
    navController: NavController,
    favLists: List<FavList>?,
    onFavListClick: (favListId: Int) -> Unit,
    selectionState: SelectionVisibilityState<Int>,
    isListAndDetailVisible: Boolean,
) {
    val tooltipPosition = TooltipDefaults.rememberPlainTooltipPositionProvider()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val listState = rememberLazyListState()
    val title =
        if (!isListAndDetailVisible) stringResource(R.string.app_name) else stringResource(R.string.lists)

    Scaffold(
        topBar = {
            SimpleTopAppBar(
                text = title,
                scrollBehavior = scrollBehavior,
            )
        },
        modifier = Modifier.Companion.nestedScroll(scrollBehavior.nestedScrollConnection),
        content = { padding ->
            Box(
                modifier =
                    Modifier.Companion
                        .padding(padding)
                        .imePadding(),
            ) {
                LazyColumn(
                    state = listState,
                ) {
                    items(favLists.orEmpty()) { favList ->
                        val selected =
                            when (selectionState) {
                                is SelectionVisibilityState.ShowSelection -> selectionState.selectedItem == favList.id
                                else -> false
                            }

                        FavListRow(
                            favList = favList,
                            onClick = { onFavListClick(favList.id) },
                            selected = selected,
                        )
                    }
                    item {
                        if (favLists.isNullOrEmpty()) {
                            Text(
                                text = stringResource(R.string.no_lists),
                                modifier = Modifier.Companion.padding(horizontal = LARGE_PADDING),
                            )
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            BasicTooltipBox(
                positionProvider = tooltipPosition,
                state = rememberBasicTooltipState(isPersistent = false),
                tooltip = {
                    ToolTip(stringResource(R.string.create_list))
                },
            ) {
                FloatingActionButton(
                    modifier = Modifier.Companion.imePadding(),
                    onClick = {
                        navController.navigate("createFavList")
                    },
                    shape = CircleShape,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = stringResource(R.string.create_list),
                    )
                }
            }
        },
    )
}

@Composable
fun FavListRow(
    favList: FavList,
    selected: Boolean = false,
    onClick: () -> Unit,
) {
    val containerColor =
        if (!selected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant

    ListItem(
        headlineContent = {
            Text(favList.name)
        },
        colors = ListItemDefaults.colors(containerColor = containerColor),
        modifier =
            Modifier.Companion.clickable {
                onClick()
            },
    )
}

@Composable
@Preview
fun FavListRowPreview() {
    FavListRow(
        favList = FavList(id = 1, name = "Fav List 1"),
        onClick = {},
    )
}
