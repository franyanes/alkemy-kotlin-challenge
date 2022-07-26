package com.example.moviecatalog.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.moviecatalog.R
import com.example.moviecatalog.ui.movie_details.MovieDetailsActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Placeholder to test the API
        findViewById<Button>(R.id.btn).setOnClickListener {
            val intent = Intent(this, MovieDetailsActivity::class.java)
            intent.putExtra("id", 299534)
            this.startActivity(intent)
        }
    }
}
