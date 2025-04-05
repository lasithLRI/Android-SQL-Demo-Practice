package com.example.sqldemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.room.Room
import kotlinx.coroutines.launch


lateinit var db: AppDatabase
lateinit var userDao: UserDao

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = Room.databaseBuilder(this,AppDatabase::class.java, "mydatabase").build()
        userDao = db.userDao()
        setContent {
            GUI()
        }
    }
}


@Composable
fun GUI(){
    var usersString by remember { mutableStateOf("") }

    LaunchedEffect(usersString){
        userDao.insertAll(
            User(1, "John", "Smith"),
            User(2, "Helen", "Stones"),
            User(3, "Mary", "Popkins"),
            User(4, "Tom", "Jones"),
        )
    }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row {
            Button(onClick = {
                scope.launch {
                    usersString = retrieveData(userDao)
                }
            }) {
                Text("Retrieve Data")
            }


            Button(onClick = {
                scope.launch {
                    userDao.insertUser(User(firstName = "Bob", lastName = "Butterfly"))
                    usersString = retrieveData(userDao)
                }
            }) {
                Text("Insert User")
            }

            Button(onClick = {
                scope.launch {
                    userDao.deleteAll()
                    usersString = retrieveData(userDao)
                }
            }) {
                Text("Delete Data")
            }


        }

        Text(modifier = Modifier.fillMaxSize(), textAlign = TextAlign.Center, text = usersString)

    }

}

suspend fun retrieveData(userDao: UserDao):String{
    var allUsers = ""

    val users: List<User> = userDao.getAll()

    for (u in users)
        allUsers += "${u.id} : ${u.firstName} ${u.lastName}\n"

    return allUsers
}

