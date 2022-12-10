<?php
include "connection.php";

header('Content-Type: application/json; charset=utf-8');
$id = $_POST['id'];

$SQL = "DELETE FROM Usuario WHERE id=:ID";

$stt = $pdo->prepare($SQL);
$stt->bindparam(':ID', $id);

if ($stt->execute()) {
    $jsonOutput = ["delete" => "ok", "id" => $id]; 

} else {
    header('HTTP/1.1 500 Internal Server Error');
    $jsonOutput = ["create" => "error"];
} 

echo json_encode($jsonOutput);