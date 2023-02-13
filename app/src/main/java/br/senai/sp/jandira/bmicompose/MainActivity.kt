package br.senai.sp.jandira.bmicompose

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.android.style.LetterSpacingSpanEm
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.senai.sp.jandira.bmicompose.ui.theme.BmiComposeTheme
import br.senai.sp.jandira.bmicompose.utils.bmiCalculate
import androidx.compose.animation.scaleIn as scaleIn

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BmiComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                ) {
                    BMICalculator()
                }
            }
        }
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BMICalculator() {

    var weightState by rememberSaveable() {
        mutableStateOf("")
    }
    var heightState by rememberSaveable() {
        mutableStateOf("")
    }

    var expandState by remember {
        mutableStateOf(false)
    }
    var bmiScoreState by remember {

        mutableStateOf(0.0)

    }

    var isWeightError by remember {

        mutableStateOf(false)

    }
    var isHeightError by remember {

        mutableStateOf(false)

    }

    val weightFocusRequest = FocusRequester()


// Content
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {

        // Header
        Column(
            modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.bmi),
                contentDescription = "katchau",
                modifier = Modifier.size(100.dp)
            )


            Text(
                text = stringResource(id = R.string.title),
                color = MaterialTheme.colors.primary,
                fontSize = 32.sp,
                letterSpacing = 6.sp
            )

        }

        // Form
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)) {

            Text(
                text = stringResource(id = R.string.weight),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = weightState,
                onValueChange = {
                    var lastchar =
                        if (it.length == 0) {
                            isWeightError = true
                            it
                        }

                        else it.get(it.length - 1)
                            isWeightError = false

                    var newValue =
                        if (lastchar == '.' || lastchar == ',') it.dropLast(1)
                        else it

                    weightState = newValue
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = "00")
                },
                isError = isWeightError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.padding(12.dp))

            Text(
                text = stringResource(id = R.string.height),
                modifier = Modifier.padding(bottom = 4.dp)
            )

            OutlinedTextField(
                value = heightState,
                onValueChange = {
                    var lastchar =
                        if (it.length == 0) {
                            isHeightError = true
                            it
                        }
                        else it.get(it.length - 1)
                            isHeightError = false

                    var newValue =
                        if (lastchar == '.' || lastchar == ',')
                            it.dropLast(1)
                        else it

                    heightState = newValue
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = "0.00")
                },
                isError = isHeightError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                shape = RoundedCornerShape(8.dp)
            )

            Button(
                onClick = {isWeightError = weightState.isEmpty()

                    isHeightError = heightState.isEmpty()

                    if (!isHeightError && !isWeightError){

                        bmiScoreState = bmiCalculate(weightState.toInt(), heightState.toDouble())
                        expandState= true}
                                                                         },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(Color(8, 100, 6))) {
                    Text(text = stringResource(id = R.string.button_calculate), color = Color.White)
            }
        }

        // Footer
        AnimatedVisibility(visible = expandState,
            enter = scaleIn() + expandVertically (expandFrom = Alignment.CenterVertically),
            exit = scaleOut() + shrinkVertically(shrinkTowards = Alignment.CenterVertically)
        ){
            Card(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                backgroundColor = MaterialTheme.colors.primary
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceAround
                ) {

                    Text(
                        text = stringResource(id = R.string.your_score),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = String.format("%.2f", bmiScoreState), fontSize = 48.sp, fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Congratulations, your weight is ideal!",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Row {
                        Button(
                            onClick = { expandState = false},
                            colors = ButtonDefaults.buttonColors(Color(137, 119, 248))
                        ) {
                            Text(text = stringResource(id = R.string.reset))
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Button(
                            onClick = { /*TODO*/ },
                            colors = ButtonDefaults.buttonColors(Color(137, 119, 248))
                        ) {
                            Text(text = stringResource(id = R.string.share))
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    BMICalculator()
}

