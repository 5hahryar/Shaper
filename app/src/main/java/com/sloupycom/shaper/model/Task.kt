package com.sloupycom.shaper.model

data class Task(var creation_date: String?
                ,var description: String?
                ,var name: String?
                ,var next_due_day: String?
                ,var next_due_month: String?
                ,var next_due_year: String?
                ,var owner_id: String?
                ,var reminder: String?
                ,var repetition: String?
                ,var state: String?
                ,var id: String) {

}