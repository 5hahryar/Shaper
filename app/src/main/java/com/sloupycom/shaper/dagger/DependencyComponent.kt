package com.sloupycom.shaper.dagger

import com.sloupycom.shaper.utils.Util
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component
interface DependencyComponent {
    fun getUtil(): Util
}