<?php
include "connect.php";

// Nhận dữ liệu từ POST
$id = $_POST['id'];
$mess = $_POST['mess'];

// Chuẩn bị câu truy vấn với các placeholder
$query = "UPDATE `hotro` SET `admin`=? WHERE id=?";

// Chuẩn bị và thực thi câu truy vấn
$stmt = mysqli_prepare($conn, $query);
mysqli_stmt_bind_param($stmt, "si", $mess, $id); // s - string, i - integer
$result = mysqli_stmt_execute($stmt);

// Kiểm tra xem câu truy vấn có thành công không
if ($result) {
    $arr = [
        'success' => true,
        'message' => "Thành công"
    ];
} else {
    $arr = [
        'success' => false,
        'message' => "Không thành công"
    ];
}

// Xuất kết quả dưới dạng JSON
echo json_encode($arr);

// Đóng statement và kết nối
mysqli_stmt_close($stmt);
mysqli_close($conn);
?>
