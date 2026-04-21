package com.seunome.scanora.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.seunome.scanora.R

@Composable
fun OnboardingScreen(
    onFinish: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val pages = listOf(
        Pair(
            R.string.onboarding_page_one_title,
            R.string.onboarding_page_one_body,
        ),
        Pair(
            R.string.onboarding_page_two_title,
            R.string.onboarding_page_two_body,
        ),
        Pair(
            R.string.onboarding_page_three_title,
            R.string.onboarding_page_three_body,
        ),
    )
    var currentPage by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Text(
            text = stringResource(id = pages[currentPage].first),
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = stringResource(id = pages[currentPage].second),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
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

