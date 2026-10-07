package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun ConfidenceWhyPanel(
    confidence: Int,
    whyEvidence: String,
    contentToCopy: String,
    onRegenerate: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showWhy by remember { mutableStateOf(false) }
    var userRating by remember { mutableStateOf<Boolean?>(null) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Confidence badge & Why button
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Confidence bar
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(100.dp))
                        .background(LagoonSurfaceSubtleLight)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(if (confidence >= 90) LagoonOnline else LagoonWarning)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "$confidence% confidence",
                            style = MaterialTheme.typography.labelSmall,
                            color = LagoonTealDark
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Why this answer?",
                    style = MaterialTheme.typography.labelSmall,
                    color = LagoonPrimary,
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .clickable { showWhy = !showWhy }
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                        .testTag("why_this_answer_btn")
                )
            }

            // Action buttons: copy, thumbs up/down, share
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("ANAHER Answer", contentToCopy)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy answer",
                        tint = LagoonTextSecondaryLight,
                        modifier = Modifier.size(15.dp)
                    )
                }

                IconButton(
                    onClick = {
                        userRating = true
                        Toast.makeText(context, "Feedback recorded", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = if (userRating == true) Icons.Default.ThumbUp else Icons.Outlined.ThumbUp,
                        contentDescription = "Thumbs up",
                        tint = if (userRating == true) LagoonPrimary else LagoonTextSecondaryLight,
                        modifier = Modifier.size(15.dp)
                    )
                }

                IconButton(
                    onClick = {
                        userRating = false
                        Toast.makeText(context, "Feedback recorded for model evolution", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = if (userRating == false) Icons.Default.ThumbDown else Icons.Outlined.ThumbDown,
                        contentDescription = "Thumbs down",
                        tint = if (userRating == false) LagoonErrorTextLight else LagoonTextSecondaryLight,
                        modifier = Modifier.size(15.dp)
                    )
                }

                IconButton(
                    onClick = {
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, contentToCopy)
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(sendIntent, "Share ANAHER Answer"))
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = LagoonTextSecondaryLight,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }
        }

        // Expandable Why details
        AnimatedVisibility(visible = showWhy) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .border(1.dp, LagoonBorderLight, RoundedCornerShape(10.dp))
                    .background(LagoonSurfaceSubtleLight.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                    .padding(10.dp)
            ) {
                Text(
                    text = "Verification Evidence & Assumptions",
                    style = MaterialTheme.typography.labelSmall.copy(color = LagoonTealDark),
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = whyEvidence,
                    style = MaterialTheme.typography.bodySmall,
                    color = LagoonTextSecondaryLight
                )
            }
        }
    }
}
