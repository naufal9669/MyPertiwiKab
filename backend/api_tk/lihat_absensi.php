<?php
// lihat_absensi.php
include 'koneksi.php';

if ($_SERVER['REQUEST_METHOD'] !== 'GET') {
    http_response_code(405);
    echo json_encode(['status' => 'error', 'message' => 'Metode tidak diizinkan.']);
    exit();
}

if (!isset($_GET['guru_id'])) {
    http_response_code(400);
    echo json_encode(['status' => 'error', 'message' => 'guru_id wajib diisi.']);
    exit();
}

$guru_id = $_GET['guru_id'];

// Ambil riwayat absensi
$stmt = $koneksi->prepare("SELECT tanggal, jam_datang, jam_pulang FROM absensi WHERE guru_id = ? ORDER BY tanggal DESC LIMIT 30");
$stmt->bind_param("i", $guru_id);
$stmt->execute();
$result = $stmt->get_result();

$riwayat = [];
while ($row = $result->fetch_assoc()) {
    // Format tanggal ke format Indonesia untuk tampilan
    $row['tanggal'] = date('d M Y', strtotime($row['tanggal'])); 
    $riwayat[] = $row;
}

if (!empty($riwayat)) {
    $response = array(
        'status' => 'success',
        'message' => 'Riwayat absensi berhasil diambil.',
        'data' => $riwayat
    );
    http_response_code(200);
} else {
    $response = array('status' => 'error', 'message' => 'Tidak ada riwayat absensi ditemukan.');
    http_response_code(404);
}

$stmt->close();
$koneksi->close();

echo json_encode($response);
?>