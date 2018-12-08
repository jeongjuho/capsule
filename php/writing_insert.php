<?
session_start();

?>

<?
    $con = mysqli_connect("localhost","ggcapsule","rhdrkazoqtbf12","ggcapsule");
    $title=$_POST["title"];
    $content=$_POST["content"];
    $date=$_POST["date"];
    $gps=$_POST["gps"];
	$id=$_POST["id"];
    $pt=$_POST["path"];
    $nick=$_POST["nick"];
    $profile=$_POST["profile"];
    $wdate=$_POST["wdate"];
    $strPrivate=$_POST["strPrivate"];
  

/*
    $query = "INSERT INTO C_capsule(C_tutle,C_content,C_nick) VALUES('$text','$date','$gps')";
    mysqli_query($con,$query);
mysqli_close($conn);
 */ 

    $statement = mysqli_prepare($con,"INSERT INTO C_capsule(C_title,C_content,C_id,C_gps,C_date,C_image,C_nick,C_pimage,C_rdate,C_private) VALUES(?,?,?,?,?,?,?,?,?,?)");
    mysqli_stmt_bind_param($statement,"ssssssssss",$title,$content,$id,$gps,$date,$pt,$nick,$profile,$wdate,$strPrivate);
    mysqli_stmt_execute($statement);
 
    $response=array();
    $response["success"]=true;

    echo json_encode($response);
	
?>