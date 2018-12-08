<?
	$con = mysqli_connect("localhost","ggcapsule","rhdrkazoqtbf12","ggcapsule");
	
	$userID = $_POST["userID"];
	$userPassword =$_POST["userPass"];
	
	$statement = mysqli_prepare($con, "SELECT * FROM member WHERE M_id = ? AND M_pass = ?");
	mysqli_stmt_bind_param($statement, "ss", $userID, $userPassword);
	mysqli_stmt_execute($statement);

	mysqli_stmt_store_result($statement);
	//mysqli_stmt_bind_result($statement, $userID);

	$response = array();
	$response["success"] = false;
	
	while(mysqli_stmt_fetch($statement)){
		$response["success"] = true;
		$response["userID"] = $userID;
}

	echo json_encode($response);
	
?>