package com.example.myapplication

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Schedule : RealmObject() {
    //保存項目
    @PrimaryKey
    var id: Long =0
    var date: Date = Date()
    var title: String=""
    var detil: String=""
}