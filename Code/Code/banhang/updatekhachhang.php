<?php
include "connect.php";
$id = $_POST['id'];
$email = $_POST['email'];
$phone = $_POST['phone'];
$address = $_POST['address'];

// Thực hiện truy vấn UPDATE
$query = "UPDATE `user` SET `email`='.$email.', `mobile`='.$phone.', `address`='.$address.' WHERE `id`=.$id";
$data = mysqli_query($conn, $query);

// Kiểm tra và trả về kết quả
if ($data) {
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

print_r(json_encode($arr));
?>
