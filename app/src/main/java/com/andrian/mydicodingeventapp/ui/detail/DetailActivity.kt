package com.andrian.mydicodingeventapp.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.andrian.mydicodingeventapp.R
import com.andrian.mydicodingeventapp.data.Result
import com.andrian.mydicodingeventapp.data.local.FavoriteEvent
import com.andrian.mydicodingeventapp.data.response.DetailEventResponse
import com.andrian.mydicodingeventapp.databinding.ActivityDetailBinding
import com.andrian.mydicodingeventapp.utils.ViewModelFactory
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {

    private lateinit var detailViewModel: DetailViewModel
    private lateinit var binding: ActivityDetailBinding
    private var isFavorite = false

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#2d3e50")))
        supportActionBar?.title = "Detail"

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        detailViewModel = ViewModelProvider(this, factory)[DetailViewModel::class.java]

        val eventId = intent.getStringExtra("eventId")
        Log.d("DetailActivity", "Event ID: $eventId")

        if (eventId != null) {
            detailViewModel.getEventDetail(eventId)
            detailViewModel.checkIfFavorite(eventId)
        }

        detailViewModel.event.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    showLoading(isLoading = false)
                    setEventData(result.data)
                    val link = result.data.event?.link
                    findViewById<MaterialButton>(R.id.fab_register).setOnClickListener {
                        registerEvent(link)
                    }
                }

                is Result.Error -> {
                    showLoading(isLoading = true)
                    binding.name.text = "Gagal memuat detail event"
                }

                is Result.Loading -> showLoading(isLoading = true)
            }
        }

        detailViewModel.isFavorite.observe(this) { isFav ->
            isFavorite = isFav
            updateFavoriteIcon(isFavorite)
        }

        binding.actionFavorite.setOnClickListener {
            isFavorite = !isFavorite
            updateFavoriteIcon(isFavorite)
            if (isFavorite) Toast.makeText(this, "Telah ditambahkan di Favorite", Toast.LENGTH_SHORT).show()
            else Toast.makeText(this, "Telah dihapus dari Favorite", Toast.LENGTH_SHORT).show()
            eventId?.let { id ->
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        if (isFavorite) {
                            val eventData = detailViewModel.event.value
                            if (eventData is Result.Success) {

                                val favoriteEvent = FavoriteEvent(
                                    id = id,
                                    name = eventData.data.event?.name ?: "",
                                    mediaCover = eventData.data.event?.mediaCover
                                )
                                detailViewModel.saveFavorite(favoriteEvent)
                            }
                        } else {
                            val event = FavoriteEvent(id = id, name = binding.name.text.toString())
                            detailViewModel.deleteFavorite(event)
                        }
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        with(binding) {
            headerImage.visibility = if (isLoading) View.GONE else View.VISIBLE
            name.visibility = if (isLoading) View.GONE else View.VISIBLE
            description.visibility = if (isLoading) View.GONE else View.VISIBLE
            sisaKuota.visibility = if (isLoading) View.GONE else View.VISIBLE
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            actionFavorite.visibility = if (isLoading) View.GONE else View.VISIBLE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setEventData(event: DetailEventResponse) {
        Glide.with(this)
            .load(event.event?.mediaCover)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_foreground)
            .into(binding.headerImage)

        binding.name.text = event.event?.name ?: "Nama event tidak tersedia"
        binding.description.text = HtmlCompat.fromHtml(
            event.event?.description ?: "",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )

        val quota = event.event?.quota
        val registrant = event.event?.registrants
        if (quota != null && registrant != null) {
            binding.sisaKuota.text = "Sisa Kuota: " + (quota - registrant).toString()
        }
        binding.beginTime.text = "Waktu: " + event.event?.beginTime
        binding.ownername.text = "Diselenggarakan Oleh: " + event.event?.ownerName
    }

    private fun updateFavoriteIcon(isFavorite: Boolean) {
        binding.actionFavorite.setImageResource(
            if (isFavorite) R.drawable.baseline_favorite_24 else R.drawable.baseline_favorite_border_24
        )
    }

    private fun registerEvent(link: String?) {
        if (link != null) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.btn_back -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
