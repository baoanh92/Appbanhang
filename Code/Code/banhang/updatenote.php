<?php
include "connect.php";

// Kiểm tra xem dữ liệu được gửi từ form POST đã tồn tại chưa
if(isset($_POST['id']) && isset($_POST['GhiChu'])) {
    $id = $_POST['id'];
    $GhiChu = $_POST['GhiChu'];

    // Kiểm tra và làm sạch dữ liệu trước khi sử dụng để tránh SQL Injection
    $id = mysqli_real_escape_string($conn, $id);
    $GhiChu = mysqli_real_escape_string($conn, $GhiChu);

    // Sử dụng prepared statement để tránh lỗ hổng SQL injection
    $query = 'UPDATE `donhang` SET `ghichu`=? WHERE `id`=?';

    // Chuẩn bị câu lệnh SQL
    $stmt = mysqli_prepare($conn, $query);

    // Kiểm tra lỗi khi chuẩn bị câu lệnh SQL
    if ($stmt) {
        // Bind các tham số vào câu lệnh SQL
        mysqli_stmt_bind_param($stmt, "si", $GhiChu, $id);

        // Thực thi câu lệnh SQL
        $result = mysqli_stmt_execute($stmt);

        // Kiểm tra kết quả thực thi câu lệnh SQL
        if ($result) {
            $arr = [
                'success' => true,
                'message' => "Thành công"
            ];
        } else {
            $arr = [
                'success' => false,
                'message' => "Không thành công: " . mysqli_error($conn)
            ];
        }

        // Đóng prepared statement
        mysqli_stmt_close($stmt);
    } else {
        $arr = [
            'success' => false,
            'message' => "Lỗi khi chuẩn bị câu lệnh SQL: " . mysqli_error($conn)
        ];
    }

    // Đóng kết nối tới CSDL
    mysqli_close($conn);

} else {
    $arr = [
        'success' => false,
        'message' => "Thiếu dữ liệu đầu vào"
    ];
}

// Trả về kết quả dưới dạng JSON
print_r(json_encode($arr));

?>
