<?php

require 'init.php';

$contact = $_POST['contact'];
$contact = json_decode($contact);

$name = $contact->{'name'};
$email = $contact->{'email'};
$profile_pic = $contact->{'profile_pic'};
$upload_path = "upload/$name.jpg";

$sql = "INSERT INTO contacts (id, name, email) VALUES (NULL, '$name', '$email');";

if (mysqli_query($con, $sql))
{
	file_put_contents($upload_path, base64_decode($profile_pic));
	echo json_encode(array('response' => 'Registration Success'));
}
else
{
	echo json_encode(array('response' => 'Registration Failed'));
}

mysqli_close($con);

?>