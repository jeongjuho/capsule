<?php
    $con = mysqli_connect("localhost","ggcapsule","rhdrkazoqtbf12","ggcapsule");
    
    $id=$_POST["userId"];
    
    $result = mysqli_query($con,"SELECT num, C_title,C_content,C_id,C_image,C_date,C_gps from C_capsule where C_id='$id' ");
    
    /*$result2 = mysqli_query($con,"SELECT count(*) from C_capsule where C_id='$id' ");*/

    /*$response2=array();
    while($row = mysqli_fetch_array($result2)){
        array_push($response2, array("idNum"=>$row[0]));
    }
    echo json_encode(array("response2"=>$response2));*/
    
    $response=array();
    while($row = mysqli_fetch_array($result)){
        array_push($response, array("num"=>$row[0],"title"=>$row[1],"text"=>$row[2],"idStr"=>$row[3],"image"=>$row[4],"date"=>$row[5],"gps"=>$row[6]));
    }
    echo json_encode(array("response"=>$response));
    echo "id =$id";
    mysqli_close($con);
?>
        
        