<?php
include "connect.php";
$tensp = $_POST['tensp'];
$hinhanh = $_POST['hinhanh'];

$query = 'INSERT INTO `loaisanpham`( `loaisp`,  `hinhanh`) VALUES ("'.$tensp.'","'.$hinhanh.'")';
$data = mysqli_query($conn, $query);
if ($data == true) {
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


print_r(json_encode($arr));

?>