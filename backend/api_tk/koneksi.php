<?php
// koneksi.php
header("Content-Type: application/json; charset=UTF-8"); // Pastikan output adalah JSON

$servername = "localhost";
$username = "root"; // Default XAMPP
$password = "";     // Default XAMPP
$dbname = "tk_pertiwi_db"; // Nama DB yang kamu import

// Buat koneksi
$koneksi = new mysqli($servername, $username, $password, $dbname);

// Cek koneksi
if ($koneksi->connect_error) {
    // Jika koneksi gagal, hentikan dan kirim response error
    $response = array(
        'status' => 'error',
        'message' => 'Koneksi database gagal: ' . $koneksi->connect_error
    );
    echo json_encode($response);
    die();
}
?>