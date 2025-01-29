package com.vipul.docscannerapp.screens.home.components

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.vipul.docscannerapp.R
import com.vipul.docscannerapp.utils.getFileUri
import com.vipul.docscannerapp.utils.renameFile
import com.vipul.docscannerapp.utils.showToast
import com.vipul.docscannerapp.viewmodels.PdfViewModel
import java.util.Date

@Composable
fun RenameDeleteDialog(pdfViewModel: PdfViewModel) {
    val context = LocalContext.current
    var newName by remember(pdfViewModel.currentPdfEntity) {
        mutableStateOf(pdfViewModel.currentPdfEntity?.name ?: "")
    }
    if (pdfViewModel.showRenameDialog) {
        Dialog(onDismissRequest = {
            pdfViewModel.showRenameDialog = false
        }) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier.padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.edit_pdf),
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newName,
                        onValueChange = { newText -> newName = newText },
                        label = {
                            Text(stringResource(R.string.pdf_name))
                        })
                    Spacer(modifier = Modifier.height(8.dp))

                    Row (horizontalArrangement = Arrangement.SpaceBetween){
                        IconButton(onClick = {
                            pdfViewModel.currentPdfEntity?.let {
                                pdfViewModel.showRenameDialog = false

                                val fileUri = getFileUri(context, it.name)
                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "application/pdf"
                                    putExtra(Intent.EXTRA_STREAM, fileUri)
                                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                }

                                context.startActivity(
                                    Intent.createChooser(
                                        shareIntent,
                                        "Share PDF"
                                    )
                                )
                            }
                        }) {
                            Icon(
                                painterResource(id = R.drawable.ic_share),
                                contentDescription = "share",
                                tint = Color.Red
                            )
                        }
                        IconButton(onClick = {
                            pdfViewModel.currentPdfEntity?.let {
                                pdfViewModel.showRenameDialog = false
                                if (context.deleteFile(it.name)) {
                                    pdfViewModel.deletePdf(it)
                                } else {
                                    context.showToast("Something went wrong")
                                }
                            }
                        }) {
                            Icon(
                                painterResource(id = R.drawable.ic_delete),
                                contentDescription = "delete",
                                tint = Color.Red
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = { pdfViewModel.showRenameDialog = false }) {
                            Text(stringResource(R.string.cancel))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = {
                            pdfViewModel.currentPdfEntity?.let {
                                if (!it.name.equals(newName, true)) {
                                    pdfViewModel.showRenameDialog = false
                                    renameFile(context, it.name, newName)

                                    val updatePdf = it.copy(
                                        name = newName,
                                        lastModifiedTime = Date()
                                    )
                                    pdfViewModel.updatePdf(updatePdf)
                                } else {
                                    pdfViewModel.showRenameDialog = false
                                }
                            }
                        }) {
                            Text(stringResource(R.string.update))
                        }
                    }
                }
            }
        }
    }
}




