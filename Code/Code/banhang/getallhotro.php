<?php
include "connect.php";

// Kiểm tra xem id đã được gửi từ phía client chưa
if(isset($_GET['user'])) {
    // Lấy id từ dữ liệu gửi lên
    $user = $_GET['user'];

    // Sử dụng prepared statement để tránh tấn công SQL injection
    $query = "SELECT * FROM `hotro` WHERE user = ?";
    $statement = mysqli_prepare($conn, $query);
    mysqli_stmt_bind_param($statement, "i", $user);
    mysqli_stmt_execute($statement);
    $result = mysqli_stmt_get_result($statement);

    // Tạo mảng chứa kết quả
    $support = array();

    // Lặp qua các hàng kết quả và thêm vào mảng
    while ($row = mysqli_fetch_assoc($result)) {
        $support[] = $row; // Changed from $products[] to $support[]
    }

    // Đóng statement sau khi sử dụng
    mysqli_stmt_close($statement);

    // Tạo mảng kết quả
    $response = array(
        'success' => true,
        'message' => "Dữ liệu sản phẩm với ID $user",
        'result' => $support
    );

    // Trả về JSON
    echo json_encode($response);
} else {
    // Nếu không có id được gửi lên từ phía client
    $response = array(
        'success' => false,
        'message' => "Không có ID sản phẩm được gửi lên từ client",
        'result' => null
    );

    // Trả về JSON
    echo json_encode($response);
}

?>
