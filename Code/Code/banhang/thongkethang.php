<?php
include "connect.php";

$query = "SELECT dh.*,SUM(ct.gia) as tongtienthang, MONTH(dh.ngaydat) as thang FROM `donhang` as dh join chitietdonhang as ct on ct.iddonhang = dh.id GROUP BY YEAR(dh.ngaydat), MONTH(dh.ngaydat)";
$data = mysqli_query($conn, $query);
$result = array();
while ($row = mysqli_fetch_assoc($data)) {
	$result[] = ($row);
	// code...
}
if (!empty($result)) {
	$arr = [
		'success' => true,
		'message' => "thanh cong",
		'result' => $result
	];
}else{
	$arr = [
		'success' => false,
		'message' => " khong thanh cong",
		'result' => $result
	];
}
print_r(json_encode($arr));

?>