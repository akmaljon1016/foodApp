package com.example.foodapp

fun main() {
    Events<String, Int>("Akmaljon")
}

class Events<T, V>(value: T, data: V?=null) {
    init {
        println(value.toString().length)

    }
}