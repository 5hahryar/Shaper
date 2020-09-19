package com.sloupycom.shaper.dagger

import com.sloupycom.shaper.model.Repo
import com.sloupycom.shaper.utils.Util
import dagger.Component

@Component
interface DependencyComponent {
    fun getUtil(): Util
    fun getRepo(): Repo
}