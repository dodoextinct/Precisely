<?php
	include  'connect_to_db.php';
    $statement = $pdo->prepare("CREATE TABLE IF NOT EXISTS fb_posts (id INT AUTO_INCREMENT, message VARCHAR(2038), url VARCHAR(2038), image_link VARCHAR(2038), source_name VARCHAR(2038), source INT NOT NULL, timedate VARCHAR(255) NOT NULL, fb_id VARCHAR(255) NOT NULL, PRIMARY KEY(id), KEY(fb_id)) DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;");
    $statement->execute();
    $statement = $pdo->prepare("CREATE TABLE IF NOT EXISTS comments (id INT AUTO_INCREMENT, user_id INT NOT NULL, post_id INT NOT NULL, message VARCHAR(2038), PRIMARY KEY(id));");
    $statement->execute();
    $statement = $pdo->prepare("CREATE TABLE IF NOT EXISTS action (id INT AUTO_INCREMENT, action_id INT NOT NULL, user_id INT NOT NULL, post_id INT NOT NULL, PRIMARY KEY(id), UNIQUE unique_index (user_id, post_id));");
    $statement->execute();
    $statement = $pdo->prepare("CREATE TABLE IF NOT EXISTS count_action (post_id INT NOT NULL, counter INT NOT NULL, PRIMARY KEY(post_id), UNIQUE(post_id));");
    $statement->execute();
    
    $page_fb = $_POST["page_first_source"];
    $page_youtube = $_POST["page_second_source"];
    if ($page_fb == 0 && $page_youtube == 0) {
    	$sql = "SELECT A.*, B.counter from ((SELECT id, message, url, image_link, source_name, source, timedate from fb_posts ORDER BY id DESC LIMIT 10) union all (select * from youtube_posts ORDER BY id DESC LIMIT 2)) AS A LEFT JOIN count_action AS B ON A.id = B.post_id";
        $stmt = $pdo->prepare($sql);
        $stmt->execute();
    }else{
    	$sql = "SELECT A.*, B.counter from ((SELECT id, message, url, image_link, source_name, source, timedate from fb_posts WHERE id <= ? ORDER BY id DESC LIMIT 10) UNION ALL (SELECT id, message, url, image_link, source_name, source, timedate FROM youtube_posts WHERE id <= ? ORDER BY id DESC LIMIT 2)) AS A LEFT JOIN count_action AS B ON A.id = B.post_id";
        $stmt = $pdo->prepare($sql);
        $stmt->execute([$page_fb, $page_youtube]);
    }
    $data = $stmt->fetchAll(PDO::FETCH_ASSOC);
    $stmt->closeCursor();

    echo json_encode($data);
?>