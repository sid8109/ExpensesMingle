package com.example.expensesmingle.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.expensesmingle.Data.Chat
import com.example.expensesmingle.Data.Transaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ExpensesMingleViewModel : ViewModel() {
    private var database: DatabaseReference = Firebase.database.reference
    private var _usernames: MutableMap<String, String> = mutableMapOf()
    private var _friends: MutableLiveData<ArrayList<String>> = MutableLiveData(ArrayList())
    private var _summary: MutableLiveData<MutableMap<String, Double>> = MutableLiveData(mutableMapOf())
    private var _history: MutableLiveData<ArrayList<Triple<String, Double, Long>>> = MutableLiveData(ArrayList())
    private var _chats: MutableLiveData<ArrayList<Pair<String, Boolean>>> = MutableLiveData(ArrayList())
    private var _spentOn: MutableLiveData<ArrayList<String>> = MutableLiveData(ArrayList())
    var chatFriend: String = ""

    val usernames: MutableMap<String, String>
        get() = _usernames

    val friends: LiveData<ArrayList<String>>
        get() = _friends

    val summary: LiveData<MutableMap<String, Double>>
        get() = _summary

    val history: LiveData<ArrayList<Triple<String, Double, Long>>>
        get() = _history

    val chats: LiveData<ArrayList<Pair<String, Boolean>>>
        get() = _chats

    val spentOn: LiveData<ArrayList<String>>
        get() = _spentOn

    fun getUsernames() {
        val usernamesRef = database.child("Users")
        usernamesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (user in snapshot.children) {
                    val username = user.child("username").value
                    val uid = user.child("uid").value
                    if (username != null && uid != null) {
                        _usernames[uid.toString()] = username.toString()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        )
    }

    fun getFriends() {
        val friendsRef = database.child("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("friends")
        friendsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = arrayListOf<String>()
                for (friendSnapshot in snapshot.children) {
                    val friendName = friendSnapshot.getValue(String::class.java)
                    if (friendName != null) {
                        list.add(friendName)
                    }
                }
                _friends.value = list
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun addTrans(whoPaid: String, amt: Double, reason: String, iPaid: Boolean, time: Long) {
        val currUser = usernames[FirebaseAuth.getInstance().currentUser!!.uid]
        database.child("Transactions").child(currUser!!).push()
            .setValue(Transaction(whoPaid, amt, reason, iPaid, time))
        database.child("Transactions").child(whoPaid).push()
            .setValue(Transaction(currUser, amt, reason, !iPaid, time))
    }

    fun getTransactions() {
        val currUser = FirebaseAuth.getInstance().currentUser ?: return
        database.child("Users").child(currUser.uid).child("username").get()
            .addOnSuccessListener {
                val currUserName = it.value.toString()
                val ref = database.child("Transactions").child(currUserName)
                ref.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val netTransactions = mutableMapOf<String, Double>()
                        for (transaction in snapshot.children) {
                            val friend = transaction.child("friend").getValue(String::class.java)
                            val amount = transaction.child("amount").getValue(Double::class.java)!!
                            val iPaid = transaction.child("ipaid").getValue(Boolean::class.java)
                            if (!netTransactions.containsKey(friend.toString())) {
                                netTransactions[friend.toString()] = 0.0
                            }
                            if (iPaid == true) {
                                netTransactions[friend.toString()] =
                                    netTransactions[friend.toString()]!! + amount
                            } else {
                                netTransactions[friend.toString()] =
                                    netTransactions[friend.toString()]!! - amount
                            }
                        }
                        _summary.value = netTransactions
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            }
    }

    fun getHistory(friendName: String) {
        _history.value?.clear()
        val currUser = usernames[FirebaseAuth.getInstance().currentUser!!.uid]!!
        val transRef = database.child("Transactions").child(currUser)
        transRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val h = arrayListOf<Triple<String, Double, Long>>()
                for (transaction in snapshot.children) {
                    val friend = transaction.child("friend").getValue(String::class.java)
                    if(friend != friendName) continue
                    var amount = transaction.child("amount").getValue(Double::class.java)!!
                    val reason = transaction.child("reason").getValue(String::class.java)!!
                    val time = transaction.child("time").getValue(Long::class.java)!!
                    val iPaid = transaction.child("ipaid").getValue(Boolean::class.java)
                    if(iPaid == true) {
                        h.add(Triple(reason, amount, time))
                    } else {
                        amount *= -1
                        h.add(Triple(reason, amount, time))
                    }
                }
                h.reverse()
                _history.value = h
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun addMessage(chat: String) {
        val time = System.currentTimeMillis()
        val currUser = usernames[FirebaseAuth.getInstance().currentUser!!.uid]
        database.child("Message").child(currUser!!).push()
            .setValue(Chat(chatFriend, chat, true, time))
        database.child("Message").child(chatFriend).push()
            .setValue(Chat(currUser, chat, false, time))
    }

    fun getChats() {
        _chats.value?.clear()
        val currUser = usernames[FirebaseAuth.getInstance().currentUser!!.uid]!!
        val chatRef = database.child("Message").child(currUser)
        chatRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val c = arrayListOf<Pair<String, Boolean>>()
                for (Chat in snapshot.children) {
                    val friend = Chat.child("friend").getValue(String::class.java)
                    if(friend != chatFriend) continue
                    val message = Chat.child("message").getValue(String::class.java)!!
                    val iSent = Chat.child("isent").getValue(Boolean::class.java)!!
                    c.add(Pair(message, iSent))
                }
                _chats.value = c
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun calculate(amount: Double, reason: String) {
        val time = System.currentTimeMillis()
        val perPerson = amount / spentOn.value!!.size
        val currUser = usernames[FirebaseAuth.getInstance().currentUser!!.uid]
        for(i in spentOn.value!!) {
            if(i == currUser) {
                continue
            }
            database.child("Transactions").child(currUser!!).push()
                .setValue(Transaction(i, perPerson, reason, true, time))
            database.child("Transactions").child(i).push()
                .setValue(Transaction(currUser, perPerson, reason, false, time))
        }
        _spentOn.value!!.clear()
    }
}