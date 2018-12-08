<?php
    $con = mysqli_connect("localhost","ggcapsule","rhdrkazoqtbf12","ggcapsule");
    
    
    $result = mysqli_query($con,"SELECT C_title,C_content,C_id,C_image,C_pimage,C_nick,C_private from C_capsule");
    $response=array();

	while($row = mysqli_fetch_array($result)){
  		array_push($response, array("title"=>$row[0],"text"=>$row[1],"id"=>$row[2],"image"=>$row[3],"pimage"=>$row[4]));
	}

    echo json_encode(array("response"=>$response));
?>
        
        