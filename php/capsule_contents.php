<?php
    $con = mysqli_connect("localhost","ggcapsule","rhdrkazoqtbf12","ggcapsule");
    
    $num=$_POST["userNum"];
    
    $result = mysqli_query($con,"SELECT num,C_gps,C_date from C_capsule where num=$num ");
    
   

    
    $response=array();
    while($row = mysqli_fetch_array($result)){
        array_push($response, array("num"=>$row[0],"gps"=>$row[1],"date"=>$row[2]));
    }
    echo json_encode(array("response"=>$response));
    
    mysqli_close($con);
?>
        
        