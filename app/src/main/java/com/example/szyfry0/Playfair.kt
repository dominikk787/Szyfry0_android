package com.example.szyfry0

import java.util.*

object Playfair {
    object Key {
        data class Alphabet (val description: String, val alphabet: String, val size: Int, val hexPrefix: String = "",
                             val spaceReplace: String = "", val replaceJ: Boolean = false, val lowerCase: Boolean = true) {
            fun trim(str: String): String {
                var s = str.replace(" ", spaceReplace)
                if(lowerCase) s = s.toLowerCase(Locale.getDefault())
                if(replaceJ) s = s.replace('j', 'i')
                if(hexPrefix.isNotEmpty()) {
                    s = s.replace("[^$alphabet]".toRegex()) {
                        hexPrefix + it.value[0].toByte().toString(16)
                    }
                    println(s)
                } else {
                    s = s.replace("[^$alphabet]".toRegex(), "")
                }
                return s
            }
        }
        private val alphabets = listOf(Alphabet("25 Podstawowy alfabet bez 'j'", "abcdefghiklmnopqrstuvwxyz", 5, replaceJ = true),
            Alphabet("36 Rozszerzony alfabet z cyframi", "abcdefghijklmnopqrstuvwxyz0123456789",6,"0xx","0xs"),
            Alphabet("49 Rozszerzony alfabet z cyframi i symbolami", "abcdefghijklmnopqrstuvwxyz0123456789!@*()-=+/,.<>", 7, "*x*", "*s*")
        )
        private var ab: Alphabet? = null

        private object KeyBuf{
            private var ab = Alphabet("", "", 0)
            private var data = ""

            fun setAlphabet(a: Alphabet?) {
                ab = a ?: ab
                data = ab.alphabet
            }
            fun setKey(k: String) {
                if(ab.size > 0) {
                    var key = k
                    if (ab.lowerCase) key = key.toLowerCase(Locale.getDefault())
                    key = key.replace("[^${ab.alphabet}]".toRegex(), "")
                    key = key.toCharArray().distinct().toString()
                    println(key)
                    data = key + ab.alphabet
                    data = data.toCharArray().distinct().toString()
                    println(data)
                }
            }
        }

        fun setAlphabet(id: Int) {
            ab = alphabets[id]
        }
        fun getAlphabet(id: Int): Alphabet? {
            return alphabets[id]
        }
        fun getAlphabet(): Alphabet? {
            return ab
        }
        fun setKey(k: String) {
            KeyBuf.setKey(k)
        }
    }
}