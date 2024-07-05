<?php
include "connect.php";
$user = $_POST['user'];
$mess = $_POST['mess'];
$date = $_POST['date'];

// Chuyển đổi ngày thành chuỗi với định dạng "yyyy/MM/dd"
$dateFormatted = date('Y/m/d', strtotime($date));

// Prepare the query with placeholders
$query = 'INSERT INTO `hotro`(`user`, `message`, `giothem`) VALUES (?, ?, ?)';

// Bind parameters and execute the query
$stmt = mysqli_prepare($conn, $query);
mysqli_stmt_bind_param($stmt, "sss", $user, $mess, $dateFormatted); // Sử dụng $dateFormatted thay vì $date
$result = mysqli_stmt_execute($stmt);

// Check if the query was successful
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

// Output the result as JSON
print_r(json_encode($arr));

// Close statement and connection
mysqli_stmt_close($stmt);
mysqli_close($conn);

?>
