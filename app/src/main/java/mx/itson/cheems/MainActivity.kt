package mx.itson.cheems

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var gameOverCard = 0
    private var flippedCards = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.boton_restart).setOnClickListener {
            restartGame()
        }
        start()
    }

    fun start() {
        flippedCards = 0
        for (i in 1..6) {
            val btnCard = findViewById<View>(
                resources.getIdentifier("card$i", "id", this.packageName)
            ) as ImageButton
            btnCard.setOnClickListener(this)
            btnCard.setBackgroundResource(R.drawable.icon_pregunta)
            btnCard.isEnabled = true
        }
        gameOverCard = (1..6).random()
        Log.d("El valor de la carta", "La carta perdedora es ${gameOverCard}")
    }

    fun flip(card: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorAdmin = applicationContext.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            val vibrator = vibratorAdmin.defaultVibrator
            vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            val vibrator = applicationContext.getSystemService(Context.VIBRATOR_SERVICE)as Vibrator
        }

        //Para evitar que el usuario haga clic varias veces en la misma carta, se desactiva la carta después de voltearla
        val btnCard = findViewById<View>(
            resources.getIdentifier("card$card", "id", this.packageName)
        ) as ImageButton
        btnCard.isEnabled = false

        if (card == gameOverCard) {
            Toast.makeText(this, getString(R.string.text_game_over), Toast.LENGTH_SHORT).show()
            for (i in 1..6) {
                val btnCard = findViewById<View>(
                    resources.getIdentifier("card$i", "id", this.packageName)
                ) as ImageButton
                if (i == card) {
                    btnCard.setBackgroundResource(R.drawable.icon_chempe)
                } else {
                    btnCard.setBackgroundResource(R.drawable.icon_cheems)
                }
            }
        } else {
            btnCard.setBackgroundResource(R.drawable.icon_cheems)
            flippedCards++
            checkWin()
        }
    }
    //Verifica si el jugador ha volteado 5 cartas sin voltear la carta perdedora
    fun checkWin() {
        if (flippedCards == 5) {
            Toast.makeText(this, R.string.text_win, Toast.LENGTH_SHORT).show()
        }
    }

    //Se implementó la función restartGame(), que simplemente llama a start() para reiniciar
    fun restartGame() {
        start()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.card1 -> flip(1)
            R.id.card2 -> flip(2)
            R.id.card3 -> flip(3)
            R.id.card4 -> flip(4)
            R.id.card5 -> flip(5)
            R.id.card6 -> flip(6)
        }
    }
}
