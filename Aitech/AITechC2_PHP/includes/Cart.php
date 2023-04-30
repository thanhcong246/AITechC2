<?php 

class Cart{
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

	function setProductToCart($name_product, $cost_product, $name_user){
	    // Check if there's an existing cart for the same user and status=1
		$stmt = $this->con->prepare("SELECT code_cart FROM cart_details WHERE name_user=? AND status=1");
		$stmt->bind_param("s", $name_user);
		$stmt->execute();
		$result = $stmt->get_result();
		$row = $result->fetch_assoc();
	    $code_cart = $row['code_cart']; // Get the code_cart value if there's an existing cart

	    // If there's no existing cart, generate a new random code_cart value
	    if (!$code_cart) {
	    	$code_cart = rand(100000, 999999);
	    }

	    // Set the status to 0 for existing carts of the same user
	    $stmt = $this->con->prepare("UPDATE cart_details SET status=0 WHERE name_user=? AND status=1 AND code_cart!=?");
	    $stmt->bind_param("si", $name_user, $code_cart);
	    $stmt->execute();

	    // Insert the new cart item with the code_cart and status=1
	    $stmt = $this->con->prepare("INSERT INTO cart_details (code_cart, name_product, cost_product, name_user, status) VALUES (?, ?, ?, ?, 1)");
	    $stmt->bind_param("isss", $code_cart, $name_product, $cost_product, $name_user);

	    if ($stmt->execute()) {
	    	return true;
	    } else {
	    	return "Lỗi khi mua hàng";
	    }
	}

	function getCart($name_user){
		$stmt = $this->con->prepare("SELECT cart_details.id_cart_details, cart_details.code_cart, cart_details.name_product, cart_details.cost_product, cart_details.name_user, products.image_product FROM cart_details, products WHERE cart_details.status = 1 AND cart_details.name_user = ? AND cart_details.name_product = products.name_product");
		$stmt->bind_param("s", $name_user);
		$stmt->execute();
		$stmt->bind_result($id_cart_details, $code_cart, $name_product, $cost_product, $name_user, $image_product);

		$cart_details = array();

		while ($stmt->fetch()) {
			$cart_detail = new stdClass();
			$cart_detail->id_cart_details = $id_cart_details;
			$cart_detail->code_cart = $code_cart;
			$cart_detail->name_product = $name_product;
			$cart_detail->cost_product = $cost_product;
			$cart_detail->name_user = $name_user;
			$cart_detail->image_product = $this->image_url.$image_product;

			array_push($cart_details, $cart_detail);
		}
		return $cart_details;
	}

	function removeAllCartItems($name_user){
		$stmt = $this->con->prepare("DELETE FROM cart_details WHERE name_user = ? AND status = 1");
		$stmt->bind_param("s", $name_user);
		$stmt->execute();
		return $stmt->affected_rows > 0;
	}

	function removeCartItemById($id_cart_details){
		$stmt = $this->con->prepare("DELETE FROM cart_details WHERE id_cart_details = ? AND status = 1");
		$stmt->bind_param("s", $id_cart_details);
		$stmt->execute();
		return $stmt->affected_rows > 0;
	}

}

?>