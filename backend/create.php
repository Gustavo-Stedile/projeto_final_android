<?php
include "connection.php";

$nick = $_POST['nick'];
$nome = $_POST['nome'];
$senha = $_POST['senha'];
$uf = $_POST['uf'];
$telefone = $_POST['telefone'];
$idade = $_POST['idade'];
$generos = $_POST['generos'];

$SQL = "SELECT * FROM Usuario WHERE nick=:NICK";

$stt = $pdo->prepare($SQL); 
$stt->bindParam(":NICK", $nick);
$stt->execute();

if ($stt->rowCount() > 0) {
    $jsonOutput = ["create" => "error", "msg" => "nick já existe"];
    echo json_encode($jsonOutput);
    return; 
}

header('Content-Type: application/json; charset=utf-8');

$SQL = "INSERT INTO Usuario (nick, nome, senha, telefone, uf, idade, generos) VALUES (:NICK, :NOME, :SENHA, :TELEFONE, :UF, :IDADE, :GENEROS)";

$stt = $pdo->prepare($SQL); 
$stt->bindParam(':NICK', $nick); 
$stt->bindParam(':NOME', $nome); 
$stt->bindParam(':SENHA', $senha);
$stt->bindParam(':TELEFONE', $telefone); 
$stt->bindParam(':UF', $uf); 
$stt->bindParam(':IDADE', $idade);
$stt->bindParam(':GENEROS', $generos);

if ($stt->execute()) {
    $id = $pdo->lastInsertId();
    $jsonOutput = ["create" => "ok", "id" => $id]; 

} else {
    header('HTTP/1.1 500 Internal Server Error');
    $jsonOutput = ["create" => "error"];
} 

echo json_encode($jsonOutput);
