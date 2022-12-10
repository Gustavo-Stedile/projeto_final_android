<?php
include "connection.php";

$id = $_GET['id'];

$SQL = "SELECT * FROM Usuario WHERE id=:ID";
$stt = $pdo->prepare($SQL);

$stt->bindParam(":ID", $id);
$stt->execute();

$usuario = $stt->fetch(PDO::FETCH_OBJ);
$jsonOutput = [
    "id" => $usuario->id, 
    "nick" => $usuario->nick,
    "nome" => $usuario->nome,
    "telefone" => $usuario->telefone,
    "uf" => $usuario->uf,
    "generos" => $usuario->generos,
    "idade" => $usuario->idade
];

echo json_encode($jsonOutput);