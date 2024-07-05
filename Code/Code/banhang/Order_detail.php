<?php
include "connect.php";
$product_id = $_POST['product_id'];
$quantity  = $_POST['quantity'];
$sqlmax = "SELECT MAX(id) AS max_id FROM donhang";
$result = mysqli_query($conn, $sqlmax);
$max_id =0;
if ($result->num_rows > 0) {
    // Lấy dòng dữ liệu từ kết quả truy vấn
    $row = $result->fetch_assoc();
    
    // Chuyển đổi giá trị ID sang kiểu số nguyên
    $max_id = intval($row["max_id"]);
}
echo($max_id);
$query = 'INSERT INTO `order_detail`(`order_id`, `product_id`, `quantity`) 
VALUES ("'.$max_id.'","'.$product_id.'","'.$quantity.'")';
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