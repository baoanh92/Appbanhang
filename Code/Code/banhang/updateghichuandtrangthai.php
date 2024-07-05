<?php
include "connect.php";

$id = $_POST['id'];
$NgayGiao = $_POST['NgayGiao'];

// Sử dụng prepared statement để tránh lỗ hổng SQL injection
$query = 'UPDATE `donhang` SET  `ngaygiao`=? WHERE `id`=?';

// Chuẩn bị câu lệnh SQL
$stmt = mysqli_prepare($conn, $query);

// Kiểm tra lỗi khi chuẩn bị câu lệnh SQL
if (!$stmt) {
    $arr = [
        'success' => false,
        'message' => "Lỗi khi chuẩn bị câu lệnh SQL"
    ];
} else {
    // Bind các tham số vào câu lệnh SQL
    mysqli_stmt_bind_param($stmt, "ssi", $NgayGiao, $id);

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
            'message' => "Không thành công"
        ];
    }

    // Đóng prepared statement
    mysqli_stmt_close($stmt);
}

// Đóng kết nối tới CSDL
mysqli_close($conn);

// Trả về kết quả dưới dạng JSON
print_r(json_encode($arr));

?>
