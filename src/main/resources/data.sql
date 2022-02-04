

INSERT INTO `usuarios` VALUES (100000,'gerentes','calle gerente','12345678G','gerente@gerente.com','1999-07-18','gerente','$2a$12$Bzs5tApq5iEJ9sVdt1mjy.zSiEUODsKajscptpMoISNpesaVAt6bG','GERENTE','gerente');

INSERT INTO `negocios` VALUES (100000,'A12345678','Plz San Fernando','Forum',1, 100000);

INSERT INTO `productos` VALUES (100000, 'Botellin de Cerveza', 1.00, '0'), (100001,  'Fanta de naranja', 1.80, '1'), (100002,  'Fanta de limón', 1.80, '1')
								,(100003,  'Gintonic', 5.00, '0'),(100004,  'Ron Cola', 5.00, '0'),(100005,  'Solomillo Whisky', 6.00, '2'),(100006,  'Montadito de Filete', 2.50, '2')
								,(100007,  'Paquete de patatas', 1.50, '5'),(100008,  'Calamares fritos', 3.00, '4');
                                
INSERT INTO `prod_neg` 	VALUES (100000,100000),(100000,100001),(100000,100002),(100000,100003),(100000,100004),(100000,100005),(100000,100006),(100000,100007),(100000,100008);
