<?php
// update_profile.php - VERSI ANTI CRASH
include 'koneksi.php';

// Matikan error display agar tidak merusak JSON, tapi kita tangkap manual nanti
ini_set('display_errors', 0);
error_reporting(0);

header('Content-Type: application/json');

// Fungsi untuk kirim respon JSON dan berhenti
function sendResponse($status, $message) {
    echo json_encode(['status' => $status, 'message' => $message]);
    exit();
}

try {
    if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
        sendResponse('error', 'Method salah');
    }

    $guru_id  = $_POST['guru_id'] ?? '';
    $fullname = $_POST['fullname'] ?? '';
    $jabatan  = $_POST['jabatan'] ?? '';
    $nisn     = $_POST['nisn'] ?? '';
    $alamat   = $_POST['alamat'] ?? '';

    if (empty($guru_id)) {
        sendResponse('error', 'ID Guru tidak ditemukan');
    }

    // 1. UPDATE DATA TEKS DULU
    $sql = "UPDATE guru SET fullname=?, jabatan=?, nisn=?, alamat=? WHERE id=?";
    $stmt = $koneksi->prepare($sql);
    
    if (!$stmt) {
        throw new Exception("Gagal prepare statement: " . $koneksi->error);
    }

    $stmt->bind_param("ssssi", $fullname, $jabatan, $nisn, $alamat, $guru_id);
    
    if (!$stmt->execute()) {
        throw new Exception("Gagal update data: " . $stmt->error);
    }

    // 2. CEK & UPLOAD FOTO
    // Kita cek apakah ada file yang diupload dan tidak error
    if (isset($_FILES['photo']) && $_FILES['photo']['error'] === UPLOAD_ERR_OK) {
        
        $target_dir = "uploads/";
        
        // Buat folder jika belum ada (Coba paksa buat)
        if (!is_dir($target_dir)) {
            if (!mkdir($target_dir, 0777, true)) {
                throw new Exception("Gagal membuat folder uploads. Cek izin server.");
            }
        }
        
        $file_ext = pathinfo($_FILES['photo']['name'], PATHINFO_EXTENSION);
        $new_filename = $guru_id . "_" . time() . "." . $file_ext;
        $target_file = $target_dir . $new_filename;
        
        if (move_uploaded_file($_FILES['photo']['tmp_name'], $target_file)) {
            // Berhasil upload, simpan URL ke DB
            // GANTI IP INI SESUAI LAPTOP KAMU!
            $base_url = "http://10.221.36.10/api_tk/"; 
            $full_url = $base_url . $target_file;
            
            $sql_foto = "UPDATE guru SET profile_image_url='$full_url' WHERE id='$guru_id'";
            mysqli_query($koneksi, $sql_foto);
        } else {
            throw new Exception("Gagal memindahkan file gambar.");
        }
    }

    // Jika sampai sini, berarti sukses
    sendResponse('success', 'Profil berhasil diupdate');

} catch (Exception $e) {
    // Tangkap error apapun dan kirim sebagai JSON (Bukan Error 500)
    sendResponse('error', 'Server Error: ' . $e->getMessage());
}

$koneksi->close();
?>