<?php
// verify_otp.php
include 'koneksi.php';
date_default_timezone_set('Asia/Jakarta');

header('Content-Type: application/json');

// Ambil data
$guru_id = $_POST['guru_id'] ?? 0;
$otp_code = $_POST['otp_code'] ?? '';

if (empty($guru_id) || empty($otp_code)) {
    http_response_code(400);
    echo json_encode(['status' => 'error', 'message' => 'Data verifikasi tidak lengkap.']);
    exit();
}

// 1. Cari token yang cocok dan belum expired
$query = "SELECT token FROM otp_tokens WHERE guru_id = ? AND token = ? AND expires_at > NOW()";
$stmt = $koneksi->prepare($query);
$stmt->bind_param("is", $guru_id, $otp_code);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows > 0) {
    // 2. TOKEN COCOK & BELUM EXPIRED
    
    // Hapus token setelah berhasil verifikasi
    $stmt_delete = $koneksi->prepare("DELETE FROM otp_tokens WHERE guru_id = ? AND token = ?");
    $stmt_delete->bind_param("is", $guru_id, $otp_code);
    $stmt_delete->execute();
    
    echo json_encode(['status' => 'success', 'message' => 'Verifikasi berhasil. Silakan atur password baru.', 'guru_id' => $guru_id]);
    http_response_code(200);
} else {
    // 3. GAGAL (Token salah atau sudah kadaluarsa)
    echo json_encode(['status' => 'error', 'message' => 'Kode OTP salah atau sudah kadaluarsa.']);
    http_response_code(401); // Unauthorized
}

$stmt->close();
$koneksi->close();
?>