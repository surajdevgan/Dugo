<?php
$response = array();
$donorId=$_POST['donor'];
$seekerId=$_POST['seeker'];
$requestId=$_POST['request'];
$date=$_POST['date'];

include('dbconfig.php');
include('fcm.php');


$fcm=new GCM();
$tokens=array();
$message="1 donor recieved";
$noti = array("message" => $message);

$result = @mysql_query("UPDATE Request SET DonorId='$donorId'WHERE RequestId='$requestId'");

$resNoti=@mysql_query("SELECT TOKEN FROM UserDetail WHERE ID='$seekerId'");

if(mysql_num_rows($resNoti)>0 and $result){
	while ($row=@mysql_fetch_array($resNoti)) {
		$dev_id=$row['token'];
		array_push($tokens, $dev_id);
	}
	$fcm->send_notification($tokens,$noti);
	$response["id"] = $seekerId;
	$response["success"] = 1;
	$response["message"] = "Notification Sent".$seekerId;
}else{
	$response["success"] = 0;
	$response["message"] = "Notification Not Sent";
}

echo json_encode($response);
?>					 