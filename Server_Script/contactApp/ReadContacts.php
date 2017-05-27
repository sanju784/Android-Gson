<?php

require 'init.php';

$sql = "SELECT * FROM contacts";

$result = mysqli_query($con, $sql);
$response = array();

while ($row = mysqli_fetch_array($result))
{
	array_push($response, array('name' => $row['name'], 'email' => $row['email']));
}

echo json_encode($response);

mysqli_close($con);


?>