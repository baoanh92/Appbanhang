<?php
include "connect.php";

    $name = $_POST['name'];
    $hinhanh = $_POST['hinhanh'];
    $query = "INSERT INTO `loaisanpham` (`loaisp`, `hinhanh`) VALUES (?, ?)";
    $stmt = $conn->prepare($query);
    $stmt->bind_param("ss", $name, $hinhanh);
    $stmt->execute();
    if ($stmt->affected_rows > 0) {
        $arr = [
            'success' => true,
            'message' => "Thêm thành công"
        ];
    } else {
        $arr = [
            'success' => false,
            'message' => "Thêm không thành công"
        ];
    }
    $stmt->close();
$conn->close();
print_r(json_encode($arr));
?>
