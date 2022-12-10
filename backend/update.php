<?php
include "connection.php";

$id = $_POST['id'];
$nick = $_POST['nick'];
$nome = $_POST['nome'];
$senha = $_POST['senha'];
$uf = $_POST['uf'];
$telefone = $_POST['telefone'];
$idade = $_POST['idade'];
$generos = $_POST['generos'];

header('Content-Type: application/json; charset=utf-8');

$SQL = "UPDATE Usuario SET nick=:NICK, nome=:NOME, senha=:SENHA, telefone=:TELEFONE, uf=:UF, idade=:IDADE, generos=:GENEROS WHERE ID=:ID";

$stt = $pdo->prepare($SQL); 
$stt->bindParam(":ID", $id);
$stt->bindParam(':NICK', $nick); 
$stt->bindParam(':NOME', $nome); 
$stt->bindParam(':SENHA', $senha);
$stt->bindParam(':TELEFONE', $telefone); 
$stt->bindParam(':UF', $uf); 
$stt->bindParam(':IDADE', $idade);
$stt->bindParam(':GENEROS', $generos);

if ($stt->execute()) {
    $jsonOutput = ["update" => "ok", "id" => $id]; 

} else {
    header('HTTP/1.1 500 Internal Server Error');
    $jsonOutput = ["update" => "error"];
} 

echo json_encode($jsonOutput);
