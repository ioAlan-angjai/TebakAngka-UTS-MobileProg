
package com.example.tebakangkauts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

// Activity utama yang menampilkan aplikasi Tebak Angka.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Menampilkan UI utama menggunakan Jetpack Compose.
        setContent {
            MaterialTheme {
                TebakAngkaApp()
            }
        }
    }
}

// Konstanta warna yang digunakan pada tampilan aplikasi.
private val Hijau = Color(0xFF276749)
private val Latar = Color(0xFFF3F7F4)
private val TeksGelap = Color(0xFF20352A)

// Fungsi utama yang mengatur tampilan dan logika permainan Tebak Angka.
@Composable
fun TebakAngkaApp() {

    // Menyimpan angka rahasia acak dari 0 sampai 100.
    var angkaRahasia by remember {
        mutableIntStateOf(Random.nextInt(0, 101))
    }

    // Menyimpan skor pemain yang dimulai dari 100.
    var skor by remember { mutableIntStateOf(100) }

    // Menyimpan angka yang dimasukkan oleh pemain.
    var inputAngka by remember { mutableStateOf("") }

    // Menyimpan pesan petunjuk dan hasil permainan.
    var pesan by remember {
        mutableStateOf("Masukkan angka antara 0 sampai 100.")
    }

    // Menandai apakah permainan sudah selesai.
    var permainanSelesai by remember {
        mutableStateOf(false)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Latar
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Menampilkan judul dan instruksi permainan.
            Text(
                text = "TEBAK ANGKA",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Hijau
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Bisakah kamu menemukan angka rahasiaku?",
                fontSize = 14.sp,
                color = TeksGelap,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Kartu yang menampung skor, input, tombol, dan petunjuk.
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // Menampilkan skor pemain saat ini.
                    Text(
                        text = "SKOR KAMU",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = TeksGelap
                    )

                    Text(
                        text = skor.toString(),
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = Hijau
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Input tebakan dengan validasi karakter angka.
                    OutlinedTextField(
                        value = inputAngka,
                        onValueChange = { nilai ->
                            if (
                                nilai.length <= 3 &&
                                nilai.all { it.isDigit() }
                            ) {
                                inputAngka = nilai
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Angka tebakan (0-100)") },
                        placeholder = { Text("Contoh: 50") },
                        singleLine = true,
                        enabled = !permainanSelesai,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Memeriksa tebakan, memberikan petunjuk,
                    // dan mengurangi skor jika tebakan salah.
                    Button(
                        onClick = {
                            val tebakan = inputAngka.toIntOrNull()

                            when {
                                tebakan == null -> {
                                    pesan = "Masukkan angka terlebih dahulu."
                                }

                                tebakan !in 0..100 -> {
                                    pesan = "Angka harus antara 0 dan 100."
                                }

                                tebakan == angkaRahasia -> {
                                    pesan = "Benar! Kamu berhasil menebak!"
                                    permainanSelesai = true
                                }

                                else -> {
                                    skor = (skor - 10).coerceAtLeast(0)

                                    pesan = if (tebakan < angkaRahasia) {
                                        "Terlalu kecil! Coba angka lebih besar."
                                    } else {
                                        "Terlalu besar! Coba angka lebih kecil."
                                    }

                                    if (skor == 0) {
                                        pesan =
                                            "Kesempatan skor habis! Angka rahasia: $angkaRahasia"
                                        permainanSelesai = true
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        enabled = !permainanSelesai,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Hijau
                        )
                    ) {
                        Text(
                            text = "TEBAK SEKARANG",
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Menampilkan petunjuk atau hasil tebakan.
                    Text(
                        text = pesan,
                        fontSize = 15.sp,
                        color = TeksGelap,
                        textAlign = TextAlign.Center
                    )

                    // Menampilkan tombol untuk mengulang permainan
                    // setelah pemain menang atau skor mencapai nol.
                    if (permainanSelesai) {
                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                // Mengatur ulang angka, skor, input,
                                // pesan, dan status permainan.
                                angkaRahasia = Random.nextInt(0, 101)
                                skor = 100
                                inputAngka = ""
                                pesan =
                                    "Permainan baru dimulai! Tebak angkanya."
                                permainanSelesai = false
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF426A8C)
                            )
                        ) {
                            Text("MAIN LAGI")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Menampilkan rentang angka yang dapat ditebak.
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Rentang angka: 0 - 100",
                    color = TeksGelap,
                    fontSize = 13.sp
                )
            }
        }
    }
}
