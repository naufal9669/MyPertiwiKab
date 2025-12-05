<?php
// get_izin_status.php (VERSION: FORCE CHECK)
include 'koneksi.php';
date_default_timezone_set('Asia/Jakarta');

header('Content-Type: application/json');

if (!isset($_GET['guru_id'])) {
    echo json_encode(['status' => 'error', 'message' => 'Butuh guru_id']);
    exit();
}

$guru_id = $_GET['guru_id'];
$tanggal_hari_ini = date("Y-m-d");

// QUERY DIPERBAIKI:
// Kita cari data di tabel pengajuan_izin
// Dimana guru_id cocok DAN tanggal_izin cocok
$query = "SELECT * FROM pengajuan_izin WHERE guru_id = ? AND tanggal_izin = ?";

$stmt = $koneksi->prepare($query);
$stmt->bind_param("is", $guru_id, $tanggal_hari_ini);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows > 0) {
    // KUNCINYA: Kalau ada datanya (baris > 0), berarti dia IZIN.
    // Kita kirim status "Diterima" biar Android langsung nangkep.
    $status_izin = "Diterima";
} else {
    $status_izin = "Tidak Ada";
}

echo json_encode([
    'status' => 'success',
    'izin_status' => $status_izin,
    'debug_tanggal' => $tanggal_hari_ini // Buat ngecek di log kalau perlu
]);

$stmt->close();
$koneksi->close();
?>