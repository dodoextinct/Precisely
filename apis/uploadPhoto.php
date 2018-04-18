<?php

    include  'connect_to_db.php';
    if (is_uploaded_file($_FILES['bill']['tmp_name'])) {
    $uploads_dir = './scheduled_images';
    $tmp_name = $_FILES['bill']['tmp_name'];
    $pic_name = $_FILES['bill']['name'];
    move_uploaded_file($tmp_name, $uploads_dir.$pic_name);
    echo ("uploaded");
    }
    else{
       echo "File not uploaded successfully.";
    }

   ?>