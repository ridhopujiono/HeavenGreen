package com.apps.heavengreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RelativeLayout
import com.apps.heavengreen.application.HeavenGreenDatabase
import com.apps.heavengreen.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding;
    private lateinit var sellTrashMenu: RelativeLayout

    private lateinit var heavenGreenDatabase: HeavenGreenDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        heavenGreenDatabase = HeavenGreenDatabase.getDatabase(this)


        var binding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.sellTrashMenu.setOnClickListener {
            var intent = Intent(this, SellTrashActivity::class.java)
            startActivity(intent)
        }

        binding.historiesMenu.setOnClickListener {
            var intent = Intent(this, TrashTransactionHistories::class.java)
            startActivity(intent)
        }
    }
}