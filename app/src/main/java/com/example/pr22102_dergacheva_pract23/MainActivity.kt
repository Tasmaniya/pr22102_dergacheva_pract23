package com.example.pr22102_dergacheva_pract23

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import com.example.pr22102_dergacheva_pract23.ui.theme.Pr22102_dergacheva_pract23Theme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Pr22102_dergacheva_pract23Theme {
                MainContent()
            }
        }
    }
}

@Composable
fun MainContent() {
    var isSplashVisible by remember { mutableStateOf(true) }
    var currentPage by remember { mutableStateOf(0) }

    LaunchedEffect(true) {
        delay(3000)
        isSplashVisible = false
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = isSplashVisible,
            enter = fadeIn(animationSpec = tween(durationMillis = 500)),
            exit = fadeOut(animationSpec = tween(durationMillis = 500))
        ) {
            SplashScreen(onSkip = { isSplashVisible = false })
        }

        AnimatedVisibility(
            visible = !isSplashVisible,
            enter = fadeIn(animationSpec = tween(durationMillis = 500))
        ) {
            AnalysisScreen(
                currentPage = currentPage,
                onSwipeRight = {
                    if (currentPage > 0) currentPage -= 1
                },
                onSwipeLeft = {
                    if (currentPage < 2) currentPage += 1
                    else currentPage = 3
                },
                onSkip = { currentPage = 3 }
            )
        }
    }
}

@Composable
fun SplashScreen(onSkip: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        val backgroundImage: Painter = painterResource(id = R.drawable.background)
        Image(
            painter = backgroundImage,
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        val logoImage: Painter = painterResource(id = R.drawable.img_1)
        Image(
            painter = logoImage,
            contentDescription = "Logo",
            modifier = Modifier
                .align(Alignment.Center)
                .size(300.dp)
        )

        Box(modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)) {
            Text(
                text = "Пропустить",
                color = Color.White,
                fontSize = 18.sp,
                modifier = Modifier.clickable { onSkip() }
            )
        }
    }
}

@Composable
fun AnalysisScreen(
    currentPage: Int,
    onSwipeRight: () -> Unit,
    onSwipeLeft: () -> Unit,
    onSkip: () -> Unit
) {
    val swipeThreshold =65.dp
    val swipeThresholdPx = with(LocalDensity.current) { swipeThreshold.toPx() }

    val pageData = listOf(
        PageData(
            title = "Анализы",
            subtitle = "Экспресс сбор и получение проб",
            imageRes = R.drawable.img_2
        ),
        PageData(
            title = "Уведомления",
            subtitle = "Вы быстро узнаете о результатах",
            imageRes = R.drawable.img_3
        ),
        PageData(
            title = "Мониторинг",
            subtitle = "Наши врачи всегда наблюдают\nза вашими показателями здоровья",
            imageRes = R.drawable.img_4
        )
    )

    if (currentPage == 3) {
        EmptyScreen()
    } else {
        val currentPageData = pageData[currentPage]

        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { _, dragAmount ->
                        if (dragAmount < -swipeThresholdPx) {
                            onSwipeLeft()
                        } else if (dragAmount > swipeThresholdPx) {
                            onSwipeRight()
                        }
                    }
                }
        ) {
            Text(
                text = "Пропустить",
                color = Color.Blue,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopStart)
                    .clickable { onSkip() }
            )

            Image(
                painter = painterResource(id = R.drawable.img),
                contentDescription = "Plus Icon",
                modifier = Modifier
                    .size(100.dp)
                    .padding(16.dp)
                    .align(Alignment.TopEnd)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = currentPageData.title,
                    fontSize = 24.sp,
                    color = Color(0xFF00C853)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = currentPageData.subtitle,
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(3) { index ->
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(
                                    color = if (index == currentPage) Color.Blue else Color.Transparent,
                                    shape = androidx.compose.foundation.shape.CircleShape
                                )
                                .border(
                                    width = 1.dp,
                                    color = Color.Blue,
                                    shape = androidx.compose.foundation.shape.CircleShape
                                )
                                .padding(4.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))

                Image(
                    painter = painterResource(id = currentPageData.imageRes),
                    contentDescription = currentPageData.title,
                    modifier = Modifier.size(180.dp)
                )
            }
        }
    }
}

@Composable
fun EmptyScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "Вход и регистрация",
            fontSize = 24.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

data class PageData(
    val title: String,
    val subtitle: String,
    val imageRes: Int
)