package uqac.dim.xtremclicker.sound

import android.content.Context
import android.media.MediaPlayer
import uqac.dim.xtremclicker.R

class SoundsManager(applicationContext: Context) {
    private val clickSounds: List<MediaPlayer> = listOf(
        MediaPlayer.create(applicationContext, R.raw.yeah),
        MediaPlayer.create(applicationContext, R.raw.cool),
        MediaPlayer.create(applicationContext, R.raw.all_right)
    )

    private val musicLoop: MediaPlayer =
        MediaPlayer.create(applicationContext, R.raw.burn_the_world_waltz).apply {
            isLooping = true
        }

    fun playRandomClickSound() {
        clickSounds.random().start()
    }

    fun startMusic() {
        musicLoop.start()
    }

    fun pauseMusic() {
        musicLoop.pause()
    }
}