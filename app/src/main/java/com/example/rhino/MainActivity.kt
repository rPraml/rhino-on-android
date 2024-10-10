package com.example.rhino

import org.mozilla.javascript.Context;
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.rhino.ui.theme.RhinoJavasciptAppTheme
import org.mozilla.javascript.Scriptable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RhinoJavasciptAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = executeJavaScript("'javascript' + ' ' + 'works'"),
                        modifier = Modifier.padding(innerPadding)
                    )
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

