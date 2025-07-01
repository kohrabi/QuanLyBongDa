package com.example.quanlybongda.ui.jetpackcompose.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun <T> SwipeToDeleteContainer(
    item: T,
    onUpdate: (T) -> Unit = {},
    onDelete: suspend (T) -> Boolean,
    animationDuration : Int = 500,
    isEditable: Boolean = false,
    content: @Composable (T) -> Unit,
    modifier: Modifier = Modifier,
    backgroundModifier: Modifier = Modifier,
) {
    var isRemoved by remember(item) { mutableStateOf(false) }
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                isRemoved = true;
            }
            return@rememberSwipeToDismissBoxState true;
        },
        positionalThreshold = { it * 0.9f }
    )
    var isUpdating by remember() { mutableStateOf(false) }

    LaunchedEffect(isRemoved) {
        if (isRemoved) {
            delay(animationDuration.toLong());
            isRemoved = onDelete(item);
        }
    }

    LaunchedEffect(dismissState.currentValue) {
        if (dismissState.currentValue != SwipeToDismissBoxValue.Settled) {
            val state = dismissState.currentValue;
            dismissState.reset()
            if (!isUpdating && state == SwipeToDismissBoxValue.StartToEnd) {
                onUpdate(item);
                isUpdating = true;
            }
        }
        else {
            isUpdating = false;
        }
    }


    AnimatedVisibility(
        visible = !isRemoved,
        exit = slideOutHorizontally(animationSpec = tween(durationMillis = animationDuration), targetOffsetX = { -it * 2 }) +
                shrinkVertically(animationSpec = tween(durationMillis = animationDuration), shrinkTowards = Alignment.CenterVertically),
        modifier = modifier
    ) {
        SwipeToDismissBox(
            state = dismissState,
            backgroundContent = {
                DeleteBackground(
                    swipeToDismissBoxState = dismissState,
                    leftIcon = Icons.Default.Edit,
                    rightIcon = Icons.Default.Delete,
                    backgroundModifier = backgroundModifier
                );
            },
            enableDismissFromEndToStart = isEditable,
            enableDismissFromStartToEnd = isEditable,
            modifier = Modifier.fillMaxSize()
        ) {
            content(item);
        }
    }
}


@Composable
fun <T> SwipeContainer(
    item: T,
    leftSwipe: ((T) -> Unit)? = null,
    rightSwipe: ((T) -> Unit)? = null,
    content: @Composable (T) -> Unit,
    backgroundModifier: Modifier = Modifier,
    leftIcon: ImageVector = Icons.Default.Restore,
    rightIcon: ImageVector = Icons.Default.Delete,
    leftColor: Color = Color(0xFF74C27A),
    rightColor: Color = Color(0xFFDC456F)
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                if (rightSwipe != null) {
                    rightSwipe(item);
                }
            }
            else if (value == SwipeToDismissBoxValue.StartToEnd) {
                if (leftSwipe != null) {
                    leftSwipe(item);
                }
            }
            return@rememberSwipeToDismissBoxState true;
        },
        positionalThreshold = { it * 0.9f }
    )
    var isUpdating by remember() { mutableStateOf(false) }

    LaunchedEffect(dismissState.currentValue) {
        if (dismissState.currentValue != SwipeToDismissBoxValue.Settled) {
            delay(500);
            withFrameNanos {}
            dismissState.reset()
        }
    }

    
    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            DeleteBackground(
                swipeToDismissBoxState = dismissState,
                leftIcon = leftIcon,
                rightIcon = rightIcon,
                backgroundModifier = backgroundModifier,
                leftColor = leftColor,
                rightColor = rightColor
            );
        },
        enableDismissFromEndToStart = leftSwipe != null,
        enableDismissFromStartToEnd = rightSwipe != null,
        modifier = Modifier.fillMaxSize()
    ) {
        content(item);
    }
}

@Composable
fun DeleteBackground(
    swipeToDismissBoxState: SwipeToDismissBoxState,
    leftIcon: ImageVector = Icons.Default.Restore,
    rightIcon: ImageVector = Icons.Default.Delete,
    backgroundModifier: Modifier = Modifier,
    leftColor: Color = Color(0xFF74C27A),
    rightColor: Color = Color(0xFFDC456F)
) {

    // Color(0xFFDC456F), Color(0xFFB06AB3)
    val animatedColor by animateColorAsState(
        if (swipeToDismissBoxState.dismissDirection == SwipeToDismissBoxValue.StartToEnd) leftColor
        else if (swipeToDismissBoxState.dismissDirection == SwipeToDismissBoxValue.EndToStart) rightColor
        else Color.Transparent,
        label = "color"
    )

    Box(
        modifier = backgroundModifier
            .drawBehind {
                drawRect(animatedColor)
            }
        .fillMaxSize(),
        contentAlignment =
            if (swipeToDismissBoxState.dismissDirection == SwipeToDismissBoxValue.StartToEnd)
                Alignment.CenterStart
            else
                Alignment.CenterEnd) {
        if (swipeToDismissBoxState.dismissDirection == SwipeToDismissBoxValue.StartToEnd) {
            Icon(
                imageVector = leftIcon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.padding(8.dp)
            )
        }
        else if (swipeToDismissBoxState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
            Icon(
                imageVector = rightIcon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
//
//    Row(
//        modifier = backgroundModifier
//            .fillMaxSize(),
//        horizontalArrangement = Arrangement.SpaceEvenly,
//        verticalAlignment = Alignment.CenterVertically,
//    ) {
//        if (swipeToDismissBoxState.dismissDirection == SwipeToDismissBoxValue.StartToEnd) {
//            Box(
//                modifier = Modifier
//                    .background(Color.Green)
//                    .fillMaxHeight()
//                    .weight(1.0f),
//                contentAlignment = Alignment.CenterStart
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Restore,
//                    contentDescription = null,
//                    tint = Color.White,
//                    modifier = Modifier.padding(8.dp)
//                )
//            }
//        }
//        else if (swipeToDismissBoxState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
//            Box(
//                modifier = Modifier
//                    .background(Color.Red)
//                    .fillMaxHeight()
//                    .weight(1.0f),
//                contentAlignment = Alignment.CenterEnd
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Delete,
//                    contentDescription = null,
//                    tint = Color.White,
//                    modifier = Modifier.padding(8.dp)
//                )
//            }
//        }
//        else {
//            Box(
//                modifier = Modifier
//                    .background(Color.Green)
//                    .fillMaxHeight()
//                    .weight(1.0f),
//                contentAlignment = Alignment.CenterStart
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Restore,
//                    contentDescription = null,
//                    tint = Color.White,
//                    modifier = Modifier.padding(8.dp)
//                )
//            }
//            Box(
//                modifier = Modifier
//                    .background(Color.Red)
//                    .fillMaxHeight()
//                    .weight(1.0f),
//                contentAlignment = Alignment.CenterEnd
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Delete,
//                    contentDescription = null,
//                    tint = Color.White,
//                    modifier = Modifier.padding(8.dp)
//                )
//            }
//
//        }
//    }
}
