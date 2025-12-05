<?php
// absensi.php - FINAL VERSION (Fixed Logic)
include 'koneksi.php';
date_default_timezone_set('Asia/Jakarta'); 

// 1. Cek Metode & Ambil Data
if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    http_response_code(405);
    echo json_encode(['status' => 'error', 'message' => 'Metode tidak diizinkan.']);
    exit();
}

$data = json_decode(file_get_contents("php://input"), true);

if (!isset($data['guru_id']) || !isset($data['tipe'])) {
    http_response_code(400);
    echo json_encode(['status' => 'error', 'message' => 'Data tidak lengkap.']);
    exit();
}

$guru_id = $data['guru_id'];
$tipe = strtolower($data['tipe']); // datang, pulang, atau alfa
$tanggal_hari_ini = date("Y-m-d");
$jam_sekarang = date("H:i:s");

// 2. Cek Apakah Sudah Ada Data Hari Ini?
$check_stmt = $koneksi->prepare("SELECT id, jam_datang, jam_pulang FROM absensi WHERE guru_id = ? AND tanggal = ?");
$check_stmt->bind_param("is", $guru_id, $tanggal_hari_ini);
$check_stmt->execute();
$result = $check_stmt->get_result();
$data_absensi = $result->fetch_assoc();
$check_stmt->close();


// ==========================================
//  LOGIKA UTAMA
// ==========================================

// --- A. JIKA TIPE = DATANG ---
if ($tipe == 'datang') {
    if ($data_absensi) {
        // Sudah ada data
        $response = array('status' => 'error', 'message' => 'Anda sudah absen hari ini.');
        http_response_code(409);
    } else {
        // Belum ada data -> INSERT BARU
        // Tentukan status dasar: Jika lewat jam 07:00 dianggap Terlambat (Opsional, bisa diatur)
        // Disini kita set default 'Hadir' dulu, biar logic di Android yang nentuin visualnya
        $status_awal = "Hadir"; 
        
        $insert_stmt = $koneksi->prepare("INSERT INTO absensi (guru_id, tanggal, jam_datang, status) VALUES (?, ?, ?, ?)");
        $insert_stmt->bind_param("isss", $guru_id, $tanggal_hari_ini, $jam_sekarang, $status_awal);
        
        if ($insert_stmt->execute()) {
            $response = array('status' => 'success', 'message' => 'Absen Datang Berhasil.');
            http_response_code(201);
        } else {
            $response = array('status' => 'error', 'message' => 'Gagal DB: ' . $insert_stmt->error);
            http_response_code(500);
        }
        $insert_stmt->close();
    }

// --- B. JIKA TIPE = PULANG ---
} elseif ($tipe == 'pulang') {
    if (!$data_absensi) {
        $response = array('status' => 'error', 'message' => 'Belum absen datang.');
        http_response_code(409);
    } elseif ($data_absensi['jam_pulang']) {
         $response = array('status' => 'error', 'message' => 'Sudah absen pulang.');
         http_response_code(409);
    } else {
        // UPDATE PULANG
        $update_stmt = $koneksi->prepare("UPDATE absensi SET jam_pulang = ? WHERE guru_id = ? AND tanggal = ?");
        $update_stmt->bind_param("sis", $jam_sekarang, $guru_id, $tanggal_hari_ini);

        if ($update_stmt->execute()) {
            $response = array('status' => 'success', 'message' => 'Absen Pulang Berhasil.');
            http_response_code(200);
        } else {
            $response = array('status' => 'error', 'message' => 'Gagal Update DB: ' . $update_stmt->error);
            http_response_code(500);
        }
        $update_stmt->close();
    }

// --- C. JIKA TIPE = ALFA (DARI ANDROID) ---
} elseif ($tipe == 'alfa') {
    if ($data_absensi) {
        // Jika SUDAH ADA data (entah itu Hadir, Terlambat, atau Izin), JANGAN DITIMPA jadi ALFA.
        // Kita biarkan saja data yang sudah ada.
        $response = array('status' => 'success', 'message' => 'Data sudah ada (Tidak perlu ALFA).');
        http_response_code(200);
    } else {
        // Jika BELUM ADA data sama sekali -> INSERT ALFA
        // Kita set jam_datang dengan jam saat ini (sebagai penanda kapan sistem mencatat alfa)
        $status_alfa = "ALFA";
        
        $insert_stmt = $koneksi->prepare("INSERT INTO absensi (guru_id, tanggal, jam_datang, status) VALUES (?, ?, ?, ?)");
        $insert_stmt->bind_param("isss", $guru_id, $tanggal_hari_ini, $jam_sekarang, $status_alfa);
        
        if ($insert_stmt->execute()) {
            $response = array('status' => 'success', 'message' => 'Status ALFA berhasil dicatat.');
            http_response_code(201);
        } else {
            $response = array('status' => 'error', 'message' => 'Gagal Insert ALFA: ' . $insert_stmt->error);
            http_response_code(500);
        }
        $insert_stmt->close();
    }

} else {
    $response = array('status' => 'error', 'message' => 'Tipe tidak valid.');
    http_response_code(400);
}

$koneksi->close();
echo json_encode($response);
?>