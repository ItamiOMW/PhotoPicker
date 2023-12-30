package com.itami.photopicker

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.itami.photopicker.ui.theme.PhotoPickerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PhotoPickerTheme {
                var selectedSingleImageUri by rememberSaveable {
                    mutableStateOf<Uri?>(null)
                }

                var selectedMultipleImageUris by rememberSaveable {
                    mutableStateOf<List<Uri>>(emptyList())
                }

                val singleImagePickerLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.PickVisualMedia(),
                    onResult = { uri ->
                        selectedSingleImageUri = uri
                    }
                )

                val multipleImagesPickerLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.PickMultipleVisualMedia(),
                    onResult = { uris ->
                        selectedMultipleImageUris = uris
                    }
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Button(
                                onClick = {
                                    singleImagePickerLauncher.launch(
                                        PickVisualMediaRequest(
                                            mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                                        )
                                    )
                                }
                            ) {
                                Text(text = "Pick single image")
                            }
                            Button(
                                onClick = {
                                    multipleImagesPickerLauncher.launch(
                                        PickVisualMediaRequest(
                                            ActivityResultContracts.PickVisualMedia.ImageOnly
                                        )
                                    )
                                }
                            ) {
                                Text(text = "Pick multiple images")
                            }
                        }
                    }
                    item {
                        AsyncImage(
                            model = selectedSingleImageUri,
                            contentDescription = "Selected image",
                            contentScale = ContentScale.Crop,
                        )
                    }
                    items(items = selectedMultipleImageUris) { uri ->
                        AsyncImage(
                            model = uri,
                            contentDescription = "Selected image",
                            contentScale = ContentScale.Crop,
                        )
                    }
                }
            }
        }
    }
}
