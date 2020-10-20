package com.example.szyfry0

object Playfair {
    object Key {
        data class Alphabet (val descryption: String, val alphabet: String, val hexPrefix: String) {
            fun trim(str: String): String {
                var s = str
                if(!alphabet.contains(' ') && hexPrefix.isEmpty()) s = s.replace(" ", "")
                if(hexPrefix.isNotEmpty()) {
                    s = s.replace("[$alphabet]".toRegex(), {
                        hexPrefix + it.value[0]
                    })
                }
                return s
            }
        }
        var alphabets = listOf(Alphabet("25 Podstawowy alfabet bez 'j'", "abcdefghiklmnopqrstuvwxyz", ""),
            Alphabet("36 Rozszerzony alfabet z cyframi", "abcdefghijklmnopqrstuvwxyz0123456789", "0xx"),
            Alphabet("49 Rozszerzony alfabet z cyframi i symbolami", "abcdefghijklmnopqrstuvwxyz0123456789!@*()-=+/,.<>", "*x*")
        )
    }
}