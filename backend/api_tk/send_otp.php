<?php
// send_otp.php - FINAL LOGIC (4 DIGIT OTP & GURU ID)
include 'koneksi.php';
date_default_timezone_set('Asia/Jakarta');

header('Content-Type: application/json');

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    http_response_code(405);
    echo json_encode(['status' => 'error', 'message' => 'Metode tidak diizinkan.']);
    exit();
}

$email = $_POST['email'] ?? '';

if (empty($email)) {
    http_response_code(400);
    echo json_encode(['status' => 'error', 'message' => 'Email wajib diisi.']);
    exit();
}

// 1. Cek Email di Database Guru
$stmt_check = $koneksi->prepare("SELECT id, fullname FROM guru WHERE email = ?");
$stmt_check->bind_param("s", $email);
$stmt_check->execute();
$result_check = $stmt_check->get_result();

if ($result_check->num_rows === 0) {
    http_response_code(404);
    echo json_encode(['status' => 'error', 'message' => 'Email tidak terdaftar.']);
    exit();
}

$guru_data = $result_check->fetch_assoc();
$guru_id = $guru_data['id'];
$fullname = $guru_data['fullname'];

// 2. GENERATE OTP (FIXED: 4 digit untuk tampilan Android)
$otp_code = rand(1000, 9999); 
$expires_at = date('Y-m-d H:i:s', time() + (5 * 60)); // Expired dalam 5 menit

// 3. Hapus token lama
$stmt_delete = $koneksi->prepare("DELETE FROM otp_tokens WHERE guru_id = ?");
$stmt_delete->bind_param("i", $guru_id);
$stmt_delete->execute();
$stmt_delete->close();

// 4. Simpan Token Baru ke Database
$stmt_insert = $koneksi->prepare("INSERT INTO otp_tokens (guru_id, token, expires_at) VALUES (?, ?, ?)");
$stmt_insert->bind_param("iss", $guru_id, $otp_code, $expires_at);
$stmt_insert->execute();
$stmt_insert->close();

// 5. KIRIM RESPON KE ANDROID
// CATATAN: Pesan di sini akan muncul di Toast HP kamu.
$response = [
    'status' => 'success',
    'message' => 'Kode OTP berhasil dikirim ke email Anda. (Kode: ' . $otp_code . ')',
    'guru_id' => $guru_id 
];
http_response_code(200);
echo json_encode($response);
?>