package com.zhuozheng.sawyerdiyview

fun main(){
    val a = "0.0000"
    val b= a.toFloat().toInt().toFloat()
    val result = if (a.toFloat() == b) "true" else "false"
    println("wtf = $result,b = $b")

    val c = 222
    println(String.format("%08d",c))
}