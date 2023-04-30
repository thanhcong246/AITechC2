<?php

class product {
	private $con;
	private $image_url = "http://192.168.1.12:86/AITechC2/quanlysanpham/uploads/";

	function __construct() {
		//Getting the DbConnect.php file
		require_once dirname(__FILE__) . '/DbConnect.php';

		//Creating a DbConnect object to connect to the database
		$db = new DbConnect();

		//Initializing our connection link of this class
		//by calling the method connect of DbConnect class
		$this->con = $db->connect();
	}

	function getProduct() {
		$stmt = $this->con->prepare("SELECT * FROM products WHERE status='1'");
		$stmt->execute();
		$stmt->bind_result($id_product, $name_product, $cost_product, $image_product, $detail_product, $category, $rate, $status);

		$products = array();

		while ($stmt->fetch()) {
			$product = new stdClass();
			$product->id = $id_product;
			$product->name = $name_product;
			$product->cost = $cost_product;
			$product->image = $this->image_url.$image_product;
			/*$image_path = realpath(dirname(__FILE__) . '/../quanlysanpham/uploads/') . '/' . $image_product;
			$image_data = @file_get_contents($image_path);
			if ($image_data !== false) {
				$base64_image = base64_encode($image_data);
				$product->image = $base64_image;
			} else {
				$product->image = "";
			}*/
			$product->detail = $detail_product;
			$product->category = $category;
			$product->rate = $rate;
			$product->status = $status;
			array_push($products, $product);
		}
		return $products;
	}

	function getProductByName($search){
		$stmt = $this->con->prepare("SELECT * FROM products WHERE status='1' AND name_product LIKE ?");
		$search = "%$search%";
		$stmt->bind_param("s", $search);
		$stmt->execute();
		$stmt->bind_result($id_product, $name_product, $cost_product, $image_product, $detail_product, $category, $rate, $status);

		$results = array();

		while ($stmt->fetch()) {
			$product = new stdClass();
			$product->id = $id_product;
			$product->name = $name_product;
			$product->cost = $cost_product;
			$product->image = $this->image_url.$image_product;
			$product->detail = $detail_product;
			$product->category = $category;
			$product->rate = $rate;
			$product->status = $status;
			array_push($results, $product);
		}
		return $results;
	}


	function getProductByCategory($category){
		$stmt = $this->con->prepare("SELECT * FROM products WHERE category = ? AND status='1'");
		$stmt->bind_param("s", $category);
		$stmt->execute();
		$stmt->bind_result($id_product, $name_product, $cost_product, $image_product, $detail_product, $category, $rate, $status);

		$results = array();

		while ($stmt->fetch()) {
			$product = new stdClass();
			$product->id = $id_product;
			$product->name = $name_product;
			$product->cost = $cost_product;
			$product->image = $this->image_url.$image_product;
			$product->detail = $detail_product;
			$product->category = $category;
			$product->rate = $rate;
			$product->status = $status;
			array_push($results, $product);
		}
		return $results;
	}

	function getDetailProduct($name_product){
		$stmt = $this->con->prepare("SELECT * FROM products WHERE name_product = ? AND status='1' LIMIT 1");
		$stmt->bind_param("s", $name_product);
		$stmt->execute();
		$stmt->bind_result($id_product, $name_product, $cost_product, $image_product, $detail_product, $category, $rate, $status);

		$results = array();

		while ($stmt->fetch()) {
			$product = new stdClass();
			$product->id = $id_product;
			$product->name = $name_product;
			$product->cost = $cost_product;
			$product->image = $this->image_url.$image_product;
			$product->detail = $detail_product;
			$product->category = $category;
			$product->rate = $rate;
			$product->status = $status;
			array_push($results, $product);
		}
		return $results;
	}

}

?>