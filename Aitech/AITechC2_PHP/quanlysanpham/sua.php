<?php
$sql_sua_sp = "SELECT * FROM tbl_sanpham WHERE id_sanpham = '$_GET[idsanpham]' LIMIT 1";
$query_sua_sp = mysqli_query($mysqli, $sql_sua_sp);	
?>
<p style="font-size: 30px; text-align: center;"><b>Sửa sản phẩm</b></p>
<table border="1px" width="100%" style="border-collapse: collapse;" class="table table-hover">
	<form method="POST" action="module/quanlysanpham/xuly.php?idsanpham=<?php echo $_GET['idsanpham']?>" enctype="multipart/form-data">
		<?php
		while($row = mysqli_fetch_array($query_sua_sp)){
			?>
			<tr>
				<td>Tên Sản Phẩm</td>
				<td><input type="text" name="tensanpham" value="<?php echo $row['tensanpham'] ?>"></td>
			</tr>
			<tr>
				<td>Mã Sản Phẩm</td>
				<td><input type="text" name="masanpham" value="<?php echo $row['masanpham'] ?>"></td>
			</tr>
			<tr>
				<td>Giá Sản Phẩm</td>
				<td><input type="text" name="giasanpham" value="<?php echo $row['giasanpham'] ?>"></td>
			</tr>
			<tr>
				<td>Số Lượng</td>
				<td><input type="text" name="soluong" value="<?php echo $row['soluong'] ?>"></td>
			</tr>
			<tr>
				<td>Hình Ảnh</td>
				<td>
					<input type="file" name="hinhanh" value="">
					<img width="150px" src="module/quanlysanpham/uploads/<?php echo $row['hinhanh'] ?>">
				</td>
			</tr>
			<tr>
				<td>Tóm Tắt</td>
				<td><textarea rows="10" name="tomtat" id="tomtat" style="resize: none;"><?php echo $row['tomtat'] ?></textarea></td>
			</tr>
			<tr>
				<td>Nội Dung</td>
				<td><textarea rows="10" name="noidung" id="noidung" style="resize: none;"><?php echo $row['noidung'] ?></textarea></td>
			</tr>
			<tr>
				<td>Danh Mục Sản Phẩm</td>
				<td>
					<select name="danhmuc">
						<?php
						$sql_danhmuc = "SELECT * FROM tbl_danhmuc ORDER BY id_danhmuc DESC";
						$query_danhmuc = mysqli_query($mysqli,$sql_danhmuc);
						while($row_danhmuc = mysqli_fetch_array($query_danhmuc)){
							if($row_danhmuc['id_danhmuc']==$row['id_danhmuc']){
								?>
								<option selected value="<?php echo $row_danhmuc['id_danhmuc'] ?>"><?php echo $row_danhmuc['tendanhmuc'] ?></option>
								<?php
							}else{
								?>
								<option value="<?php echo $row_danhmuc['id_danhmuc'] ?>"><?php echo $row_danhmuc['tendanhmuc'] ?></option>
								<?php
							}
						}
						?>
					</select>
				</td>
			</tr>
			<tr>
				<td>Tình Trạng</td>
				<td>
					<select name="tinhtrang">
						<?php
						if($row['tinhtrang']==1){
							?>
							<option value="1" selected>Kích Hoạt</option>
							<option value="0">Ẩn</option>
							<?php
						}else{
							?>
							<option value="1">Kích Hoạt</option>
							<option value="0" selected>Ẩn</option>
							<?php
						}
						?>
					</select>
				</td>
			</tr>
			<tr>
				<td colspan="2"><input type="submit" name="suasanpham" value="Sửa sản phẩm"></td>
			</tr>
			<?php
		}
		?>
	</form>

</table>