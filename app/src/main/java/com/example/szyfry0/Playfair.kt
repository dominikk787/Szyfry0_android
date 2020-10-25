package com.example.szyfry0

import java.util.*

object Playfair {
    object Key {
        data class Alphabet (val alphabet: String, val size: Int, val hexPrefix: String = "",
                             val spaceReplace: String = "", val replaceJ: Boolean = false, val lowerCase: Boolean = true, val onlyLatin: Boolean = true, val complete: Char = 'x') {
            fun trim(str: String, useHex: Boolean = true): String {
                var s = str.replace(" ", spaceReplace)
                if(lowerCase) s = s.toLowerCase(Locale.getDefault())
                if(replaceJ) s = s.replace('j', 'i')
                if(hexPrefix.isNotEmpty() && useHex) {
                    s = s.replace("[^$alphabet]".toRegex()) {
                        hexPrefix + "%02x".format(it.value[0].toInt())
                    }
                    println(s)
                } else {
                    s = s.replace("[^$alphabet]".toRegex(), "")
                }
                return s
            }
            fun parseSpecial(instr: String): String {
                var s = instr
                if(hexPrefix.isNotEmpty()) s = s.replace("$hexPrefix[0-9a-f]{2,}".toRegex()) {
                                                            val str = it.value.substring(hexPrefix.length)
                                                            str.toInt(16).toChar().toString()
                                                        }
                if(spaceReplace.isNotEmpty()) s = s.replace(spaceReplace, " ")
                return s
            }
        }
        private val alphabets = listOf(Alphabet("abcdefghiklmnopqrstuvwxyz", 5, replaceJ = true),
            Alphabet("abcdefghijklmnopqrstuvwxyz0123456789",6,"0xy","0xs", complete = '0'),
            Alphabet("abcdefghijklmnopqrstuvwxyz0123456789!@*()-=+/,.<>", 7, "@x@", "@s@", complete = '(')
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
                println("get $y $x ${y.modulo(ab.size)} ${x.modulo(ab.size)}")
                return data[(y.modulo(ab.size) * ab.size) + x.modulo(ab.size)]
            }
            fun get(): String = data
            fun find(ch: Char): Pair<Int, Int> {
                val index = data.indexOf(ch)
                return Pair(index.rem(ab.size), index / ab.size)
            }
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
        fun find(ch: Char): Pair<Int, Int> = KeyBuf.find(ch)
    }
    fun crypt(instr: String, decrypt: Boolean = false): String {
        var str = Key.getAlphabet().trim(instr, !decrypt)
        if(str.length.rem(2) != 0) str += Key.getAlphabet().complete
        val list = str.chunked(2)
        val l = mutableListOf<String>()
        for(s in list) {
            var s0 = s[0]
            var s1 = s[1]
            val (x0, y0) = Key.find(s0)
            val (x1, y1) = Key.find(s1)
            if(x0 == x1 && y0 == y1) {
                s0 = when(decrypt) {
                    false -> Key.get(x0 + 1, y0 + 1)
                    true -> Key.get(x0 - 1, y0 - 1)
                }
                s1 = s0
            } else if(x0 == x1) {
                s0 = when(decrypt) {
                    false -> Key.get(x0, y0 + 1)
                    true -> Key.get(x0, y0 - 1)
                }
                s1 = when(decrypt) {
                    false -> Key.get(x0, y1 + 1)
                    true -> Key.get(x0, y1 - 1)
                }
            } else if(y0 == y1) {
                s0 = when(decrypt) {
                    false -> Key.get(x0 + 1, y0)
                    true -> Key.get(x0 - 1, y0)
                }
                s1 = when(decrypt) {
                    false -> Key.get(x1 + 1, y0)
                    true -> Key.get(x1 - 1, y0)
                }
            } else {
                s0 = Key.get(x1, y0)
                s1 = Key.get(x0, y1)
            }
            l.add("" + s0 + s1)
        }
        var s = l.joinToString("")
        if(decrypt) s = Key.getAlphabet().parseSpecial(s)
        return s
    }
}