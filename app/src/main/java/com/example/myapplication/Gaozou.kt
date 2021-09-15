package com.example.myapplication

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Gazou : RealmObject() {
    //保存項目
    @PrimaryKey
    //詳細
    var data: String=""
}