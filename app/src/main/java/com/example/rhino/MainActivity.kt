package com.example.rhino

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rhino.ui.theme.RhinoJavasciptAppTheme
import org.mozilla.javascript.Context
import org.mozilla.javascript.Scriptable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RhinoJavasciptAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(16.dp)
                    ) {
                        var inputText by remember { mutableStateOf("") }
                        var outputText by remember { mutableStateOf("") }

                        // TextField for user to input JavaScript
                        TextField(
                            value = inputText,
                            onValueChange = { inputText = it },
                            label = { Text("Enter JavaScript") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Button to evaluate JavaScript
                        Button(
                            onClick = {
                                // Execute the JavaScript and set the result to outputText
                                outputText = executeJavaScript(inputText)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Run JavaScript")
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Display the result
                        Text(
                            text = "Result: $outputText",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }

    private fun executeJavaScript(jsCode: String): String {
        // Create and enter a Rhino context
        val rhino = Context.enter()
        rhino.optimizationLevel = -1  // Disable JIT for Android compatibility

        return try {
            // Initialize the standard objects (like JavaScript's Object, Function, etc.)
            val scope: Scriptable = rhino.initStandardObjects()

            // Evaluate the JavaScript code
            val result = rhino.evaluateString(scope, jsCode, "JavaScript", 1, null)

            // Convert the result to a string and return it
            Context.toString(result)
        } catch (e: Exception) {
            // If an error occurs, return the error message
            "Error: ${e.message}"
        } finally {
            // Exit the Rhino context to avoid memory leaks
            Context.exit()
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RhinoJavasciptAppTheme {
        Greeting("Android")
    }
}

