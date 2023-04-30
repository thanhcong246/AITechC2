<?php 

class Comment{
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

	function getComment($name_product){
		$stmt = $this->con->prepare("SELECT name_user, name_product, comment_product, rate, date_comment FROM comments WHERE name_product = ? ORDER BY id_comment DESC");
		$stmt->bind_param("s", $name_product);
		$stmt->execute();
		$stmt->bind_result($name_user, $name_product, $comment_product, $rate, $date_comment);

		$comments = array();

		while ($stmt->fetch()) {
			$comment = new stdClass();
			$comment->name_user = $name_user;
			$comment->name_product = $name_product;
			$comment->comment_product = $comment_product;
			$comment->rate = $rate;
			$comment->date_comment = $date_comment;
			array_push($comments, $comment);
		}
		return $comments;
	}

	function setCommentAndGetRate($name_user, $name_product, $comment_product, $rate, $date_comment){
    // Insert the comment into the comments table
    $stmt = $this->con->prepare("INSERT INTO comments (name_user, name_product, comment_product, rate, date_comment) VALUES (?, ?, ?, ?, ?)");
    $stmt->bind_param("sssss", $name_user, $name_product, $comment_product, $rate, $date_comment);
    if (!$stmt->execute()) {
        return "Lỗi khi bình luận";
    }

    // Compute the new rate for the product
    $stmt = $this->con->prepare("SELECT AVG(rate) FROM comments WHERE name_product = ?");
    $stmt->bind_param("s", $name_product);
    $stmt->execute();
    $stmt->bind_result($product_rate);
    $stmt->fetch();
    $stmt->close();

    // Update the rate in the products table
    $stmt = $this->con->prepare("UPDATE products SET rate = ? WHERE name_product = ?");
    $stmt->bind_param("ds", $product_rate, $name_product);
    $stmt->execute();

    // Return the new rate for the product
    $rates = array();
    $rate = new stdClass();
    $rate->name_product = $name_product;
    $rate->product_rate = $product_rate;
    array_push($rates, $rate);

    return $rates;
}


	/*function setComment($name_user, $name_product, $comment_product, $rate, $date_comment){
		$stmt = $this->con->prepare("INSERT INTO comments (name_user, name_product, comment_product, rate, date_comment) VALUES (?, ?, ?, ?, ?)");
		$stmt->bind_param("sssss", $name_user, $name_product, $comment_product, $rate, $date_comment);
		if ($stmt->execute()) {
			return true;
		} else {
			return "Lỗi khi bình luận";
		}
	}

	function getRateProduct($name_product){
		$stmt  = $this->con->prepare("SELECT products.rate, comments.rate FROM products, comments WHERE products.name_product = comments.name_product AND products.name_product = ?");
		$stmt->bind_param("s", $name_product);
		$stmt->execute();
		$stmt->bind_result($product_rate, $comment_rate);

		$count = 0;
		$sum = 0;
		while ($stmt->fetch()) {
			$count++;
			$sum += $comment_rate;
		}
		$product_rate = ($count > 0) ? $sum / $count : 0;

    // Update the rate in the products table
		$stmt = $this->con->prepare("UPDATE products SET rate = ? WHERE name_product = ?");
		$stmt->bind_param("ds", $product_rate, $name_product);
		$stmt->execute();

		$rates = array();
		$rate = new stdClass();
		$rate->name_product = $name_product;
		$rate->product_rate = $product_rate;
		array_push($rates, $rate);

		return $rates;
	}*/
}

?>