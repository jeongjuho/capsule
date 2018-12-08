<?php
    $con = mysqli_connect("localhost","ggcapsule","rhdrkazoqtbf12","ggcapsule");
    $result = mysqli_query($con,"SELECT num, C_title,C_content,C_id,C_image,C_gps,C_nick,C_pimage,C_private from C_capsule order by rand()");

    $response=array();
    while($row = mysqli_fetch_array($result)){
        array_push($response, array("num"=>$row[0],"title"=>$row[1],"text"=>$row[2],"idStr"=>$row[3],"image"=>$row[4],"gps"=>$row[5],"nick"=>$row[6],"pimage"=>$row[7],"strPrivate"=>$row[8]));
    }
    echo json_encode(array("response"=>$response));
    mysqli_close($con);
?>
        
        