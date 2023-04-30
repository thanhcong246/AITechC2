<?php

class category {
	private $con;

	function __construct() {
		//Getting the DbConnect.php file
		require_once dirname(__FILE__) . '/DbConnect.php';

		//Creating a DbConnect object to connect to the database
		$db = new DbConnect();

		//Initializing our connection link of this class
		//by calling the method connect of DbConnect class
		$this->con = $db->connect();
	}

	function getCategory() {
		$stmt = $this->con->prepare("SELECT * FROM categories");
		$stmt->execute();
		$stmt->bind_result($id_category, $categoryName);

		$categories = array();

		while ($stmt->fetch()) {
			$category = new stdClass();
			$category->id = $id_category;
			$category->category = $categoryName;
			array_push($categories, $category);
		}

		return $categories;
	}

	function getCategoryByName($categoryName){
		$stmt = $this->con->prepare("SELECT id_category, category FROM categories WHERE category = ?");
		$stmt->bind_param("s", $categoryName);
		$stmt->execute();
		$stmt->bind_result($id_category, $category);

		$result = new stdClass();

		while ($stmt->fetch()) {
			$result->id_category = $id_category;
			$result->category = $category;
		}

		return $result;
	}
}

?>
