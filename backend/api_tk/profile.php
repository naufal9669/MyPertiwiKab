<?php
require 'koneksi.php';

if ($_SERVER['REQUEST_METHOD'] == 'GET') {
    $id = $_GET['id'];
    $query = mysqli_query($con, "SELECT * FROM guru WHERE id='$id'");
    $data = mysqli_fetch_assoc($query);
    if($data['foto_url'] != null) {
        $data['foto_url'] = "http://10.0.2.2/mypertiwikab/uploads/" . $data['foto_url'];
    }
    echo json_encode(['guruProfile' => $data]);
} 
else if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $id = $_POST['id'];
    $fullname = $_POST['fullname'];
    $jabatan = $_POST['jabatan'];
    $nisn = $_POST['nisn'];
    $alamat = $_POST['alamat'];

    $sql = "UPDATE guru SET fullname='$fullname', jabatan='$jabatan', nisn='$nisn', alamat='$alamat' WHERE id='$id'";
    
    // Logic Upload Foto
    if (isset($_FILES['photo']['name']) && $_FILES['photo']['name'] != '') {
        $filename = "guru_" . $id . "_" . time() . ".jpg";
        $target = "uploads/" . $filename;
        if (move_uploaded_file($_FILES['photo']['tmp_name'], $target)) {
            $sql = "UPDATE guru SET fullname='$fullname', jabatan='$jabatan', nisn='$nisn', alamat='$alamat', foto_url='$filename' WHERE id='$id'";
        }
    }

    if (mysqli_query($con, $sql)) {
        echo json_encode(['status' => 'success', 'message' => 'Profil Update Berhasil']);
    } else {
        echo json_encode(['status' => 'error', 'message' => 'Gagal Update']);
    }
}
?>