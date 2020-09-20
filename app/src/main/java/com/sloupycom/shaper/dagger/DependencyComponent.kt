package com.sloupycom.shaper.dagger

import com.sloupycom.shaper.model.Repo
import com.sloupycom.shaper.utils.Util
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component
interface DependencyComponent {
    fun getUtil(): Util
    fun getRepo(): Repo
}