<?php
include "connect.php";
$email = $_POST['email'];
$pass = $_POST['pass'];
$username = $_POST['username'];
$mobile = $_POST['mobile'];
$address = $_POST['address'];

// Kiểm tra trùng email
$query_email = 'SELECT * FROM `user` WHERE `email`  = "'.$email.'"';
$data_email = mysqli_query($conn, $query_email);
$numrow_email = mysqli_num_rows($data_email);

// Kiểm tra trùng số điện thoại
$query_mobile = 'SELECT * FROM `user` WHERE `mobile`  = "'.$mobile.'"';
$data_mobile = mysqli_query($conn, $query_mobile);
$numrow_mobile = mysqli_num_rows($data_mobile);

if ($numrow_email > 0){
    $arr = [
        'success' => false,
        'message' => "Email đã tồn tại"
    ];
}elseif ($numrow_mobile > 0){
    $arr = [
        'success' => false,
        'message' => "Số điện thoại đã tồn tại"
    ];
}else{
    // Thêm mới người dùng
    $query_insert = 'INSERT INTO `user`( `email`, `pass`, `username`, `mobile`,`address`) VALUES ("'.$email.'","'.$pass.'","'.$username.'","'.$mobile.'","'.$address.'")';
    $data_insert = mysqli_query($conn, $query_insert);

    if ($data_insert == true) {
        $arr = [
            'success' => true,
            'message' => "Thành công"
        ];
    }else{
        $arr = [
            'success' => false,
            'message' => "Không thành công"
        ];
    }
}

print_r(json_encode($arr));
?>
