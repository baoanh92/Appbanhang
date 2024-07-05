<?php
include "connect.php";

$id = $_POST['id'];
$name = $_POST['name'];
$hinhanh = $_POST['hinhanh'];
$query = "UPDATE `loaisanpham` SET `loaisp`=?, `hinhanh`=? WHERE `id`=?";
$stmt = $conn->prepare($query);
$stmt->bind_param("ssi", $name, $hinhanh, $id);
$stmt->execute();

if ($stmt->affected_rows > 0) {
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
$stmt->close();
$conn->close();
print_r(json_encode($arr));
?>
