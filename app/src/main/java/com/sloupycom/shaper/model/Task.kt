package com.sloupycom.shaper.model

data class Task(var id: String = "",
                var owner_id: String = "",
                var name: String = "",
                var description: String? = "",
                var creation_date: String = "",
                var next_due: List<Int> = listOf(30, 12, 2400),
                var state: String = ""
)