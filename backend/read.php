<?php
include "connection.php";

header('Content-Type: application/json; charset=utf-8');

$SQL = "SELECT * FROM Usuario";
$res = $pdo->query($SQL);

$jsonOutput = [];
while ($usuario = $res->fetch(PDO::FETCH_OBJ)) {
    $jsonOutput[] = [
        "id" => $usuario->id, 
        "nick" => $usuario->nick,
        "nome" => $usuario->nome,
        "telefone" => $usuario->telefone,
        "uf" => $usuario->uf,
        "generos" => $usuario->generos,
        "idade" => $usuario->idade
    ];
}

echo json_encode($jsonOutput);