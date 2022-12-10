<?php 
header('Content-Type: application/json; charset=utf-8');
$dsn = "mysql: host=localhost;dbname=projeto_final;charset=utf8"; 
$usuario = "root"; 
$senha = "";

try {
    $pdo = new PDO ($dsn, $usuario, $senha);
} catch (PDOException $erro) { 
    header('HTTP/1.1 404 Not Found');
    echo json_encode(["msg" => "conexão com banco de dados não pôde ser efetuada"]);
    exit();
}
?>