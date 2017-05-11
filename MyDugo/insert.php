<?php

$name = $_POST['name'];
$phone =$_POST['phone'];
$gender =$_POST['gender'];
$age = $_POST['age'];
$bloodgrp = $_POST['blood'];
$city = $_POST['city'];
$password = $_POST['password'];
$date = $_POST['date'];
$time = $_POST['time'];
$token = $_POST['token'];

include("dbconfig.php");

$result = @mysql_query("INSERT INTO UserDetail VALUES(null,'$name','$phone','$age','$gender','$bloodgrp','$city','$password','$token','$date','$time')");	
$response = array();
 if($result){
 	$last_id=@mysql_insert_id();
 	$response['UserId']=$last_id;
 	$response['success']=1;
 	$response['message']="Record Inserted Sucessfully";
 }else{
 	$response['success']=0;
 	$response['message']="Insertion Failure";
 }
 
 echo json_encode($response);

?>