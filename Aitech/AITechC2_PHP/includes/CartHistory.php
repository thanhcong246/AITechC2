<?php

class CartHistory{
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

    function getCartHistory($id_user){
        $stmt = $this->con->prepare("SELECT cart_payments.code_cart, cart_payments.cart_status, cart_payments.cart_date, cart_payments.cart_number, cart_payments.cart_cost, cart_payments.cart_payment FROM cart_payments, shipping, users WHERE users.id = shipping.id_user AND shipping.id_shipping = cart_payments.id_shipping AND users.id = ? ORDER BY cart_payments.id_cart DESC");
        $stmt->bind_param('i', $id_user);
        $stmt->execute();
        $stmt->bind_result($code_cart, $cart_status, $cart_date, $cart_number, $cart_cost, $cart_payment);

        $cart_histories = array();

        while ($stmt->fetch()){
            $cart_history = new stdClass();
            $cart_history->code_cart = $code_cart;
            $cart_history->cart_status = $cart_status;
            $cart_history->cart_date = $cart_date;
            $cart_history->cart_number = $cart_number;
            $cart_history->cart_cost = $cart_cost;
            $cart_history->cart_payment = $cart_payment;

            array_push($cart_histories, $cart_history);
        }
        return $cart_histories;
    }
}

?>