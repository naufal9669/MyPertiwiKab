-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 05, 2025 at 11:26 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `tk_pertiwi_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `absensi`
--

CREATE TABLE `absensi` (
  `id` int(11) NOT NULL,
  `guru_id` int(11) NOT NULL,
  `tanggal` date NOT NULL,
  `jam_datang` time DEFAULT NULL,
  `jam_pulang` time DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `absensi`
--

INSERT INTO `absensi` (`id`, `guru_id`, `tanggal`, `jam_datang`, `jam_pulang`, `status`) VALUES
(29, 19, '2025-12-05', '10:01:34', '10:02:03', 'Hadir');

-- --------------------------------------------------------

--
-- Table structure for table `agenda_kegiatan`
--

CREATE TABLE `agenda_kegiatan` (
  `id` int(11) NOT NULL,
  `judul` varchar(255) NOT NULL,
  `deskripsi` text NOT NULL,
  `tanggal` date NOT NULL,
  `waktu` time DEFAULT NULL,
  `foto` varchar(255) DEFAULT NULL,
  `tipe` varchar(50) DEFAULT 'kegiatan',
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `agenda_kegiatan`
--

INSERT INTO `agenda_kegiatan` (`id`, `judul`, `deskripsi`, `tanggal`, `waktu`, `foto`, `tipe`, `created_at`, `updated_at`) VALUES
(1, 'Kegiatan Hari Anak 2025', 'Perayaan Hari Anak dengan berbagai lomba.', '2025-11-20', NULL, NULL, 'kegiatan', '2025-10-07 12:54:17', '2025-10-07 12:54:17'),
(4, 'Hari Santri', '-', '2025-11-14', NULL, '1762862094_IMG_3376.JPG', 'acara_tahunan', '2025-11-11 11:54:54', '2025-11-11 11:54:54'),
(5, 'hari ini', '-', '2025-11-11', NULL, '1762873270_IMG_3417.JPG', 'acara_tahunan', '2025-11-11 15:01:10', '2025-11-11 15:01:10');

-- --------------------------------------------------------

--
-- Table structure for table `galeri_foto`
--

CREATE TABLE `galeri_foto` (
  `id` int(11) NOT NULL,
  `judul` varchar(255) DEFAULT NULL,
  `deskripsi` text DEFAULT NULL,
  `kategori` enum('guru','kepala_sekolah','kegiatan','prestasi','ekstrakurikuler','lainnya') NOT NULL DEFAULT 'kegiatan',
  `file_path` varchar(255) NOT NULL,
  `uploaded_by` int(11) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `galeri_foto`
--

INSERT INTO `galeri_foto` (`id`, `judul`, `deskripsi`, `kategori`, `file_path`, `uploaded_by`, `created_at`) VALUES
(4, '', '', 'kepala_sekolah', '1762249915_IMG_3424.JPG', NULL, '2025-11-04 09:51:55'),
(8, 'dani sp3', '', 'guru', '1762334154_IMG_3384.JPG', NULL, '2025-11-05 09:15:54');

-- --------------------------------------------------------

--
-- Table structure for table `guru`
--

CREATE TABLE `guru` (
  `id` int(11) NOT NULL,
  `fullname` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `jabatan` varchar(50) DEFAULT NULL,
  `nisn` varchar(20) DEFAULT NULL,
  `alamat` text DEFAULT NULL,
  `profile_image_url` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `guru`
--

INSERT INTO `guru` (`id`, `fullname`, `email`, `password`, `jabatan`, `nisn`, `alamat`, `profile_image_url`, `created_at`) VALUES
(19, 'nana', 'unahyya@gmail.com', '$2y$10$0iEUK5d7NK2mwe06i6V4Nep73LVJsFJpQ5O7t/T59r4WDVg5oJ7uW', 'Guru B1', '12345', 'jl.merdeka', 'http://10.221.36.10/api_tk/uploads/19_1764903785.jpg', '2025-12-05 03:00:38');

-- --------------------------------------------------------

--
-- Table structure for table `info_sekolah`
--

CREATE TABLE `info_sekolah` (
  `id` int(11) NOT NULL,
  `section` varchar(100) NOT NULL,
  `content` text NOT NULL,
  `updated_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `info_sekolah`
--

INSERT INTO `info_sekolah` (`id`, `section`, `content`, `updated_at`) VALUES
(1, 'profil', 'TK Pertiwi adalah taman kanak-kanak yang berdedikasi untuk memberikan pendidikan berkualitas bagi anak usia dini.', '2025-10-07 12:54:17'),
(2, 'visi_misi', 'Visi: Menjadi TK terdepan dalam pendidikan anak usia dini. Misi: Memberikan lingkungan belajar yang menyenangkan dan mendukung perkembangan holistik.', '2025-10-07 12:54:17'),
(3, 'kontak', 'Alamat: Jl. Contoh No. 123, Jakarta\nTelepon: +6281234567890\nEmail: info@tkpertiwi.sch.id', '2025-10-07 12:54:17');

-- --------------------------------------------------------

--
-- Table structure for table `notifications`
--

CREATE TABLE `notifications` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `pendaftaran_id` int(11) NOT NULL,
  `message` varchar(255) NOT NULL,
  `is_read` tinyint(1) DEFAULT 0,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `type` enum('pendaftaran','pembayaran') NOT NULL DEFAULT 'pendaftaran'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `otp_tokens`
--

CREATE TABLE `otp_tokens` (
  `id` int(11) NOT NULL,
  `guru_id` int(11) NOT NULL,
  `token` varchar(6) NOT NULL,
  `expires_at` datetime NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `payments`
--

CREATE TABLE `payments` (
  `id` int(11) NOT NULL,
  `pendaftar_id` int(11) NOT NULL,
  `nama_anak` varchar(100) NOT NULL,
  `nama_ortu` varchar(100) NOT NULL,
  `metode_pembayaran` varchar(50) DEFAULT NULL,
  `tanggal_bayar` datetime DEFAULT NULL,
  `bukti_path` varchar(255) DEFAULT NULL,
  `status_pembayaran` enum('belum_bayar','menunggu_verifikasi','dibayar') DEFAULT 'belum_bayar',
  `created_at` timestamp NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `payments`
--

INSERT INTO `payments` (`id`, `pendaftar_id`, `nama_anak`, `nama_ortu`, `metode_pembayaran`, `tanggal_bayar`, `bukti_path`, `status_pembayaran`, `created_at`) VALUES
(1, 28, '', '', 'qris', '2025-11-18 02:02:44', 'bukti_1763431364_691bd3c40ca23.png', 'menunggu_verifikasi', '2025-11-18 02:02:44'),
(2, 28, '', '', 'qris', '2025-11-18 02:03:10', 'bukti_1763431390_691bd3de57e0a.png', 'menunggu_verifikasi', '2025-11-18 02:03:10'),
(3, 28, '', '', 'bca', '2025-11-18 02:04:03', 'bukti_1763431443_691bd4132f712.png', 'menunggu_verifikasi', '2025-11-18 02:04:03'),
(4, 28, '', '', 'bca', '2025-11-18 02:06:13', 'bukti_1763431573_691bd495093ff.png', 'menunggu_verifikasi', '2025-11-18 02:06:13'),
(5, 28, 'pitek', 'ayam', 'bri', '2025-11-18 02:16:52', 'bukti_1763432212_691bd7143f66c.png', 'dibayar', '2025-11-18 02:16:52'),
(6, 28, 'pitek', 'ayam', 'bri', '2025-11-18 09:31:52', 'bukti_1763458312_691c3d08d4b88.png', 'dibayar', '2025-11-18 09:31:52'),
(7, 28, 'pitek', 'ayam', 'bca', '2025-11-18 09:38:27', 'bukti_1763458707_691c3e93d40b8.png', 'dibayar', '2025-11-18 09:38:27'),
(8, 28, 'pitek', 'ayam', 'qris', '2025-11-18 09:38:39', 'bukti_1763458719_691c3e9fd1699.png', 'dibayar', '2025-11-18 09:38:39'),
(9, 28, 'pitek', 'ayam', 'bri', '2025-11-20 02:38:20', 'bukti_1763606300_691e7f1c88d4c.png', 'dibayar', '2025-11-20 02:38:20'),
(10, 28, 'pitek', 'ayam', 'bri', '2025-11-20 02:42:23', 'bukti_1763606543_691e800f4f17f.png', 'dibayar', '2025-11-20 02:42:23'),
(11, 28, 'pitek', 'ayam', 'bri', '2025-11-20 03:28:56', 'bukti_1763609336_691e8af8d35af.png', 'dibayar', '2025-11-20 03:28:56'),
(12, 28, 'pitek', 'ayam', 'bri', '2025-11-20 03:31:50', 'bukti_1763609510_691e8ba612aee.png', 'dibayar', '2025-11-20 03:31:50'),
(13, 28, 'pitek', 'ayam', 'bri', '2025-11-20 09:53:23', 'bukti_1763632403_691ee513a53e5.png', 'dibayar', '2025-11-20 09:53:23'),
(14, 28, 'pitek', 'ayam', 'bri', '2025-11-20 09:53:48', 'bukti_1763632428_691ee52c21a92.png', 'dibayar', '2025-11-20 09:53:48'),
(15, 36, 'bebe', 'ayy', 'bca', '2025-11-20 09:57:15', 'bukti_1763632635_691ee5fb53e24.png', 'dibayar', '2025-11-20 09:57:15'),
(16, 36, 'bebe', 'ayy', 'mandiri', '2025-11-20 10:21:53', 'bukti_1763634113_691eebc15a8b3.png', 'dibayar', '2025-11-20 10:21:53'),
(17, 36, 'bebe', 'ayy', 'mandiri', '2025-11-20 10:27:29', 'bukti_1763634449_691eed11225e2.png', 'dibayar', '2025-11-20 10:27:29'),
(18, 36, 'bebe', 'ayy', 'mandiri', '2025-11-20 11:11:24', 'bukti_1763637084_691ef75c9bc1b.png', 'dibayar', '2025-11-20 11:11:24'),
(19, 36, 'bebe', 'ayy', 'bri', '2025-11-20 11:20:23', 'bukti_1763637623_691ef977c37f2.png', 'dibayar', '2025-11-20 11:20:23'),
(20, 40, 'vbhjko', 'asdfg', 'bri', '2025-11-20 11:50:44', 'bukti_1763639444_691f0094b2da1.png', 'dibayar', '2025-11-20 11:50:44'),
(21, 41, 'iqbal', 'hari', 'bri', '2025-11-20 12:03:04', 'bukti_1763640184_691f0378ed700.png', 'dibayar', '2025-11-20 12:03:04'),
(22, 41, 'iqbal', 'hari', 'bri', '2025-11-20 12:18:08', 'bukti_1763641088_691f0700f01db.png', 'dibayar', '2025-11-20 12:18:08');

-- --------------------------------------------------------

--
-- Table structure for table `pendaftaran`
--

CREATE TABLE `pendaftaran` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `nama_anak` varchar(255) NOT NULL,
  `nama_ortu` varchar(255) NOT NULL,
  `tanggal_lahir_anak` date NOT NULL,
  `alamat` text DEFAULT NULL,
  `nomor_telepon` varchar(20) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `tanggal_daftar` timestamp NULL DEFAULT current_timestamp(),
  `status_pembayaran` enum('belum_bayar','menunggu_verifikasi','dibayar','ditolak') DEFAULT 'belum_bayar',
  `metode_pembayaran` varchar(50) DEFAULT NULL,
  `bukti_pembayaran_path` varchar(255) DEFAULT NULL,
  `tanggal_pembayaran` timestamp NULL DEFAULT NULL,
  `akta_kelahiran` varchar(255) DEFAULT NULL,
  `kartu_keluarga` varchar(255) DEFAULT NULL,
  `pas_foto` varchar(255) DEFAULT NULL,
  `surat_sehat` varchar(255) DEFAULT NULL,
  `bukti_pembayaran` varchar(255) DEFAULT NULL,
  `status_ppdb` enum('pending','diterima','ditolak') DEFAULT 'pending',
  `access_code` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pendaftaran`
--

INSERT INTO `pendaftaran` (`id`, `user_id`, `nama_anak`, `nama_ortu`, `tanggal_lahir_anak`, `alamat`, `nomor_telepon`, `email`, `tanggal_daftar`, `status_pembayaran`, `metode_pembayaran`, `bukti_pembayaran_path`, `tanggal_pembayaran`, `akta_kelahiran`, `kartu_keluarga`, `pas_foto`, `surat_sehat`, `bukti_pembayaran`, `status_ppdb`, `access_code`) VALUES
(40, 24, 'vbhjko', 'asdfg', '2025-11-22', 'asdfghjk', '13456', 'cobacoba@gmail.com', '2025-11-20 11:50:22', 'dibayar', NULL, NULL, NULL, 'akta_kelahiran_1763639422.png', 'kartu_keluarga_1763639422.png', 'pas_foto_1763639422.png', 'surat_sehat_1763639422.png', NULL, 'pending', NULL),
(41, 25, 'iqbal', 'hari', '2025-11-11', 'aaaaaaaaaaaaa', '1234567890123', 'iqbal@gmail.com', '2025-11-20 12:02:39', 'dibayar', NULL, NULL, NULL, 'akta_kelahiran_1763640159.png', 'kartu_keluarga_1763640159.png', 'pas_foto_1763640159.png', 'surat_sehat_1763640159.png', NULL, 'diterima', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `pengajuan_izin`
--

CREATE TABLE `pengajuan_izin` (
  `id` int(11) NOT NULL,
  `guru_id` int(11) NOT NULL,
  `tanggal_izin` date NOT NULL,
  `jenis_izin` varchar(50) NOT NULL,
  `deskripsi` text DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pengajuan_izin`
--

INSERT INTO `pengajuan_izin` (`id`, `guru_id`, `tanggal_izin`, `jenis_izin`, `deskripsi`, `created_at`) VALUES
(12, 19, '2025-12-05', 'Izin Sakit', 'sakit', '2025-12-05 03:02:25');

-- --------------------------------------------------------

--
-- Table structure for table `ppdb_info`
--

CREATE TABLE `ppdb_info` (
  `id` int(11) NOT NULL,
  `type` enum('jadwal','syarat') NOT NULL,
  `content` mediumtext DEFAULT NULL,
  `display_order` int(11) DEFAULT 0,
  `created_at` timestamp NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `ppdb_info`
--

INSERT INTO `ppdb_info` (`id`, `type`, `content`, `display_order`, `created_at`) VALUES
(1, 'jadwal', '{\n    \"jadwal\": [\n        {\n            \"nama\": \"pendaftaran online\",\n            \"mulai\": \"2025-10-28\",\n            \"selesai\": \"2025-10-29\"\n        },\n        {\n            \"nama\": \"verifikasi\",\n            \"mulai\": \"2025-11-19\",\n            \"selesai\": \"2025-11-20\"\n        },\n        {\n            \"nama\": \"uji nyali\",\n            \"mulai\": \"2025-11-28\",\n            \"selesai\": \"2025-11-29\"\n        }\n    ],\n    \"syarat\": \"fotokopo kk\\r\\nsuket\\r\\nkia anak\"\n}', 1, '2025-10-07 12:54:17'),
(2, 'jadwal', 'Verifikasi Dokumen: 1 Oktober - 5 Oktober 2025', 2, '2025-10-07 12:54:17'),
(3, 'jadwal', 'Pengumuman Kelulusan: 10 Oktober 2025', 3, '2025-10-07 12:54:17'),
(4, 'jadwal', 'Daftar Ulang: 11 Oktober - 15 Oktober 2025', 4, '2025-10-07 12:54:17'),
(5, 'syarat', 'Fotokopi Akta Kelahiran', 1, '2025-10-07 12:54:17'),
(6, 'syarat', 'Fotokopi Kartu Keluarga', 2, '2025-10-07 12:54:17'),
(7, 'syarat', 'Pas Foto 3x4 (2 lembar)', 3, '2025-10-07 12:54:17'),
(8, 'syarat', 'Surat Keterangan Sehat dari Dokter', 4, '2025-10-07 12:54:17');

-- --------------------------------------------------------

--
-- Table structure for table `settings`
--

CREATE TABLE `settings` (
  `id` int(11) NOT NULL,
  `key_name` varchar(255) NOT NULL,
  `value` text NOT NULL,
  `updated_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `settings`
--

INSERT INTO `settings` (`id`, `key_name`, `value`, `updated_at`) VALUES
(1, 'nominal_pendaftaran', '500000', '2025-10-07 12:54:17');

-- --------------------------------------------------------

--
-- Table structure for table `status_history`
--

CREATE TABLE `status_history` (
  `id` int(11) NOT NULL,
  `pendaftaran_id` int(11) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `status` varchar(50) NOT NULL,
  `checked_at` timestamp NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `role` varchar(10) NOT NULL,
  `nama_ortu` varchar(100) NOT NULL,
  `foto` varchar(255) DEFAULT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `reset_token` varchar(255) DEFAULT NULL,
  `reset_expires` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `role`, `nama_ortu`, `foto`, `email`, `password`, `created_at`, `reset_token`, `reset_expires`) VALUES
(9, 'akukamu', 'admin', 'ayah', NULL, 'kamu@gmail.com', '12345678', '2025-10-22 11:56:48', NULL, NULL),
(14, 'yaya', 'admin', '', 'admin_1763646360_691f1b9816feb.png', 'awas@gmail.com', '$2y$10$EBVtjMir1KfjTNb4rhfsNeMVTbln4OoRlbwuwGgBIZ4FpqXUGzhVi', '2025-10-27 11:41:24', NULL, NULL),
(23, 'bebe', 'user', 'ayy', NULL, 'beb@gmail.com', '$2y$10$H1UDpM3lSBovK6lSLFBex.WimZI/FDuEWB7qTyejsusRy4gxpEGJG', '2025-11-20 09:56:09', NULL, NULL),
(24, 'rizal', 'user', 'dimas', NULL, 'rizal@gmail.com', '$2y$10$LE673Oyh9XqSEsUqAhbwAOjUi1O0mUlauyfNBK5RRIGc4W237PfN2', '2025-11-20 11:22:22', NULL, NULL),
(25, 'iqbal', 'user', 'hari', NULL, 'iqbal@gmail.com', '$2y$10$VyFm7ImUO2u1TNJdhZTZ.OuDuD/ktS3tuKItfAGmECtLzcdc77rCa', '2025-11-20 12:01:15', NULL, NULL);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `absensi`
--
ALTER TABLE `absensi`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `guru_id` (`guru_id`,`tanggal`);

--
-- Indexes for table `agenda_kegiatan`
--
ALTER TABLE `agenda_kegiatan`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `galeri_foto`
--
ALTER TABLE `galeri_foto`
  ADD PRIMARY KEY (`id`),
  ADD KEY `uploaded_by` (`uploaded_by`);

--
-- Indexes for table `guru`
--
ALTER TABLE `guru`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`),
  ADD UNIQUE KEY `nisn` (`nisn`);

--
-- Indexes for table `info_sekolah`
--
ALTER TABLE `info_sekolah`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `notifications`
--
ALTER TABLE `notifications`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `otp_tokens`
--
ALTER TABLE `otp_tokens`
  ADD PRIMARY KEY (`id`),
  ADD KEY `guru_id` (`guru_id`);

--
-- Indexes for table `payments`
--
ALTER TABLE `payments`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `pendaftaran`
--
ALTER TABLE `pendaftaran`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_pendaftaran_user` (`user_id`);

--
-- Indexes for table `pengajuan_izin`
--
ALTER TABLE `pengajuan_izin`
  ADD PRIMARY KEY (`id`),
  ADD KEY `guru_id` (`guru_id`);

--
-- Indexes for table `ppdb_info`
--
ALTER TABLE `ppdb_info`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `settings`
--
ALTER TABLE `settings`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `key_name` (`key_name`);

--
-- Indexes for table `status_history`
--
ALTER TABLE `status_history`
  ADD PRIMARY KEY (`id`),
  ADD KEY `pendaftaran_id` (`pendaftaran_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `absensi`
--
ALTER TABLE `absensi`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;

--
-- AUTO_INCREMENT for table `agenda_kegiatan`
--
ALTER TABLE `agenda_kegiatan`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `galeri_foto`
--
ALTER TABLE `galeri_foto`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `guru`
--
ALTER TABLE `guru`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT for table `info_sekolah`
--
ALTER TABLE `info_sekolah`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `notifications`
--
ALTER TABLE `notifications`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `otp_tokens`
--
ALTER TABLE `otp_tokens`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `payments`
--
ALTER TABLE `payments`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT for table `pendaftaran`
--
ALTER TABLE `pendaftaran`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=42;

--
-- AUTO_INCREMENT for table `pengajuan_izin`
--
ALTER TABLE `pengajuan_izin`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `ppdb_info`
--
ALTER TABLE `ppdb_info`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `settings`
--
ALTER TABLE `settings`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `status_history`
--
ALTER TABLE `status_history`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `absensi`
--
ALTER TABLE `absensi`
  ADD CONSTRAINT `absensi_ibfk_1` FOREIGN KEY (`guru_id`) REFERENCES `guru` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `galeri_foto`
--
ALTER TABLE `galeri_foto`
  ADD CONSTRAINT `galeri_foto_ibfk_1` FOREIGN KEY (`uploaded_by`) REFERENCES `users` (`id`) ON DELETE SET NULL;

--
-- Constraints for table `otp_tokens`
--
ALTER TABLE `otp_tokens`
  ADD CONSTRAINT `otp_tokens_ibfk_1` FOREIGN KEY (`guru_id`) REFERENCES `guru` (`id`);

--
-- Constraints for table `pendaftaran`
--
ALTER TABLE `pendaftaran`
  ADD CONSTRAINT `fk_pendaftaran_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `pengajuan_izin`
--
ALTER TABLE `pengajuan_izin`
  ADD CONSTRAINT `pengajuan_izin_ibfk_1` FOREIGN KEY (`guru_id`) REFERENCES `guru` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `status_history`
--
ALTER TABLE `status_history`
  ADD CONSTRAINT `status_history_ibfk_1` FOREIGN KEY (`pendaftaran_id`) REFERENCES `pendaftaran` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
