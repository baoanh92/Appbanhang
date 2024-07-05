<?php
include "connect.php";

$email = isset($_POST['email']) ? mysqli_real_escape_string($conn, $_POST['email']) : '';
$pass = isset($_POST['pass']) ? mysqli_real_escape_string($conn, $_POST['pass']) : '';
$username = isset($_POST['username']) ? mysqli_real_escape_string($conn, $_POST['username']) : '';
$mobile = isset($_POST['mobile']) ? mysqli_real_escape_string($conn, $_POST['mobile']) : '';
$address = isset($_POST['address']) ? mysqli_real_escape_string($conn, $_POST['address']) : '';
$decentralization = isset($_POST['decentralization']) ? mysqli_real_escape_string($conn, $_POST['decentralization']) : '';

$query = "SELECT * FROM `thanhvien` WHERE `email` = '$email'";
$result = mysqli_query($conn, $query);

if (mysqli_num_rows($result) > 0) {
    $response = [
        'success' => false,
        'message' => "Email already exists"
    ];
} else {
    $insertQuery = "INSERT INTO `thanhvien` (`email`, `pass`, `username`, `mobile`, `address`, `decentralization`) 
                    VALUES ('$email', '$pass', '$username', '$mobile', '$address', '$decentralization')";
    $insertResult = mysqli_query($conn, $insertQuery);

    if ($insertResult) {
        $response = [
            'success' => true,
            'message' => "Success"
        ];
    } else {
        $response = [
            'success' => false,
            'message' => "Failed to insert data"
        ];
    }
}

header('Content-Type: application/json');
echo json_encode($response);
?>
