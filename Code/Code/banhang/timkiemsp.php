<?php
include "connect.php";
$search = $_POST['search'];
$loai = $_POST['loai'];
if (empty($search)) {
    $arr = [
        'success' => false,
        'message' => "Không có dữ liệu để tìm kiếm"
    ];
} else {
    $query = "SELECT * FROM `sanpham` WHERE `tensp` LIKE '%" . $search . "%' AND `loai` = '" . $loai . "'";

    $data = mysqli_query($conn, $query);
    $result = array();
    while ($row = mysqli_fetch_assoc($data)) {
        $result[] = $row;
    }
    if (!empty($result)) {
        $arr = [
            'success' => true,
            'message' => "Tìm kiếm thành công",
            'result' => $result
        ];
    } else {
        $arr = [
            'success' => false,
            'message' => "Không tìm thấy sản phẩm",
            'result' => $result
        ];
    }
}

print_r(json_encode($arr));
?>
