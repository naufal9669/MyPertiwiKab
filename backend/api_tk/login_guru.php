<?php
// login_guru.php - FIXED SECURITY & VERIFICATION
include 'koneksi.php';

header('Content-Type: application/json');

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    http_response_code(405);
    echo json_encode(['status' => 'error', 'message' => 'Metode tidak diizinkan.']);
    exit();
}

$data = json_decode(file_get_contents("php://input"), true);

if (!isset($data['email']) || !isset($data['password'])) {
    http_response_code(400);
    echo json_encode(['status' => 'error', 'message' => 'Email dan password wajib diisi.']);
    exit();
}

$email = $data['email'];
$password = $data['password'];

$stmt = $koneksi->prepare("SELECT id, fullname, email, password, jabatan FROM guru WHERE email = ?");
$stmt->bind_param("s", $email);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows === 1) {
    $guru = $result->fetch_assoc();
    
    if (password_verify($password, $guru['password'])) {
        // Login berhasil
        $response = array(
            'status' => 'success',
            'message' => 'Login berhasil.',
            'guru' => [
                'id' => $guru['id'],
                'fullname' => $guru['fullname'],
                'email' => $guru['email'],
                'jabatan' => $guru['jabatan']
            ]
        );
        http_response_code(200);
    } else {
        // Password salah
        $response = array('status' => 'error', 'message' => 'Email atau Password salah.');
        http_response_code(401);
    }
} else {
    // Email tidak ditemukan
    $response = array('status' => 'error', 'message' => 'Email atau Password salah.');
    http_response_code(401);
}

$stmt->close();
$koneksi->close();

echo json_encode($response);
?>