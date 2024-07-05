<?php
include "connect.php";
$page = $_POST['page'];
$total = 5; // có thể hiển thị 5 sản phẩm trên mỗi trang
$pos = ($page - 1) * $total; // 0, 5, 10, ...
$loai = $_POST['loai'];

$query = 'SELECT * FROM `sanpham` WHERE `loai`= '.$loai.' LIMIT '.$pos.', '.$total;
$data = mysqli_query($conn, $query) or die( mysqli_error($conn));
$result = array();
while ($row = mysqli_fetch_assoc($data)) {
    $result[] = $row;
}
if (!empty($result)) {
    $arr = [
        'success' => true,
        'message' => "thanh cong",
        'result' => $result
    ];
} else {
    $arr = [
        'success' => false,
        'message' => "khong thanh cong",
        'result' => $result
    ];
}
print_r(json_encode($arr));

?>
