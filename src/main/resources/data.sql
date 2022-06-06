

INSERT INTO `usuarios` VALUES (100000,'gerentes','calle gerente','12345678G','gerente@gerente.com','1999-07-18','gerente','$2a$12$Bzs5tApq5iEJ9sVdt1mjy.zSiEUODsKajscptpMoISNpesaVAt6bG','GERENTE','gerente');

INSERT INTO `usuarios` VALUES (100001,'García Marchena','C/Prim 26','21150678Q','manuelgarciamarchena99@gmail.com','1999-07-18','Manuel','$2a$12$LuaWW0lIg6gR7Me2DGWTn.pZXClrE/j5OqgouV4Dv8o4sDClPP.dG','CLIENTE','mangarmar17');


INSERT INTO `negocios` VALUES (100000,'A12345678','Plz San Fernando','Forum',1, 100000);

INSERT INTO `productos` VALUES (100000, 'Botellin de Cerveza', 1.00, '0',1), (100001,  'Fanta de naranja', 1.80, '1', 1), (100002,  'Fanta de limón', 1.80, '1',1)
								,(100003,  'Gintonic', 5.00, '0',1),(100004,  'Ron Cola', 5.00, '0',1),(100005,  'Solomillo Whisky', 6.00, '2',1),(100006,  'Montadito de Filete', 2.50, '2',1)
								,(100007,  'Paquete de patatas', 1.50, '5',1),(100008,  'Calamares fritos', 3.00, '4',1);
                                
INSERT INTO `prod_neg` 	VALUES (100000,100000),(100000,100001),(100000,100002),(100000,100003),(100000,100004),(100000,100005),(100000,100006),(100000,100007),(100000,100008);
