<?php
    $con = mysqli_connect("localhost","ggcapsule","rhdrkazoqtbf12","ggcapsule");
    
    $cnt = $_POST["strNum"];
    
   
    $result = mysqli_query($con,"delete FROM C_capsule WHERE num = '$cnt' " );
  /*  $query = "delete FROM C_capsule WHERE num= $num";
    $result = mysqli_query($con, $query);*/
     
        
    if($result)
      echo "1";
    else
      echo "-1";
     
    mysqli_close($con);



?>
