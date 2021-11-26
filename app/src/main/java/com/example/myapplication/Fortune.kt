package com.example.myapplication

import java.util.*

//占いでつかってるのかな
class Fortune {
    val fortuneList = arrayOf("大吉","中吉","吉","小吉","末吉","凶","大凶")
    val descList = arrayOf(
        "やったね！いい事しか起きないんじゃないかな?",
        "おお!まあいいことの方が多いんじゃないかな?",
        "うーん、なんか普通な日かもね~、悪いことはないと思うよ!",
        "帰り道とか足元気を付けたほうがいいかもね",
        "割とがちで夜は出歩かないほうがいいんじゃないかな",
        "うん、人生いい事ばかりじゃないって、元気出せって",
        "あのね、今日は家出ないほうがいいどころか、起きないほうがいいかもね")

    var name: String = ""
    var description: String = ""

    //占いの結果をランダムに配列に入れる
    open fun getFortune(): Fortune{
        val index = Random().nextInt(fortuneList.size)
        this.name = fortuneList[index]
        this.description = descList[index]
        return this
    }
}