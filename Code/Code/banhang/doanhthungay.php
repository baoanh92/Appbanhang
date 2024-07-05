<?php
include "connect.php";


$ngayHienTai = date("Y-m-d");

$query = "SELECT SUM(tongtien) as tongDoanhThuNgay FROM `donhang` WHERE DATE(ngaydat) = '$ngayHienTai'";
$data = mysqli_query($conn, $query);

if ($data) {

    $row = mysqli_fetch_assoc($data);
    

    $tongDoanhThuNgay = $row['tongDoanhThuNgay'];

    $result = [
        'success' => true,
        'message' => "Thành công",
        'tongDoanhThuNgay' => $tongDoanhThuNgay
    ];
} else {
    $result = [
        'success' => false,
        'message' => "Không thành công",
        'tongDoanhThuNgay' => 0 
    ];
}

echo json_encode($result);
?>
