package com.soturine.scanora.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.soturine.scanora.R
import com.soturine.scanora.core.ui.component.SectionHeader

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
        verticalArrangement = Arrangement.Center,
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                SectionHeader(
                    eyebrow = "${currentPage + 1}/${pages.size}",
                    title = stringResource(id = pages[currentPage].first),
                    supportingText = stringResource(id = pages[currentPage].second),
                )
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
    }
}

