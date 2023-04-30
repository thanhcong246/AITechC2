<?php
class TableCreator
{
    private $con;

    function __construct($con)
    {
        $this->con = $con;
    }

    function createTable($tableName, $fields)
    {
        //Creating table query
        $query = "CREATE TABLE $tableName (id INT(11) UNSIGNED AUTO_INCREMENT PRIMARY KEY";

        foreach ($fields as $fieldName => $fieldType) {
            $query .= ", $fieldName $fieldType";
        }

        $query .= ")";

        //Executing query
        if ($this->con->query($query) === TRUE) {
            echo "Table $tableName created successfully";
        } else {
            echo "Error creating table: " . $this->con->error;
        }
    }
}