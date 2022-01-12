package com.example.myapplication

import java.util.*

//占いでつかってるのかな
class Fortune {
    val fortuneList = arrayOf(R.drawable.daikiti,R.drawable.chukiti,R.drawable.kiti,
    R.drawable.shoukiti,R.drawable.suekiti,R.drawable.kyou)
    val descList = arrayOf(
        "やったね！いい事しか起きないんじゃないかな?",
        "おお!まあいいことの方が多いんじゃないかな?",
        "うーん、なんか普通な日かもね~、悪いことはないと思うよ!",
        "帰り道とか足元に気を付けたほうがいいかもね",
        "夜は出歩かず家でゆっくり過ごそう!!",
        "焦らずゆっくり頑張ろう!!")
    val Color = arrayOf("白色","黒色","赤色","青色","黄色","紫色","紺色","橙色",
        "桃色","茶色","緑色","ベージュ色","金色","銀色")
    val item = arrayOf("歯ブラシ","手帳","携帯電話","Tシャツ","ブレスレット","ネックレス",
        "貯金箱","花柄のアイテム","観葉植物","四角いもの","金属製品","海や湖の絵","丸いもの",
        "細長いもの","キャンドル","財布","鏡","収納アイテム","角張った物","時計","通帳")
    var name: String = ""
    var description: String = ""
    var color: String = ""
    var lucky_item: String = ""
    var index: Int = Random().nextInt(descList.size)

    //占いの結果をランダムに配列に入れる
    open fun getFortune(): Fortune{
        val index2 = Random().nextInt(Color.size)
        var index3 = Random().nextInt(item.size)
        this.name = fortuneList[index].toString()
        this.description = descList[index]
        this.color = Color[index2]
        this.lucky_item = item[index3]
        return this
    }
}