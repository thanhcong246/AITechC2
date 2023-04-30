<?php

class Shipping{
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

    function setShippingAddress($name, $name_user, $phone_user, $address_user, $note_user){
        // lấy id_user dựa vào name_user
        $stmt_get_id_user = $this->con->prepare("SELECT id FROM users WHERE name=?");
        $stmt_get_id_user->bind_param("s", $name);
        $stmt_get_id_user->execute();
        $result = $stmt_get_id_user->get_result();
        $row = $result->fetch_assoc();
        $id_user = $row["id"];

        // kiểm tra xem id_user đã tồn tại trong bảng shipping hay chưa
        $stmt_check_id_user = $this->con->prepare("SELECT COUNT(*) AS count FROM shipping WHERE id_user=?");
        $stmt_check_id_user->bind_param("i", $id_user);
        $stmt_check_id_user->execute();
        $result_check_id_user = $stmt_check_id_user->get_result();
        $row_check_id_user = $result_check_id_user->fetch_assoc();

        // nếu id_user đã tồn tại trong bảng shipping, thực hiện UPDATE
        if ($row_check_id_user["count"] > 0) {
            $stmt_update_shipping = $this->con->prepare("UPDATE shipping SET name=?, phone=?, address=?, note=? WHERE id_user=?");
            $stmt_update_shipping->bind_param("ssssi", $name_user, $phone_user, $address_user, $note_user, $id_user);
            if (!$stmt_update_shipping->execute()) {
                return "Error occurred while updating address in Shipping";
            }
            // Lấy id_shipping từ bảng shipping và trả về kết quả
            $stmt_get_shipping_id = $this->con->prepare("SELECT id_shipping FROM shipping WHERE id_user=?");
            $stmt_get_shipping_id->bind_param("i", $id_user);
            $stmt_get_shipping_id->execute();
            $result_get_shipping_id = $stmt_get_shipping_id->get_result();
            $row_get_shipping_id = $result_get_shipping_id->fetch_assoc();
            return $row_get_shipping_id['id_shipping'];
        }
        // nếu id_user chưa tồn tại trong bảng shipping, thực hiện INSERT
        else {
            $stmt_insert_shipping = $this->con->prepare("INSERT INTO shipping (name, phone, address, note, id_user) VALUES (?, ?, ?, ?, ?)");
            $stmt_insert_shipping->bind_param("ssssi", $name_user, $phone_user, $address_user, $note_user, $id_user);
            if (!$stmt_insert_shipping->execute()) {
                return "Error occurred while adding address to Shipping";
            }
            // Lấy id_shipping từ bảng shipping và trả về kết quả
            $id_shipping = $stmt_insert_shipping->insert_id;
            return $id_shipping;
        }
    }

    function getShippingAddress($name){
        // lấy id_user dựa vào name_user
        $stmt_get_id_user = $this->con->prepare("SELECT id FROM users WHERE name=?");
        $stmt_get_id_user->bind_param("s", $name);
        $stmt_get_id_user->execute();
        $result = $stmt_get_id_user->get_result();
        $row = $result->fetch_assoc();
        $id_user = $row["id"];

        // kiểm tra xem id_user đã tồn tại trong bảng shipping hay chưa
        $stmt_check_id_user = $this->con->prepare("SELECT COUNT(*) AS count FROM shipping WHERE id_user=?");
        $stmt_check_id_user->bind_param("i", $id_user);
        $stmt_check_id_user->execute();
        $result_check_id_user = $stmt_check_id_user->get_result();
        $row_check_id_user = $result_check_id_user->fetch_assoc();

        // nếu id_user đã tồn tại trong bảng shipping,
        if ($row_check_id_user["count"] > 0){
            $stmt = $this->con->prepare("SELECT id_shipping, name, phone, address, note FROM shipping WHERE id_user = ?");
            $stmt->bind_param("i", $id_user);
            $stmt->execute();
            $stmt->bind_result($id_shipping, $name_user, $phone_user, $address_user, $note_user);

            $results = array();

            while ($stmt->fetch()) {
                $shipping = new stdClass();
                $shipping->id_shipping = $id_shipping;
                $shipping->name = $name_user;
                $shipping->phone = $phone_user;
                $shipping->address = $address_user;
                $shipping->note = $note_user;
                array_push($results, $shipping);
            }
            return $results;
        }
    }
}

?>