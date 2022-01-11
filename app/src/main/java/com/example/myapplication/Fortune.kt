package com.example.myapplication

import java.util.*

//占いでつかってるのかな
class Fortune {
    val fortuneList = arrayOf("大吉","中吉","吉","小吉","末吉","凶","大凶")
    val descList = arrayOf(
        "やったね！いい事しか起きないんじゃないかな?",
        "おお!まあいいことの方が多いんじゃないかな?",
        "うーん、なんか普通な日かも悪いことはないと思うよ!",
        "帰り道とか足元気を付けたほうがいいかもね",
        "割とがちで夜は出歩かないほうがいいんじゃないかな",
        "うん、人生いい事ばかりじゃない元気出せって",
        "あのね、今日は家出ないほうがいい")
    val Color = arrayOf("白色","黒色","赤色","青色","黄色","紫色","紺色","橙色",
        "桃色","茶色","緑色","ベージュ色","金色","銀色")
    val item = arrayOf("歯ブラシ","手帳","携帯電話","Tシャツ","ブレスレット","ネックレス",
        "貯金箱","花柄のアイテム","観葉植物","四角いもの","金属製品","海や湖の絵","丸いもの",
        "細長いもの","キャンドル","財布","鏡","収納アイテム","角張った物","時計","通帳")


    var color: String = ""
    var lucky_item: String = ""
var index:Int=0
    var name: String = ""
    var description: String = ""

    //占いの結果をランダムに配列に入れる
    open fun getFortune(): Fortune{
        this.index = Random().nextInt(fortuneList.size)
        val index2 = Random().nextInt(Color.size)
        var index3 = Random().nextInt(item.size)
        this.color = Color[index2]
        this.lucky_item = item[index3]
        this.name = fortuneList[index]
        this.description = descList[index]
        return this
    }
}