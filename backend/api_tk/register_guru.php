<?php
// register_guru.php - FIXED SECURITY & DATABASE INSERT
include 'koneksi.php'; 

header('Content-Type: application/json');

$fullname = $_POST['fullname'] ?? '';
$email    = $_POST['email'] ?? '';
$password = $_POST['password'] ?? '';
$jabatan = '-'; // Default
$nisn = '-'; // Default
$alamat = '-'; // Default

if (empty($fullname) || empty($email) || empty($password)) {
    echo json_encode(['success' => false, 'message' => 'Nama, Email, dan Password wajib diisi!']);
    exit();
}

$stmt_check = $koneksi->prepare("SELECT id FROM guru WHERE email = ?");
$stmt_check->bind_param("s", $email);
$stmt_check->execute();
$result_check = $stmt_check->get_result();

if ($result_check->num_rows > 0) {
    echo json_encode(['success' => false, 'message' => 'Email sudah terdaftar!']);
    $stmt_check->close();
    exit();
}
$stmt_check->close();


$hashed_password = password_hash($password, PASSWORD_DEFAULT);

$sql = "INSERT INTO guru (fullname, email, password, jabatan, nisn, alamat) 
        VALUES (?, ?, ?, ?, ?, ?)";

$stmt = $koneksi->prepare($sql);
$stmt->bind_param("ssssss", $fullname, $email, $hashed_password, $jabatan, $nisn, $alamat);

if ($stmt->execute()) {
    echo json_encode(['success' => true, 'message' => 'Registrasi Berhasil! Silakan Login.']);
} else {
    echo json_encode(['success' => false, 'message' => 'Gagal DB: ' . $stmt->error]); // Pakai $stmt->error untuk debug
}

$stmt->close();
// mysqli_close($koneksi) tidak perlu jika $koneksi->close() dipanggil di akhir skrip
?>