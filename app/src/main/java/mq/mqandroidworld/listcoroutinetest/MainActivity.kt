package mq.mqandroidworld.listcoroutinetest

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mq.mqandroidworld.listcoroutinetest.ui.theme.ListCoroutineTestTheme
import java.sql.Time


data class TimeData(var time:Int)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dataList = arrayListOf<TimeData>()
        setContent {
            ListCoroutineTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting(dataList)
                }
            }
        }
    }
}

fun <T> SnapshotStateList<T>.changeList(list:ArrayList<T>){
    clear()
    addAll(list)
}

@Composable
fun ListItem(item:TimeData, list:ArrayList<TimeData>, mutableList:SnapshotStateList<TimeData>){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .border(1.5.dp, MaterialTheme.colors.secondary, MaterialTheme.shapes.medium),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        var time by remember { mutableStateOf(item.time) }
        var isRunning by remember { mutableStateOf(false) }
        var isChanged by remember { mutableStateOf(false) }

        // 리스트 중 중간에 하나가 삭제가 되면
        // 중간부터 아래는 전부 item이 서로 바뀌는 것을 볼 수 있다.
        // 따라서 변수 item이 변화가 생겨서 item을 업뎃 해주게 된다.
        LaunchedEffect(key1 = item){
            time = item.time
            Log.i("asdf1234","업데이트 $time")
        }

        Text(
            text = "$time",
            style = MaterialTheme.typography.h4,
            modifier = Modifier
                .padding(5.dp)
                .weight(1f),
            textAlign = TextAlign.Center
        )
        Row(
            modifier = Modifier
                .weight(2.5f)
                .padding(5.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = {
                    isRunning = !isRunning
                },
                modifier = Modifier
                    .padding(3.dp)
                    .width(50.dp)
                    .height(50.dp),
                shape = CircleShape
                ) {
                Text(
                    text = if(!isRunning) "▷" else "∥",
                    style = MaterialTheme.typography.h5
                )
            }
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .padding(3.dp)
                    .width(50.dp)
                    .height(50.dp),
                shape = CircleShape
            ) {
                Text(
                    text = "R",
                    style = MaterialTheme.typography.h5
                )
            }
            // 삭제
            Button(
                onClick = {
                    list.remove(item)
                    mutableList.changeList(list)
                    isChanged = !isChanged
                },
                modifier = Modifier
                    .padding(3.dp)
                    .width(50.dp)
                    .height(50.dp),
                shape = CircleShape
            ) {
                Text(
                    text = "X",
                    style = MaterialTheme.typography.h5
                )
            }
        }
    }
}

@Composable
fun Greeting(list:ArrayList<TimeData>) {
    val mutableList = remember {
        val mu = mutableStateListOf<TimeData>()
        mu.addAll(list)
        mu
    }
    var count by remember { mutableStateOf(0) }
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ){
       itemsIndexed(mutableList){ i, item ->
            ListItem(item, list, mutableList)
       }
    }
    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.End,
        modifier = Modifier.fillMaxSize()
    ){
        Button(
            onClick = {
                list.add(TimeData(count++))
                mutableList.changeList(list)
            },
            modifier = Modifier
                .padding(20.dp)
                .width(60.dp)
                .height(60.dp),
            shape = CircleShape
        ) {
            Text(
                text = "+",
                style = MaterialTheme.typography.h5,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ListCoroutineTestTheme {
        Greeting(arrayListOf(TimeData(0)))
    }
}