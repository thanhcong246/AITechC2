<?php

class user
{
    //Database connection link
    private $con;

    //Class constructor
    function __construct()
    {
        //Getting the DbConnect.php file
        require_once dirname(__FILE__) . '/DbConnect.php';

        //Creating a DbConnect object to connect to the database
        $db = new DbConnect();

        //Initializing our connection link of this class
        //by calling the method connect of DbConnect class
        $this->con = $db->connect();
    }

    /*
    * The create operation
    * When this method is called a new record is created in the database
    */
    function createUsers($name, $email, $phone, $address, $password, $gender)
    {
        // Kiểm tra định dạng email
        if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
            return "Email không hợp lệ";
        }

        // Kiểm tra email đã tồn tại trong cơ sở dữ liệu chưa
        $stmt = $this->con->prepare("SELECT email FROM users WHERE email=?");
        $stmt->bind_param("s", $email);
        $stmt->execute();
        $result = $stmt->get_result();
        if ($result->num_rows > 0) {
            return "Email đã tồn tại";
        }


        // Kiểm tra name đã tồn tại trong cơ sở dữ liệu chưa
        $stmt = $this->con->prepare("SELECT name FROM users WHERE name=?");
        $stmt->bind_param("s", $name);
        $stmt->execute();
        $result = $stmt->get_result();
        if ($result->num_rows > 0) {
            return "Tên đã tồn tại";
        }
        // Tạo mật khẩu đã mã hóa
        $hashed_password = password_hash($password, PASSWORD_DEFAULT);
        // Thực hiện câu lệnh INSERT nếu không có trường nào trùng lặp
        $stmt = $this->con->prepare("INSERT INTO users (name, email, phone, address, password, gender) VALUES (?, ?, ?, ?, ?, ?)");
        $stmt->bind_param("ssssss", $name, $email, $phone, $address, $hashed_password, $gender);
        if ($stmt->execute()) {
            return true;
        } else {
            return "Lỗi khi thêm người dùng mới";
        }
    }

    /*
    * The read operation
    * When this method is called it is returning all the existing record of the database
    */
    function getUsers()
    {
        $stmt = $this->con->prepare("SELECT id, name, email, phone, address, password, gender FROM users");
        $stmt->execute();
        $stmt->bind_result($id, $name, $email, $phone, $address, $password, $gender);

        $users = array();

        while ($stmt->fetch()) {
            $user = array();
            $user['id'] = $id;
            $user['name'] = $name;
            $user['email'] = $email;
            $user['phone'] = $phone;
            $user['address'] = $address;
            $user['password'] = $password;
            $user['gender'] = $gender;

            array_push($users, $user);
        }

        return $users;
    }

    /*
    * The update operation
    * When this method is called the record with the given id is updated with the new given values
    */
    function updateUsers($id, $name, $email, $phone, $address, $password, $gender)
    {
        if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
            return "Email không hợp lệ";
        }
        $stmt = $this->con->prepare("SELECT name, email FROM users WHERE (email = ? OR name = ?) AND id != ? AND email NOT IN (SELECT email FROM users WHERE id = ?)");
        $stmt->bind_param("ssii", $email, $name, $id, $id);
        $stmt->execute();
        $result = $stmt->get_result();
        if ($result->num_rows > 0) {
            while ($row = $result->fetch_assoc()) {
                if ($row['email'] === $email) {
                    return "Email đã tồn tại";
                }
                if ($row['name'] === $name) {
                    return "Name đã tồn tại";
                }
            }
        }

        if (empty($password)) {
            $stmt = $this->con->prepare("UPDATE users SET name = ?, email = ?, phone = ?, address = ?, gender = ? WHERE id = ?");
            $stmt->bind_param("sssssi", $name, $email, $phone, $address, $gender, $id);
        } else {
            $stmt = $this->con->prepare("UPDATE users SET name = ?, email = ?, phone = ?, address = ?, password = ?, gender = ? WHERE id = ?");
            $hashed_password = password_hash($password, PASSWORD_DEFAULT);
            $stmt->bind_param("ssssssi", $name, $email, $phone, $address, $hashed_password, $gender, $id);
        }


        if ($stmt->execute())
            return true;
        return false;
    }

    function resetPassword($email, $newPassword)
    {
        $stmt = $this->con->prepare("SELECT id, email FROM users WHERE email = ?");
        $stmt->bind_param("s", $email);
        $stmt->execute();
        $result = $stmt->get_result();

        if ($result->num_rows == 0) {
            return "Email không tồn tại";
        }

        $row = $result->fetch_assoc();
        $id = $row['id'];
        $hashed_password = password_hash($newPassword, PASSWORD_DEFAULT);

        $stmt = $this->con->prepare("UPDATE users SET password = ? WHERE id = ?");
        $stmt->bind_param("si", $hashed_password, $id);

        if ($stmt->execute()) {
            return true;
        } else {
            return false;
        }
    }




    /*
    * The delete operation
    * When this method is called record is deleted for the given id
    */
    function deleteUsers($id)
    {
        $stmt = $this->con->prepare("DELETE FROM users WHERE id = ? ");
        $stmt->bind_param("i", $id);
        if ($stmt->execute())
            return true;

        return false;
    }

    function login($email, $password)
    {
        // Truy vấn cơ sở dữ liệu để tìm kiếm thông tin người dùng với email được cung cấp.
        $stmt = $this->con->prepare("SELECT password FROM users WHERE email = ?");
        $stmt->bind_param("s", $email);
        $stmt->execute();
        $stmt->store_result();

        // Kiểm tra xem email có tồn tại trong cơ sở dữ liệu không.
        if ($stmt->num_rows > 0) {

            // Lấy mật khẩu đã mã hóa được lưu trữ trong cơ sở dữ liệu.
            $stmt->bind_result($hashed_password);
            $stmt->fetch();

            // So sánh mật khẩu đã mã hóa được cung cấp với mật khẩu đã lưu trữ trong cơ sở dữ liệu.
            if (password_verify($password, $hashed_password)) {
                // Đăng nhập thành công.
                return true;
            } else {
                // Mật khẩu không chính xác.
                return "Mật khẩu không chính xác";
            }
        } else {
            // Email không tồn tại trong cơ sở dữ liệu.
            return "Email không tồn tại";
        }
    }

    function getUsersByEmail($email)
    {
        $stmt = $this->con->prepare("SELECT id, name, email, phone, address, password, gender FROM users WHERE email = ?");
        $stmt->bind_param("s", $email);
        $stmt->execute();
        $stmt->bind_result($id, $name, $email, $phone, $address, $password, $gender);

        $users = array();

        while ($stmt->fetch()) {
            $user = array();
            $user['id'] = $id;
            $user['name'] = $name;
            $user['email'] = $email;
            $user['phone'] = $phone;
            $user['address'] = $address;
            $user['password'] = $password;
            $user['gender'] = $gender;

            array_push($users, $user);
        }

        return $users;
    }

    function getUsersByName($name)
    {
        $stmt = $this->con->prepare("SELECT id, name, email, phone, address, password, gender FROM users WHERE name = ?");
        $stmt->bind_param("s", $name);
        $stmt->execute();
        $stmt->bind_result($id, $name, $email, $phone, $address, $password, $gender);

        $users = array();

        while ($stmt->fetch()) {
            $user = array();
            $user['id'] = $id;
            $user['name'] = $name;
            $user['email'] = $email;
            $user['phone'] = $phone;
            $user['address'] = $address;
            $user['password'] = $password;
            $user['gender'] = $gender;

            array_push($users, $user);
        }

        return $users;
    }

    function getUsersById($id)
    {
        $stmt = $this->con->prepare("SELECT id, name, email, phone, address, password, gender, image FROM users WHERE id = ?");
        $stmt->bind_param("i", $id);
        $stmt->execute();
        $stmt->bind_result($id, $name, $email, $phone, $address, $password, $gender, $image);

        $users = array();

        while ($stmt->fetch()) {
            $user = array();
            $user['id'] = $id;
            $user['name'] = $name;
            $user['email'] = $email;
            $user['phone'] = $phone;
            $user['address'] = $address;
            $user['password'] = $password;
            $user['gender'] = $gender;
            $user['image'] = $image;


            array_push($users, $user);
        }

        return $users;
    }
}