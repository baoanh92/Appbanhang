<?php
include "connect.php";
$query = " SELECT * FROM `loaisanpham` ";
$data = mysqli_query($conn, $query) or die( mysqli_error($conn));
$result = array();

while ($row = mysqli_fetch_assoc($data)) {
	$result[] = ($row);
}
$arr = [
'success' => true,
'message' => "",
'result' => $result 
];

print_r(json_encode($arr));

?>
