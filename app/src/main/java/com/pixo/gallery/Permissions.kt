package com.pixo.gallery

import android.os.Build
import androidx.compose.runtime.Composable
import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

object Permissions {    // 사용할 권한 정의
    val readExternalStoragePermission = if (Build.VERSION.SDK_INT < 33) {
        Manifest.permission.READ_EXTERNAL_STORAGE
    } else {
        Manifest.permission.READ_MEDIA_IMAGES
    }

    const val cameraPermission = Manifest.permission.CAMERA
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionRequester(
    permission: String,
    onDismissRequest: () -> Unit,
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    val context = LocalContext.current as MainActivity
    val permissionState = rememberPermissionState(permission)

    // 사용자가 권한 요청을 거부했을 때 보여줄 다이얼로그
    var showDialog by remember { mutableStateOf(false) }
    if (showDialog) {
        CheckPermissionDialog(onDismissRequest = {
            onDismissRequest()
            showDialog = !showDialog
        }) {
            moveToSetting(context)
            showDialog = !showDialog
        }
    }

    // 권한 요청 결과 반환
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            onPermissionGranted()
        } else {
            if (shouldShowRequestPermissionRationale(context, permission)) {
                showDialog = !showDialog
            } else {
                onPermissionDenied()
            }
        }
    }

    // 권한 요청
    LaunchedEffect(Unit) {
        if (permissionState.status.shouldShowRationale) {   // 사용자가 거부한 이력이 있으면 다이얼로그
            showDialog = !showDialog
        } else {
            permissionLauncher.launch(permission)
        }
    }
}


@Composable
fun CheckPermissionDialog(onDismissRequest: () -> Unit, onConfirmation: () -> Unit) {
    AlertDialogWithTwoButton(
        onDismissRequest = { onDismissRequest() },
        onConfirmation = { onConfirmation() },
        dialogTitle = "서비스 이용 알림",
        dialogText = "해당 기능에 대한 권한 사용을 거부하였습니다. 기능 사용을 원하실 경우 휴대폰 설정 > 애플리케이션 관리자에서 해당 앱의 권한을 허용해주세요.",
        icon = Icons.Default.Info
    )
}

// 시스템 설정 페이지로 이동
private fun moveToSetting(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
    }
    context.startActivity(intent)
}

@Composable
fun AlertDialogWithTwoButton(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector? = null,
    confirmButtonText: String = "확인",
    dismissButtonText: String = "취소"
) {
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        title = {
            Row(
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(text = dialogTitle)
            }
        },
        text = {
            Text(text = dialogText)
        },
        confirmButton = {
            TextButton(onClick = { onConfirmation() }) {
                Text(text = confirmButtonText)
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text(text = dismissButtonText)
            }
        },
        modifier = Modifier.padding(16.dp)
    )
}