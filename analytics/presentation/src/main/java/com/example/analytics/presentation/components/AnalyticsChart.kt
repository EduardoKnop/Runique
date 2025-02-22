package com.example.analytics.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.analytics.presentation.R
import com.example.analytics.presentation.getValuePercentageForRange
import com.example.core.presentation.designsystem.ArrowRightIcon
import com.example.core.presentation.designsystem.KeyboardArrowDownIcon
import com.example.core.presentation.designsystem.RuniqueGray40
import com.example.core.presentation.designsystem.RuniqueGreen
import com.example.core.presentation.designsystem.RuniqueGreen30
import com.example.core.presentation.designsystem.RuniqueTheme
import com.example.core.presentation.designsystem.RuniqueWhite
import com.example.core.presentation.ui.toFormattedMonthYear
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun AnalyticsChart(
    title: String,
    list: List<Pair<LocalDate, Double>>,
    modifier: Modifier = Modifier
) {
    val today = LocalDate.now()
    var listDistances = getListDistanceForMonthAndYear(list, today.monthValue, today.year)

    val listMonthYear = getListMonthsFromDateUntilNow(list[0].first)
    val isDropDownExpanded = remember { mutableStateOf(false) }
    val itemPosition = remember { mutableIntStateOf(listMonthYear.lastIndex) }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(15.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp
            )

            Icon(
                imageVector = ArrowRightIcon,
                contentDescription = stringResource(R.string.go_to) + title,
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        if (listDistances.size >= 2) {
            Chart(
                list = listDistances,
                modifier = Modifier
                    .padding(vertical = 32.dp)
                    .weight(15f)
            )
        } else if (listDistances.size == 1) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(15f),
                horizontalArrangement = Arrangement.Center
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    drawCircle(
                        color = RuniqueGreen,
                        radius = 10f
                    )

                    drawLine(
                        color = RuniqueGray40,
                        start = Offset(x = 0f, y = size.height.times(1)),
                        end = Offset(x = size.width, y = size.height.times(1)),
                        strokeWidth = 5f
                    )
                }
            }

            Text(
                text = "1",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isDropDownExpanded.value = true },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = listMonthYear[itemPosition.intValue],
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = KeyboardArrowDownIcon,
                    contentDescription = stringResource(R.string.select_month),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            DropdownMenu(
                expanded = isDropDownExpanded.value,
                onDismissRequest = { isDropDownExpanded.value = false },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                listMonthYear.forEachIndexed { index, monthYear ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = monthYear,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 14.sp
                            )
                        },
                        onClick = {
                            isDropDownExpanded.value = false
                            itemPosition.intValue = index
                            val dateFormatter =
                                DateTimeFormatter.ofPattern("MMMM - yyyy", Locale.getDefault())
                            val date =
                                LocalDate.parse(listMonthYear[itemPosition.intValue], dateFormatter)
                            listDistances =
                                getListDistanceForMonthAndYear(list, date.monthValue, date.year)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun Chart(
    list: List<Double>,
    modifier: Modifier = Modifier
) {
    var clickPosition by remember { mutableIntStateOf(-1) }
    val zipList = list.zipWithNext()
    val max = list.max()
    val min = list.min()

    Box(modifier = modifier) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->
                            val columnWidth = 1f / list.lastIndex * size.width

                            for (i in 0 .. list.lastIndex) {
                                if (offset.x - columnWidth / 2 < i * columnWidth) {
                                    clickPosition = i
                                    break
                                }
                            }
                        }
                    }
            ) {
                for (pair in zipList) {
                    val fromValuePercentage = pair.first.getValuePercentageForRange(min, max)
                    val toValuePercentage = pair.second.getValuePercentageForRange(min, max)

                    Canvas(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                    ) {
                        val fromPoint = Offset(
                            x = 0f,
                            y = size.height.times(1 - fromValuePercentage)
                        )
                        val toPoint = Offset(
                            x = size.width,
                            y = size.height.times(1 - toValuePercentage)
                        )

                        drawLine(
                            color = RuniqueGreen,
                            start = fromPoint,
                            end = toPoint,
                            strokeWidth = 10f
                        )
                        drawCircle(
                            color = RuniqueGreen,
                            center = toPoint,
                            radius = 5f
                        )

                        drawLine(
                            color = RuniqueGray40,
                            start = Offset(x = 0f, y = size.height.times(1)),
                            end = Offset(x = size.width, y = size.height.times(1)),
                            strokeWidth = 5f
                        )

                        if (zipList.indexOf(pair) == clickPosition) {
                            drawCircle(
                                color = RuniqueWhite,
                                center = fromPoint,
                                radius = 24f,
                                style = Stroke(width = 6f)
                            )

                            drawCircle(
                                color = RuniqueGreen30,
                                center = fromPoint,
                                radius = 18f
                            )
                        } else if (zipList.indexOf(pair) + 1 == clickPosition) {
                            drawCircle(
                                color = RuniqueWhite,
                                center = toPoint,
                                radius = 24f,
                                style = Stroke(width = 6f)
                            )

                            drawCircle(
                                color = RuniqueGreen30,
                                center = toPoint,
                                radius = 18f
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                for (i in 1 .. list.size) {
                    Text(
                        text = i.toString(),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .widthIn(min = 16.dp)
                    )
                }
            }
        }
    }
}

private fun getListDistanceForMonthAndYear(
    list: List<Pair<LocalDate, Double>>,
    month: Int,
    year: Int
): List<Double> {
    val listDistances = mutableListOf<Double>()

    var day = LocalDate.of(year, month, 1)
    while (!day.isAfter(LocalDate.now()) && !day.isAfter(day.plusMonths(1))) {
        listDistances.add(0.0)
        day = day.plusDays(1)
    }

    for (i in 0 .. listDistances.lastIndex) {
        for (pair in list) {
            val date = pair.first
            if (date.monthValue == month && date.year == year) {
                if (date.dayOfMonth == (i + 1)) {
                    listDistances[i] = pair.second
                }
            }
        }
    }

    return listDistances
}

private fun getListMonthsFromDateUntilNow(date: LocalDate): List<String> {
    val listMonthYear = mutableListOf<String>()

    var day = date
    while (!day.isAfter(LocalDate.now())) {
        listMonthYear.add(day.toFormattedMonthYear())
        day = day.plusMonths(1)
    }

    return listMonthYear
}

@Preview
@Composable
private fun AnalyticsChartPreview() {
    RuniqueTheme {
        AnalyticsChart(
            title = "Avg. Distance per Run Over Time",
            list = listOf(
                Pair(LocalDate.parse("2024-10-01"), 1.0),
                Pair(LocalDate.parse("2024-10-02"), 9.0),
                Pair(LocalDate.parse("2024-10-03"), 6.0),
                Pair(LocalDate.parse("2024-10-04"), 31.0),
                Pair(LocalDate.parse("2024-10-05"), 2.0),
                Pair(LocalDate.parse("2024-10-06"), 9.0),
                Pair(LocalDate.parse("2024-10-07"), 8.0),
                Pair(LocalDate.parse("2024-10-08"), 15.0),
                Pair(LocalDate.parse("2024-10-09"), 2.0),
                Pair(LocalDate.parse("2024-10-10"), 5.0),
                Pair(LocalDate.parse("2024-10-11"), 17.0),
                Pair(LocalDate.parse("2024-10-12"), 9.0),
                Pair(LocalDate.parse("2024-10-13"), 3.0),
                Pair(LocalDate.parse("2024-10-14"), 0.0),
                Pair(LocalDate.parse("2024-10-15"), 2.0),
                Pair(LocalDate.parse("2024-10-16"), 6.0),
            )
        )
    }
}