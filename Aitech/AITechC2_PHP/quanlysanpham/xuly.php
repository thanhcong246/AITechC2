<?php
$mysqli = new mysqli("localhost","root","","AITechC2");
mysqli_set_charset($mysqli  , 'UTF8');
$tensanpham = $_POST['name_product'];
$giasanpham = $_POST['cost_product'];
//Xu ly hinh anh
$hinhanh = $_FILES['image_product']['name'];
$hinhanh_tmp = $_FILES['image_product']['tmp_name'];
$hinhanh_time = time().'_'.$hinhanh;
$tomtat = $_POST['detail_product'];
$tinhtrang = $_POST['status'];
$danhmuc = $_POST['category'];


if(isset($_POST['themsanpham'])){
	//them
	$sql_them = "INSERT INTO products(name_product,cost_product,image_product,detail_product,status,category) VALUE('".$tensanpham."','".$giasanpham."','".$hinhanh_time."','".$tomtat."','".$tinhtrang."','".$danhmuc."')";
		mysqli_query($mysqli,$sql_them);
		move_uploaded_file($hinhanh_tmp, 'uploads/'.$hinhanh_time);
		header('Location: them.php');
	}elseif(isset($_POST['suasanpham'])){
	//sua
		if(!empty($_FILES['hinhanh']['name'])){
			move_uploaded_file($hinhanh_tmp, 'uploads/'.$hinhanh_time);
			$sql = "SELECT * FROM tbl_sanpham WHERE id_sanpham = '$_GET[idsanpham]' LIMIT 1";
			$sql_sua = "UPDATE tbl_sanpham SET tensanpham = '".$tensanpham."', masanpham = '".$masanpham."', giasanpham = '".$giasanpham."', soluong = '".$soluong."', hinhanh = '".$hinhanh_time."', tomtat = '".$tomtat."', noidung = '".$noidung."', tinhtrang = '".$tinhtrang."', id_danhmuc = '".$danhmuc."' WHERE id_sanpham= '$_GET[idsanpham]'";
			//xoa hinh anh cu	
			$query = mysqli_query($mysqli,$sql);
			while($row = mysqli_fetch_array($query)){
				unlink('uploads/'.$row['hinhanh']);
			}
		}else{	
			$sql_sua = "UPDATE tbl_sanpham SET tensanpham = '".$tensanpham."', masanpham = '".$masanpham."', giasanpham = '".$giasanpham."', soluong = '".$soluong."', tomtat = '".$tomtat."', noidung = '".$noidung."', tinhtrang = '".$tinhtrang."', id_danhmuc = '".$danhmuc."' WHERE id_sanpham= '$_GET[idsanpham]'";	
		}
		mysqli_query($mysqli,$sql_sua);
		header('Location:../../index.php?action=quanlysanpham&query=them');	
	}else{
	//xoa
		$id = $_GET['idsanpham'];	
		$sql = "SELECT * FROM tbl_sanpham WHERE id_sanpham = '$id' LIMIT 1";
		$query = mysqli_query($mysqli,$sql);
		while($row=mysqli_fetch_array($query)){
			unlink('uploads/'.$row['hinhanh']);
		}	
		$sql_xoa = "DELETE FROM tbl_sanpham WHERE id_sanpham = '$id'";
		mysqli_query($mysqli,$sql_xoa);
		header('Location:../../index.php?action=quanlysanpham&query=them');
	}
?>