<?php
$sql_lietke_sp = "SELECT * FROM tbl_sanpham,tbl_danhmuc WHERE tbl_sanpham.id_danhmuc=tbl_danhmuc.id_danhmuc ORDER BY id_sanpham DESC";
$query_lietke_sp = mysqli_query($mysqli, $sql_lietke_sp);	
?>
<p style="font-size: 30px"><a style="text-decoration: none; color: black;" href="#boxlietke" aria-expanded="true" data-toggle="collapse"><b>Liệt kê sản phẩm</b></a></p>
<table width="100%" border="1" style="border-collapse: collapse;" class="table table-hover table-striped collapse mt-4 show" id="boxlietke">
	<tr>
		<th>ID</th>
		<th>Tên Sản Phẩm</th>
		<th>Mã Sản Phẩm</th>
		<th>Giá</th>
		<th>Số Lượng</th>
		<th>Danh Mục</th>
		<th>Hình Ảnh</th>
		<th>Tóm Tắt</th>
		<th>Trạng Thái</th>
		<th>Quản Lý</th>


	</tr>
	<?php
	$i=0;
	while ($row = mysqli_fetch_array($query_lietke_sp)) {
		$i++;
		?>
		<tr>
			<td><?php echo $i ?></td>
			<td><?php echo $row['tensanpham'] ?></td>
			<td><?php echo $row['masanpham'] ?></td>
			<td><?php echo $row['giasanpham'] ?></td>
			<td><?php echo $row['soluong'] ?></td>
			<td><?php echo $row['tendanhmuc'] ?></td>
			<td><img width="150px" src="module/quanlysanpham/uploads/<?php echo $row['hinhanh'] ?>"></td>
			<td><?php echo $row['tomtat'] ?></td>
			<td><?php if($row['tinhtrang']==1){
				echo 'Kích Hoạt';				
			}else{
				echo 'Chưa Kích Hoạt';
			}
			?>

		</td>
		<td>
			<a href="module/quanlysanpham/xuly.php?idsanpham=<?php echo $row['id_sanpham']?>">Xóa</a> | <a href="?action=quanlysanpham&query=sua&idsanpham=<?php echo $row['id_sanpham']?>">Sửa</a> 
		</td>
	</tr>
	<?php
}
?>
</table>