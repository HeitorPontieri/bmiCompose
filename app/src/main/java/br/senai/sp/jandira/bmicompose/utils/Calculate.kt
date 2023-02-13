package br.senai.sp.jandira.bmicompose.utils

import kotlin.math.pow

fun bmiCalculate(weight: Int, height: Double): Double = weight / ((height / 100 ).pow(2))