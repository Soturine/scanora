package com.soturine.scanora.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.soturine.scanora.R

@Composable
fun OnboardingScreen(
    onFinish: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val pages = listOf(
        OnboardingPage(
            titleRes = R.string.onboarding_page_one_title,
            bodyRes = R.string.onboarding_page_one_body,
            imageRes = R.drawable.onboarding_scanora_scan,
            imageDescriptionRes = R.string.onboarding_page_one_image_description,
        ),
        OnboardingPage(
            titleRes = R.string.onboarding_page_two_title,
            bodyRes = R.string.onboarding_page_two_body,
            imageRes = R.drawable.onboarding_scanora_adjust,
            imageDescriptionRes = R.string.onboarding_page_two_image_description,
        ),
        OnboardingPage(
            titleRes = R.string.onboarding_page_three_title,
            bodyRes = R.string.onboarding_page_three_body,
            imageRes = R.drawable.onboarding_scanora_privacy,
            imageDescriptionRes = R.string.onboarding_page_three_image_description,
        ),
    )
    var currentPage by remember { mutableIntStateOf(0) }
    val page = pages[currentPage]

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(22.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(id = page.imageRes),
                contentDescription = stringResource(id = page.imageDescriptionRes),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 430.dp),
                contentScale = ContentScale.Fit,
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = stringResource(id = page.titleRes),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
            )
            Text(
                text = stringResource(id = page.bodyRes),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            repeat(pages.size) { index ->
                Surface(
                    modifier = Modifier.size(if (index == currentPage) 22.dp else 10.dp, 10.dp),
                    shape = CircleShape,
                    color = if (index == currentPage) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.surfaceContainerHighest
                    },
                ) {}
            }
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                if (currentPage == pages.lastIndex) {
                    onFinish()
                } else {
                    currentPage += 1
                }
            },
        ) {
            Text(
                text = if (currentPage == pages.lastIndex) {
                    stringResource(id = R.string.onboarding_finish)
                } else {
                    stringResource(id = R.string.onboarding_next)
                },
            )
        }
    }
}

private data class OnboardingPage(
    val titleRes: Int,
    val bodyRes: Int,
    val imageRes: Int,
    val imageDescriptionRes: Int,
)
