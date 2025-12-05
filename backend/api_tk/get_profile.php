<?php
// get_profile.php
include 'koneksi.php'; // WAJIB ada dan bekerja

// Pastikan request method adalah GET
if ($_SERVER['REQUEST_METHOD'] !== 'GET') {
    http_response_code(405);
    echo json_encode(['status' => 'error', 'message' => 'Metode tidak diizinkan.']);
    exit();
}

// Cek apakah guru_id ada di URL
if (!isset($_GET['guru_id']) || empty($_GET['guru_id'])) {
    http_response_code(400);
    echo json_encode(['status' => 'error', 'message' => 'guru_id wajib diisi.']);
    exit();
}

$guru_id = $_GET['guru_id'];

// Ambil semua data guru dari tabel 'guru'
// Perhatikan: Kolom foto di DB-mu adalah 'profile_image_url' (berdasarkan skema yang kamu kirim)
$stmt = $koneksi->prepare("SELECT id, fullname, email, jabatan, nisn, alamat, profile_image_url FROM guru WHERE id = ?");
$stmt->bind_param("i", $guru_id);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows === 1) {
    $guru = $result->fetch_assoc();
    
    // --- PENTING: Tentukan URL Foto ---
    // Ganti IP Address ini sesuai dengan BASE_URL di ApiClient.java-mu (misal: 192.168.1.100)
    $base_ip = "http://" . $_SERVER['HTTP_HOST']; 
    $foto_url = null;
    
    if (!empty($guru['profile_image_url'])) {
         // Asumsi foto ada di folder api_tk/uploads/
         $foto_filename = basename($guru['profile_image_url']); 
         $foto_url = $base_ip . "/api_tk/uploads/" . $foto_filename; 
    }

    $response = array(
        'status' => 'success',
        'message' => 'Data profil guru berhasil diambil.',
        'guru' => [
            'id' => $guru['id'],
            'fullname' => $guru['fullname'],
            'email' => $guru['email'],
            'jabatan' => $guru['jabatan'],
            'nisn' => $guru['nisn'],
            'alamat' => $guru['alamat'],
            'foto_url' => $foto_url // URL FOTO AKAN DITAMPILKAN
        ]
    );
    http_response_code(200);
} else {
    $response = array('status' => 'error', 'message' => 'Guru tidak ditemukan.');
    http_response_code(404);
}

$stmt->close();
$koneksi->close();

echo json_encode($response);
?>