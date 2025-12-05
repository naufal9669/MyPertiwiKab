<?php
// pengajuan_izin.php (SIMPLE VERSION)
include 'koneksi.php';

// Header JSON
header('Content-Type: application/json');

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    http_response_code(405);
    echo json_encode(['status' => 'error', 'message' => 'Metode salah.']);
    exit();
}

// Ambil Data JSON dari Android
$input = file_get_contents("php://input");
$data = json_decode($input, true);

// Validasi
if (!isset($data['guru_id']) || !isset($data['tanggal_izin']) || !isset($data['jenis_izin']) || !isset($data['deskripsi'])) {
    http_response_code(400);
    echo json_encode(['status' => 'error', 'message' => 'Data tidak lengkap.']);
    exit();
}

$guru_id = $data['guru_id'];
$tanggal_izin = $data['tanggal_izin'];
$jenis_izin = $data['jenis_izin'];
$deskripsi = $data['deskripsi'];

// QUERY INSERT (Tanpa Status)
$stmt = $koneksi->prepare("INSERT INTO pengajuan_izin (guru_id, tanggal_izin, jenis_izin, deskripsi) VALUES (?, ?, ?, ?)");
$stmt->bind_param("isss", $guru_id, $tanggal_izin, $jenis_izin, $deskripsi);

if ($stmt->execute()) {
    $response = array(
        'status' => 'success',
        'message' => 'Izin berhasil dikirim.'
    );
    http_response_code(201);
} else {
    $response = array(
        'status' => 'error', 
        'message' => 'Gagal DB: ' . $stmt->error
    );
    http_response_code(500);
}

$stmt->close();
$koneksi->close();
echo json_encode($response);
?>