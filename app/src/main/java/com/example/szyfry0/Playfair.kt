package com.example.szyfry0

import java.security.Key
import java.util.*

object Playfair {
    object Key {
        data class Alphabet (val alphabet: String, val size: Int, val hexPrefix: String = "",
                             val spaceReplace: String = "", val replaceJ: Boolean = false, val lowerCase: Boolean = true, val onlyLatin: Boolean = true) {
            fun trim(str: String, useHex: Boolean = true): String {
                var s = str.replace(" ", spaceReplace)
                if(lowerCase) s = s.toLowerCase(Locale.getDefault())
                if(replaceJ) s = s.replace('j', 'i')
                if(hexPrefix.isNotEmpty() && useHex) {
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
        private val alphabets = listOf(Alphabet("abcdefghiklmnopqrstuvwxyz", 5, replaceJ = true),
            Alphabet("abcdefghijklmnopqrstuvwxyz0123456789",6,"0xx","0xs"),
            Alphabet("abcdefghijklmnopqrstuvwxyz0123456789!@*()-=+/,.<>", 7, "*x*", "*s*")
        )
        private var ab: Alphabet = alphabets[0]

        private object KeyBuf{
            private var ab = Alphabet("", 0)
            private var data = ""

            fun setAlphabet(a: Alphabet?) {
                ab = a ?: ab
                data = ab.alphabet
            }
            fun setKey(k: String) {
                if(ab.size > 0) {
                    var key = k
                    if(ab.onlyLatin) key = key.toLatin()
                    key = ab.trim(key, false)
                    println(key)
                    data = key + ab.alphabet
                    data = String(data.toCharArray().distinct().toCharArray())
                    println(data)
                }
            }
            fun get(x: Int, y: Int): Char {
                return data[(y * ab.size) + x]
            }
            fun get(): String = data
        }

        fun setAlphabet(id: Int) {
            ab = getAlphabet(id)
            KeyBuf.setAlphabet(ab)
        }
        fun getAlphabet(id: Int): Alphabet = alphabets[id.rem(alphabets.size)]
        fun getAlphabet(): Alphabet = ab
        fun setKey(k: String) = KeyBuf.setKey(k)
        fun get(x: Int, y: Int): Char = KeyBuf.get(x, y)
        fun get(): String = KeyBuf.get()
    }
}