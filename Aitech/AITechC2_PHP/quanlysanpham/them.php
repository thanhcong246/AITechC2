<p style="font-size: 30px;"><a style="text-decoration: none; color: black;" href="#boxthemsanpham" aria-expanded="true" data-toggle="collapse"><b>Thêm sản phẩm</b></a></p>
<table border="1px" width="100%" style="border-collapse: collapse;" class="table table-hover collapse mt-4" id="boxthemsanpham">
	<form method="POST" action="xuly.php" enctype="multipart/form-data">
		<tr>
			<td>Tên Sản Phẩm</td>
			<td><input type="text" name="name_product" value=""></td>
		</tr>
		<tr>
			<td>Giá Sản Phẩm</td>
			<td><input type="text" name="cost_product" value=""></td>
		</tr>
		<tr>
			<td>Hình Ảnh</td>
			<td><input type="file" name="image_product" value=""></td>
		</tr>
		<tr>
			<td>Tóm Tắt</td>
			<td><textarea rows="10" name="detail_product" id="detail_product" ></textarea></td>
		</tr>
		<tr>
			<td>Danh Mục Sản Phẩm</td>
			<td>
				<select name="category">
					<?php
					$mysqli = new mysqli("localhost","root","","AITechC2");
					mysqli_set_charset($mysqli  , 'UTF8');
					$sql_danhmuc = "SELECT * FROM categories ORDER BY id_category DESC";
					$query_danhmuc = mysqli_query($mysqli,$sql_danhmuc);
					while($row_danhmuc = mysqli_fetch_array($query_danhmuc)){
						?>
						<option value="<?php echo $row_danhmuc['category'] ?>"><?php echo $row_danhmuc['category'] ?></option>
						<?php
					}
					?>
				</select>
			</td>
		</tr>
		<tr>
			<td>Tình Trạng</td>
			<td>
				<select name="status">
					<option value="1">Kích Hoạt</option>
					<option value="0">Ẩn</option>
				</select>
			</td>
		</tr>
		<tr>
			<td colspan="2"><input type="submit" name="themsanpham" value="Thêm sản phẩm"></td>
		</tr>
	</form>
</table>