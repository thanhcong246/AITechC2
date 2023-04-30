<?php

class Payment{
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

    function setDirectPayment($name_user, $code_cart, $cart_date, $cart_number, $cart_cost, $id_shipping){
        $stmt = $this->con->prepare("INSERT INTO cart_payments (name_user, code_cart, cart_status, cart_date, cart_number, cart_cost, cart_payment, id_shipping) VALUES (?, ?, 0, ?, ?, ?, 'Thanh Toán Trực Tiếp', ?)");
        $stmt->bind_param("sssisi", $name_user, $code_cart, $cart_date, $cart_number, $cart_cost, $id_shipping);
        $stmt->execute();

        // thêm câu lệnh SQL để update bảng cart_details
        $updateStmt = $this->con->prepare("UPDATE cart_details SET status = 0 WHERE code_cart = ?");
        $updateStmt->bind_param("s", $code_cart);
        $updateStmt->execute();
    }

    function getBillPayment($code_cart){
        $stmt = $this->con->prepare("SELECT shipping.address, shipping.phone, cart_payments.name_user, cart_payments.cart_number, cart_payments.cart_cost, cart_payments.cart_status, cart_payments.code_cart, cart_payments.cart_payment FROM cart_payments JOIN shipping ON shipping.id_shipping = cart_payments.id_shipping WHERE cart_payments.code_cart = ?");
        $stmt->bind_param("s", $code_cart);
        $stmt->execute();
        $stmt->bind_result($address_user, $phone_user, $name_user, $cart_number, $cart_cost, $cart_status, $code_cart , $cart_payment);

        $payments = array();

        while ($stmt->fetch()){
            $payment = new stdClass();
            $payment->name_user_payment = $name_user;
            $payment->address_payment = $address_user;
            $payment->phone_payment = $phone_user;
            $payment->cart_number_payment = $cart_number;
            $payment->cart_cost_payment = $cart_cost;
            $payment->cart_status_payment = $cart_status;
            $payment->code_cart_payment = $code_cart;
            $payment->cart_payment = $cart_payment;
            array_push($payments, $payment);
        }
        return $payments;
    }

    function getProductPayment($code_cart){
        $stmt = $this->con->prepare("SELECT cart_details.name_product, cart_details.cost_product, products.image_product FROM cart_details, products WHERE cart_details.status = 0 AND cart_details.code_cart = ? AND cart_details.name_product = products.name_product");
        $stmt->bind_param("i", $code_cart);
        $stmt->execute();
        $stmt->bind_result($name_product, $cost_product,$image_product);

        $cart_product_payments = array();

        while ($stmt->fetch()) {
            $cart_product_payment = new stdClass();
            $cart_product_payment->name_product = $name_product;
            $cart_product_payment->cost_product = $cost_product;
            $cart_product_payment->image_product = $this->image_url.$image_product;

            array_push($cart_product_payments, $cart_product_payment);
        }
        return $cart_product_payments;
    }
}

?>