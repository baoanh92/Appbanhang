<?php
include "connect.php";
$sdt = $_POST['sdt'];
$email = $_POST['email'];
$tongtien = $_POST['tongtien'];
$iduser = $_POST['iduser'];
$diachi = $_POST['diachi'];
$soluong = $_POST['soluong'];
$chitiet = $_POST['chitiet'];
$hotennguoinhan = $_POST['hotennguoinhan'];


$query = 'INSERT INTO `donhang`(`iduser`, `diachi`, `sodienthoai`, `email`, `hotennguoinhan`, `GhiChu`) 
VALUES ('.$iduser.', "'.$diachi.'", "'.$sdt.'", "'.$email.'", "'.$hotennguoinhan.'", "Đơn hàng đang được xử lý")';


$data = mysqli_query($conn, $query);
if ($data == true) {
	$query = 'SELECT id AS iddonhang FROM `donhang` WHERE `iduser` = '.$iduser.' ORDER BY id DESC LIMIT 1';
	$data = mysqli_query($conn, $query);
	while ($row = mysqli_fetch_assoc($data)) {
		$iddonhang = $row['iddonhang']; // Lấy giá trị 'iddonhang' từ mảng $row
    }

    if (!empty($iddonhang)) {
     	// Có đơn hàng
     	$chitiet = json_decode($chitiet, true);
		
     	foreach ($chitiet as $key => $value) {     
		
		$truyvan =  'INSERT INTO `chitietdonhang`(`iddonhang`, `idsp`, `soluong`, `gia`) VALUES ('.$iddonhang.','.$value["idsp"].','.$value["soluong"].',"'.$value["giasp"].'")';
			$truyvan2 = "UPDATE `sanpham` SET `Quantity` = `Quantity` - ".$value["soluong"]." WHERE `id` = " .$value['idsp'];
	
		   $data1 = mysqli_query($conn, $truyvan);
            $data2 = mysqli_query($conn, $truyvan2);
     	}

       if ($data1 && $data2) {
           	$arr = [
		   'success' => true,
		    'message' => "thanh cong",
		    'iddonhang' => $iddonhang
		];
       } else {
           	$arr = [
		   'success' => false,
		    'message' => "khong thanh cong"
		];
       }
       print_r(json_encode($arr));
    } else {
		$arr = [
			'success' => false,
			'message' => "khong thanh cong"
		];
		print_r(json_encode($arr));
	}	
} else {
	$arr = [
		'success' => false,
		'message' => "khong thanh cong"
	];
	print_r(json_encode($arr));
}
?>
