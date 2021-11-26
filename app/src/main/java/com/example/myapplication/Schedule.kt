package com.example.myapplication

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*
//保存形式
open class Schedule : RealmObject() {
    //保存項目
    @PrimaryKey
    //データの番号
    var id: Long =0
    //データ保存の日付(開始)
    var date: Date = Date()
    //データ保存の日付(終了)
    var date2: Date = Date()
    //タイトル
    var title: String=""
    //詳細
    var detil: String=""
    //終了時間が入力されていれば0、なければ1
    var timeflg =0

}