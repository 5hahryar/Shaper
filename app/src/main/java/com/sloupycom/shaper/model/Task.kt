package com.sloupycom.shaper.model

data class Task(var creation_date: String? = ""
                ,var description: String? = ""
                ,var name: String? = ""
                ,var owner_id: String? = ""
                ,var reminder: String? = ""
                ,var repetition: String? = ""
                ,var state: String? = ""
                ,var id: String = ""
                ,var next_due: String? = "")