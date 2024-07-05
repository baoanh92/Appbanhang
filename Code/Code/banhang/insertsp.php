<?php
include "connect.php";
$tensp = $_POST['tensp'];
$gia = $_POST['gia'];
$hinhanh = $_POST['hinhanh'];
$mota = $_POST['mota'];
$loai = $_POST['loai'];
$Quantity = $_POST['Quantity'];
$query = 'INSERT INTO `sanpham`( `tensp`, `giasp`, `hinhanh`, `mota`, `loai`,`Quantity`) 
VALUES ("'.$tensp.'","'.$gia.'","'.$hinhanh.'","'.$mota.'","'.$loai.'","'.$Quantity.'")';
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