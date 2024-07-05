<?php
include "connect.php";
$query = "SELECT SUM(tongtien) as tongDoanhThu FROM `donhang`";
$data = mysqli_query($conn, $query);

$result = array();
while ($row = mysqli_fetch_assoc($data)) {
    $result = $row; // Thêm dữ liệu vào mảng $result
}

if (!empty($result)) {
    $arr = [
        'success' => true,
        'message' => "Thành công",
        'result' => $result
    ];
} else {
    $arr = [
        'success' => false,
        'message' => "Không thành công",
        'result' => $result
    ];
}

// Trả về dữ liệu dưới dạng JSON
echo json_encode($arr);
?>
