package com.example.laboratorio2matialv

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.PlaybackParams
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

@Suppress("DEPRECATION")
class AudioActivity : AppCompatActivity() {

    private val audioDataList = mutableListOf<AudioData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.audio_samples)

        audioDataList.add(
            AudioData(
                R.raw.gamemusic,
                findViewById(R.id.seekBar1),
                findViewById(R.id.playButton1),
                findViewById(R.id.pauseButton1),
                findViewById(R.id.stopButton1),
                findViewById(R.id.speedUpButton1),
                findViewById(R.id.speedDownButton1),
                findViewById(R.id.textTitle1),
                "Background Music"
            )
        )
        audioDataList.add(
            AudioData(
                R.raw.gameover,
                findViewById(R.id.seekBar2),
                findViewById(R.id.playButton2),
                findViewById(R.id.pauseButton2),
                findViewById(R.id.stopButton2),
                findViewById(R.id.speedUpButton2),
                findViewById(R.id.speedDownButton2),
                findViewById(R.id.textTitle2),
                "Defeat"
            )
        )
        audioDataList.add(
            AudioData(
                R.raw.gamewin,
                findViewById(R.id.seekBar3),
                findViewById(R.id.playButton3),
                findViewById(R.id.pauseButton3),
                findViewById(R.id.stopButton3),
                findViewById(R.id.speedUpButton3),
                findViewById(R.id.speedDownButton3),
                findViewById(R.id.textTitle3),
                "Victory"
            )
        )

        val goBackButton = findViewById<Button>(R.id.goBackButton)

        goBackButton.setOnClickListener {
            stopAllAudioPlayback()
            onBackPressed()
        }

        audioDataList.forEach { audioData ->
            initializeAudioControls(audioData)
        }
    }

    private fun initializeAudioControls(audioData: AudioData) {
        val mediaPlayer = MediaPlayer.create(this, audioData.rawResourceId)
        audioData.seekBar.max = mediaPlayer.duration

        audioData.titleView.text = audioData.title

        audioData.playButton.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
                mediaPlayer.prepare()
                mediaPlayer.seekTo(0)
                audioData.pauseButton.text = "Continue"
            } else {
                mediaPlayer.playbackParams = PlaybackParams().setSpeed(1.0f)
                mediaPlayer.start()
                updateSeekBar(mediaPlayer, audioData.seekBar)
                audioData.pauseButton.text = "Pause"
            }
        }

        audioData.pauseButton.setOnClickListener {
            if (mediaPlayer != null) {
                if (audioData.isPaused && !mediaPlayer.isPlaying) {
                    mediaPlayer.start()
                    audioData.pauseButton.text = "Pause"
                    audioData.isPaused = false
                    updateSeekBar(mediaPlayer, audioData.seekBar)
                } else if (!audioData.isPaused && mediaPlayer.isPlaying) {
                    mediaPlayer.pause()
                    audioData.pauseButton.text = "Resume"
                    audioData.isPaused = true
                }
            }
        }


        audioData.stopButton.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
                mediaPlayer.prepare()
                mediaPlayer.seekTo(0)
                audioData.seekBar.progress = 0
                audioData.pauseButton.text = "Resume"
                audioData.isPaused = false
            }
        }

        audioData.speedUpButton.setOnClickListener {
            val params = mediaPlayer.playbackParams
            params.setSpeed(params.speed * 1.5f)
            mediaPlayer.playbackParams = params
        }

        audioData.speedDownButton.setOnClickListener {
            val params = mediaPlayer.playbackParams
            params.setSpeed(params.speed * 0.5f)
            mediaPlayer.playbackParams = params
        }

        audioData.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun updateSeekBar(mediaPlayer: MediaPlayer?, seekBar: SeekBar?) {
        if (mediaPlayer != null && seekBar != null) {
            val currentPosition = mediaPlayer.currentPosition
            seekBar.progress = currentPosition
            if (mediaPlayer.isPlaying) {
                seekBar.postDelayed({ updateSeekBar(mediaPlayer, seekBar) }, 100)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        audioDataList.forEach { audioData ->
            audioData.mediaPlayer?.release()
        }
    }

    override fun onBackPressed() {
        audioDataList.forEach { audioData ->
            val mediaPlayer = audioData.mediaPlayer
            if (mediaPlayer != null && (mediaPlayer.isPlaying || audioData.isPaused)) {
                mediaPlayer.stop()
                mediaPlayer.reset()
                mediaPlayer.release()
            }
        }
        super.onBackPressed()
    }

    private fun stopAllAudioPlayback() {
        audioDataList.forEach { audioData ->
            val mediaPlayer = audioData.mediaPlayer
            mediaPlayer?.stop()
            mediaPlayer?.reset()
            mediaPlayer?.release()
        }
    }

    data class AudioData(
        val rawResourceId: Int,
        val seekBar: SeekBar,
        val playButton: Button,
        val pauseButton: Button,
        val stopButton: Button,
        val speedUpButton: Button,
        val speedDownButton: Button,
        val titleView: TextView,
        val title: String
    ) {
        var mediaPlayer: MediaPlayer? = null
        var isPaused = false
    }
}