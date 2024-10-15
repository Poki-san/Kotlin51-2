import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlin.system.measureTimeMillis

suspend fun main() {
    var stringResult = ""
    var wordResult = ""
    val time = measureTimeMillis {
        coroutineScope{
            val channelLine = getList(Storage().text)
            val channelChar = modifiedList(channelLine)
            channelChar.consumeEach {stringResult +=it}

            val channelWord = getListWord(Storage().text)
            val channelWordChar = modifiedList(channelWord)
            channelWordChar.consumeEach {wordResult +=it}
        }
    }
    println(time)
    println(stringResult.toList())
    println(wordResult.toList())
}

class Storage {
    val text = """
        Мартышка к старости слаба глазами стала.
        А у людей она слыхала,
        Что это зло еще не так большой руки.
        Лишь стоит завести Очки.
        Очков с полдюжины себе она достала.
        Вертит Очками так и сяк.
        То к темю их прижмет, то их на хвост нанижет,
        То их понюхает, то их полижет.
        Очки не действуют никак.
        Тьфу пропасть! - говорит она,- и тот дурак,
        Кто слушает людских всех врак.
        Всё про Очки лишь мне налгали.
        А проку на-волос нет в них.
        Мартышка тут с досады и с печали
        О камень так хватила их,
        Что только брызги засверкали.
        К несчастью, то ж бывает у людей.
        Как ни полезна вещь,- цены не зная ей,
        Невежда про нее свой толк все к худу клонит.
        А ежели невежда познатней,
        Так он ее еще и гонит. 
        """.trimIndent()
}

fun CoroutineScope.modifiedList(channel: ReceiveChannel<String>): ReceiveChannel<String> = produce {
    channel.consumeEach {
        if (it.isNotBlank()) {
            send(it[0].uppercase())
        }
    }
}

suspend fun CoroutineScope.getListWord(text: String) : ReceiveChannel<String> = produce {
    text.split(Regex("[ ,.,!,\n,-]")).forEach {
        delay(10L)
        send(it)
    }
    close()
}

suspend fun CoroutineScope.getList(text: String) : ReceiveChannel<String> = produce {
    text.lines().forEach {
        delay(10L)
        send(it)
    }
    close()
}
