<?php
include "connect.php";

// Sử dụng câu lệnh SQL đơn giản để lấy tất cả dữ liệu từ bảng `hotro`
$query = "SELECT * FROM `hotro`";
$result = mysqli_query($conn, $query);

// Tạo mảng chứa kết quả
$support = array();

// Lặp qua các hàng kết quả và thêm vào mảng
while ($row = mysqli_fetch_assoc($result)) {
    $support[] = $row;
}

// Đóng kết nối sau khi sử dụng
mysqli_close($conn);

// Tạo mảng kết quả
$response = array(
    'success' => true,
    'message' => "Dữ liệu hỗ trợ",
    'result' => $support
);

// Trả về JSON
echo json_encode($response);
?>
