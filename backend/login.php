<?php
include "connection.php";

$nick = $_POST['nick'];
$senha = $_POST['senha'];

$SQL = "SELECT * FROM Usuario WHERE nick=:NICK";
$stt = $pdo->prepare($SQL);

$stt->bindParam(":NICK", $nick);
$stt->execute();

if ($stt->rowCount() == 0) {
    echo json_encode(["logged" => "false", "msg" => "usuário não encontrado"]);
    return;
}

$usuario = $stt->fetch(PDO::FETCH_OBJ);
if ($usuario->senha != $senha) {
    echo json_encode(["logged" => "false", "msg" => "email ou senha incorretos"]);
    return;
}

echo json_encode(["logged" => true, "id" => $usuario->id]);
