<?php

$phone=$_POST['phone'];
$password=$_POST['password'];
$token = $_POST['token'];

include("dbconfig.php");

$result = @mysql_query("SELECT * from UserDetail WHERE PHONE='$phone' AND PASSWORD='$password'");	


$response =array();  



 if(@mysql_num_rows($result)>0){

 	$toke = @mysql_query("UPDATE UserDetail SET TOKEN = '$token' WHERE PHONE = '$phone'");
 	$response['students'] = array();
 	while($row=@mysql_fetch_array($result)){
		array_push($response['students'], $row);
	}
 	$response['message']="Login Sucessful";
 	//$response['token']=
 }else{
 	
 	$response['message']="Login Failure";
 }
 echo json_encode($response);

?>