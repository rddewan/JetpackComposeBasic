package com.example.jetpackcomposebasics

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

val purple200 = Color(0xffCE93D8)
val purple700 = Color(0xff7B1FA2)
val teal200 = Color(0xff80CBC4)
val purple500 = Color(0xff9C27B0)
val gray900 = Color(0xff212121)
val gray200 = Color(0xffEEEEEE)


private val DarkColors = darkColors(
    primary = gray200,
    secondary = gray900,
    primaryVariant = gray900,

)

private val LightColors = lightColors(
    primary = purple500,
    primaryVariant = purple700,
    secondary = teal200
)


@Composable
fun baseTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColors
    } else {
        LightColors
    }

    MaterialTheme(colors = colors) {
        content()
    }
}


/*
 Composable function that takes as a parameter a Composable function (here called content)
 which returns Unit. You return Unit because, as you might have noticed, Composable functions
 don't return UI components, they emit them. That's why they must return Unit
 */
@Composable
fun myApp(content: @Composable() () -> Unit) {
    baseTheme {
        Surface(color = Color.LightGray) {
            //screenContentLoop()
            screenContentList()
        }

    }
}

@Composable
fun screenContent() {
    Column {
        greeting(name = "Mr.")
        Divider(color = Color.Blue)//Divider is a provided composable function that creates a horizontal divider.
        greeting(name = "Richard Dewan")
    }
}

/*
Compose functions can be called like any other function in Kotlin.
This makes building UIs really powerful since you can add statements to influence how the UI
 will be displayed.
For example, you can use a for loop to add elements
 */
@Composable
fun screenContentLoop(names: List<String> = listOf("A", "B", "C", "D", "E")) {
    val counterState = remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxHeight()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            for (name in names) {
                greeting(name = name)
                Divider(color = Color.Black)
            }
        }

        Divider(color = Color.Red)
        counter()

        Divider(color = Color.Red)
        counter(count = counterState.value, updateCount = { newCount ->
            counterState.value = newCount
        })
    }
}

@Composable
fun screenContentList(names: List<String> = List(1000) { "User $it" }) {
    nameList(names = names)
}

/*
State in Compose
Under the hood, Compose uses a custom Kotlin compiler plugin so when the underlying data changes,
the composable functions can be re-invoked to update the UI hierarchy.
mutableStateOf function, which gives a composable mutable memory.
To not have a different state for every recomposition, remember the mutable state using remember.
 And, if there are multiple instances of the composable at different places on the screen,
 each copy will get its own version of the state. You can think of internal state as a
 private variable in a class
 */
@Composable
fun counter() {
    val count = remember { mutableStateOf(0) }
    OutlinedButton(onClick = { count.value++ }) {
        Text("I've been clicked ${count.value} times")

    }
}

/*
State hoisting is the way to make internal state controllable by the function that called it.
You do so by exposing the state through a parameter of the controlled composable function and
instantiate it externally from the controlling composable. Making state hoistable avoids
duplicating state and introducing bugs, helps reuse composables, and makes composables
substantially easier to test. State that is not interesting to a composable caller should be i
nternal.
*/
@Composable
fun counter(count: Int, updateCount: (Int) -> Unit) {

    OutlinedButton(onClick = { updateCount(count + 1) },
        colors = ButtonDefaults.outlinedButtonColors(
            backgroundColor = if (count > 5) Color.Red else Color.Blue
        )) {
        Text("I've been clicked $count times")
    }
}

/*
LazyColumn renders only the visible items on screen, allowing performance gains when
rendering a big list. It's equivalent in Android Views is RecyclerView
As the list holds thousands of items, which will impact the app's fluidity when rendered,
use LazyColumn to only render the visible elements on the screen rather than all of them.
In its basic usage, the LazyColumn API provides an items element within its scope,
where individual item rendering logic is written:
 */
@Composable
fun nameList(names: List<String>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(items = names) { name ->
            greeting(name = name)
            Divider(color = Color.Red)
        }
    }
}


@Composable
fun greeting(name: String) {
    /*Surface(color = Color.Green, modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Hello $name",
            modifier = Modifier.padding(18.dp),
            style = MaterialTheme.typography.h3
        )
    }*/
    var isSelected by remember { mutableStateOf(false)}
    val backgroundColor by animateColorAsState(if (isSelected) Color.Red else Color.Transparent)

    Text(
        text = "Hello $name",
        modifier = Modifier
            .padding(18.dp)
            .background(color = backgroundColor)
            .clickable(onClick = {isSelected = !isSelected}),
        style = MaterialTheme.typography.h3
    )
}

@Preview(name = "MainPreview", group = "MainActivity")
@Composable
fun defaultPreview() {
    myApp {
        screenContentLoop()
    }
}