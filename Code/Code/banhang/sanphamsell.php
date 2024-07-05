<?php
include "connect.php";
$date = $_GET['date'];
$query = "SELECT dh.id, spm.hinhanh, dh.NgayGiao, dh.GhiChu, spm.tensp, dh.email, dh.sodienthoai, ct.soluong * ct.gia as tongtien
          FROM donhang AS dh 
          JOIN chitietdonhang AS ct ON dh.id = ct.iddonhang 
          JOIN sanpham AS spm ON ct.idsp = spm.id
          WHERE DATE(dh.ngaydat) = '$date'";

$data = mysqli_query($conn, $query) or die(mysqli_error($conn));

$result = array();

while ($row = mysqli_fetch_assoc($data)) {
    $result[] = $row;
}

$arr = [
    'success' => true,
    'message' => "",
    'result' => $result 
];

print_r(json_encode($arr));
?>
