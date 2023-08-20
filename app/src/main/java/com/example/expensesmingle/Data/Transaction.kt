package com.example.expensesmingle.Data

data class Transaction(val friend: String, val amount: Double, val reason: String, val iPaid: Boolean, val time: Long) {
}