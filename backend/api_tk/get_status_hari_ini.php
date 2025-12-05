<?php
// get_status_hari_ini.php (AUTO ALFA VERSION)
include 'koneksi.php';
date_default_timezone_set('Asia/Jakarta');

$guru_id = $_GET['guru_id'];
$tanggal_hari_ini = date("Y-m-d");
$jam_sekarang_int = (int)date("H"); // Ambil jam format angka (misal 13, 14)

// 1. Cek Data Absensi di Database
$query = "SELECT * FROM absensi WHERE guru_id = '$guru_id' AND tanggal = '$tanggal_hari_ini'";
$result = mysqli_query($koneksi, $query);
$data = mysqli_fetch_assoc($result);

$status_datang = "Belum Absen";
$status_pulang = "Belum Absen";

if ($data) {
    // --- SKENARIO A: DATA SUDAH ADA ---
    
    // Cek Status Datang
    if ($data['status'] == 'ALFA') {
        $status_datang = "ALFA (Terlewat)";
        $status_pulang = "Menunggu Pulang";
    } elseif ($data['status'] == 'Izin') { // Jika kamu punya status Izin di DB
        $status_datang = "IZIN";
        $status_pulang = "IZIN";
    } elseif ($data['jam_datang']) {
        // Logika Terlambat/Hadir (Misal batas jam 7)
        $jam_datang_db = strtotime($data['jam_datang']);
        $batas_jam = strtotime("07:00:00"); 
        
        if ($jam_datang_db > $batas_jam) {
            $status_datang = "Terlambat (" . substr($data['jam_datang'], 0, 5) . ")";
        } else {
            $status_datang = "Hadir (" . substr($data['jam_datang'], 0, 5) . ")";
        }
    }

    // Cek Status Pulang
    if ($data['jam_pulang']) {
        $status_pulang = "Pulang (" . substr($data['jam_pulang'], 0, 5) . ")";
    } else {
        // Jika status Datang itu ALFA, pulangnya tetap "Menunggu"
        if ($data['status'] == 'ALFA') {
            $status_pulang = "Menunggu Pulang";
        } else {
            $status_pulang = "Menunggu Pulang";
        }
    }

} else {
    // --- SKENARIO B: DATA KOSONG (BELUM ABSEN) ---
    
    // DISINI KUNCI ALTERNATIFNYA!
    // Jika data kosong DAN Jam sekarang >= 13 (siang)
    if ($jam_sekarang_int >= 13) {
        
        // 1. OTOMATIS INSERT ALFA KE DATABASE
        // Jadi Android gak perlu kirim request apa-apa, PHP yang inisiatif.
        $jam_sql = date("H:i:s");
        $insert = "INSERT INTO absensi (guru_id, tanggal, jam_datang, status) VALUES ('$guru_id', '$tanggal_hari_ini', '$jam_sql', 'ALFA')";
        
        if (mysqli_query($koneksi, $insert)) {
            $status_datang = "ALFA (Terlewat)";
            $status_pulang = "Menunggu Pulang";
        } else {
            $status_datang = "Error Server";
        }
        
    } else {
        // Masih pagi/belum jam 13, jadi status normal
        $status_datang = "Belum Absen";
        $status_pulang = "Belum Absen";
    }
}

// Kirim JSON ke Android
echo json_encode([
    'status_datang' => $status_datang,
    'status_pulang' => $status_pulang
]);
?>