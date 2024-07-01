package com.dessalines.rankmyfavs.ui.components.favlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.dessalines.rankmyfavs.R
import com.dessalines.rankmyfavs.db.FavListItemInsert
import com.dessalines.rankmyfavs.db.FavListItemViewModel
import com.dessalines.rankmyfavs.ui.components.common.SMALL_PADDING
import com.dessalines.rankmyfavs.ui.components.common.SimpleTopAppBar
import com.dessalines.rankmyfavs.ui.components.favlistitem.FavListItemForm

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImportListScreen(
    navController: NavController,
    favListItemViewModel: FavListItemViewModel,
    favListId: Int,
) {
    val scrollState = rememberScrollState()
    var listStr = ""

    Scaffold(
        topBar = {
            SimpleTopAppBar(
                text = stringResource(R.string.import_list),
                navController = navController,
                onClickBack = {
                    navController.navigate("favListDetails/$favListId")
                },
            )
        },
        content = { padding ->
            Column(
                modifier =
                    Modifier
                        .padding(padding)
                        .verticalScroll(scrollState)
                        .imePadding(),
            ) {
                ImportListForm(
                    onChange = { listStr = it },
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val listItems = extractLines(listStr)
                    for (item in listItems) {
                        val insert =
                            FavListItemInsert(
                                favListId = favListId,
                                name = item,
                            )
                        favListItemViewModel.insert(insert)
                    }
                    navController.navigateUp()
                },
                shape = CircleShape,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Save,
                    contentDescription = stringResource(R.string.save),
                )
            }
        },
    )
}

private fun extractLines(listStr: String): List<String> {
    val listItems =
        listStr
            .lines()
            .map { it.trim() }
            // Remove the preceding list items if necessary
            .map {
                if (it.startsWith("- ") || it.startsWith("* ")) {
                    it.substring(2)
                } else {
                    it
                }
            }
    return listItems
}

@Composable
fun ImportListForm(onChange: (String) -> Unit) {
    var listStr by rememberSaveable {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier.padding(horizontal = SMALL_PADDING),
        verticalArrangement = Arrangement.spacedBy(SMALL_PADDING),
    ) {
        OutlinedTextField(
            label = { Text(stringResource(R.string.import_list_description)) },
            minLines = 3,
            modifier = Modifier.fillMaxWidth(),
            value = listStr,
            onValueChange = {
                listStr = it
                onChange(listStr)
            },
        )
    }
}

@Composable
@Preview
fun FavListItemFormPreview() {
    FavListItemForm(onChange = {})
}