package com.real.patientcare.ui.presentation.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.real.patientcare.R
import com.real.patientcare.common.WavyBackgroundView

@Composable
fun LoginScreen(
    modifier: Modifier, // this modifier is only for the column which shows the content. this modifier already apply inner padding
    navigateToDashboard: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val passwordFocusRequester = remember { FocusRequester() }
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when(effect) {
                LoginContracts.Effect.NavigateToDashboard -> { navigateToDashboard() }
                is LoginContracts.Effect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        WavyBackgroundView(R.color.teal_200, 900.dp)

        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(30.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(text = "User Login", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = painterResource(id = R.drawable.login_icon),
                contentDescription = "Login image",
                modifier = Modifier.size(200.dp)
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.email,
                onValueChange = {
                    viewModel.onIntent(
                        LoginContracts.Intent.EmailChanged(email = it)
                    )
                },
                isError = state.emailError != null,
                label = { Text("Email") },
                singleLine = true,
                keyboardActions = KeyboardActions(
                    onNext = {
                        passwordFocusRequester.requestFocus()
                    }
                )
            )
            if (state.emailError != null) {
                Text(
                    modifier = Modifier.fillMaxWidth().align(Alignment.Start),
                    text = state.emailError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.password,
                onValueChange = {
                    viewModel.onIntent(LoginContracts.Intent.PasswordChanged(it))
                },
                isError = state.passwordError != null,
                visualTransformation = PasswordVisualTransformation(),
                label = { Text("Password") },
                singleLine = true,
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                )
            )
            if (state.passwordError != null) {
                Text(
                    modifier = Modifier.fillMaxWidth().align(Alignment.Start),
                    text = state.passwordError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(50.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp)
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF03DAC5),
                    contentColor = Color.White
                ),
                onClick = {
                    viewModel.onIntent(LoginContracts.Intent.LoginClicked)
                }
            ) {

                if (state.isLoading)
                    CircularProgressIndicator(
                        modifier = Modifier.size(30.dp),
                        strokeWidth = 2.dp,
                        color = Color.Black
                    )

                else
                    Text("Login")
            }
        }
    }
}