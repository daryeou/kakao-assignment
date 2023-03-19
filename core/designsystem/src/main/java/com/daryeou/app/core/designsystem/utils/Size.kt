package com.daryeou.app.core.designsystem.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Float.toSp(): TextUnit = Dp(this@toSp).value.sp

@Composable
fun Int.toSp(): TextUnit = Dp(this@toSp.toFloat()).value.sp

@Composable
fun Dp.toSp(): TextUnit = this@toSp.value.sp

@Composable
fun TextUnit.toDp(): Dp = this@toDp.value.dp