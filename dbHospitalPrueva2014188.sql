create database dbHospitalPrueva2014188;
use dbHospitalPrueva2014188;

create table Medicos(
	codigoMedico int not null primary key auto_increment,
	licenciaMedica varchar(30)not null,
	nombres varchar(30)not null,	
	apellidos varchar(30)not null,
	horaEntrada datetime,
	horaSalidad datetime,
	turnoMaximo int(10),
	sexo varchar(15)not null
);

create table telefonosMedicos(
	codigoTelefonoMedico int not null primary key auto_increment,
	telefonoPersonal varchar(15)not null,
	telefonoTrabajo varchar(15)not null,
	codigoMedico int not null,
	foreign key(codigoMedico)references Medicos(codigoMedico)on delete cascade
);	

create table Horarios(
	codigoHorario int not null primary key auto_increment,
	horarioInicio datetime not null,
	horarioSalida datetime not null,
	lunes tinyint,
	martes tinyint,
	miercoles tinyint,
	jueves tinyint,
	viernes tinyint);

create table Especialidades(
	codigoEspecialidad int not null primary key auto_increment,
	nombreEspecialidad varchar (50) not null
);


create table Medico_Especialidad(
	codigoMedicoEspecialidad int not null primary key auto_increment,
	codigoMedico int not null,
	codigoEspecialidad int not null unique,
	codigoHorario int not null,
	foreign key (codigoMedico)references Medicos(codigoMedico) on delete cascade,
	foreign key (codigoEspecialidad)references Especialidades(codigoEspecialidad) ,
	foreign key (codigoHorario)references Horarios(codigoHorario) on delete cascade);


create table Pacientes(
	codigoPacientes int not null primary key auto_increment,
	DPI varchar(20)not null,
	apellidos varchar(50)not null,
	nombres varchar(50)not null,
	fechaNacimiento date not null,
	edad int not null,
	direccion varchar(150)not null,
	ocupacion varchar(50)null);

alter table Pacientes add sexo varchar(30) check(sexo='Masculino'or sexo='Femenino');

create table contactoUrgencia(
	codigoContactoUrgencia int not null primary key auto_increment,
	nombres varchar(30)not null ,
	apellidos varchar(30)not null,
	numeroContacto varchar(10)not null,
	codigoPacientes int not null unique,	
	foreign key(codigoPacientes)references Pacientes(codigoPacientes));


create table Areas(
	codigoArea int not null primary key auto_increment,
	nombreArea varchar(100)not null);

create table Cargos(
	codigoCargo int not null primary key auto_increment,
	nombreCargo varchar(30)not null);

create table ResposableTurno(
	codigoResposableTurno int not null primary key auto_increment,
	nombreResposable varchar(50)not null,
	apellidoResposable varchar(50)not null,
	telefonoResposable varchar(50)not null,
	codigoArea int not null unique,
	codigoCargo int not null unique ,
	foreign key(codigoArea)references Areas(codigoArea)on delete cascade,
	foreign key(codigoCargo)references Cargos(codigoCargo)on delete cascade
);

create table Turno(
	codigoTurno int not null primary key auto_increment,
	fechaTurno date,
	fechaCita date,
	valorCita decimal(10,2)not null,
	codigoMedicoEspecialidad int not null unique ,
	codigoResposableTurno int not null unique,
	codigoPacientes int not null unique,
	foreign key(codigoMedicoEspecialidad)references Medico_Especialidad(codigoMedicoEspecialidad)on delete cascade,
	foreign key(codigoResposableTurno)references ResposableTurno(codigoResposableTurno)on delete cascade,
	foreign key(codigoPacientes)references Pacientes(codigoPacientes)on delete cascade
);


create table ControlCitas(
	codigoControlCita int not null primary key auto_increment,
    fecha Date, 
    horaInicio varchar(50) not null,
    horaFin varchar(50) not null, 
    codigoMedico int not null unique,
    codigoPacientes int not null unique,
    foreign key(codigoMedico) references Medicos(codigoMedico) on delete cascade,
    foreign key(codigoPacientes) references Pacientes(codigoPacientes) on delete cascade 
    
);



create table Recetas(
	codigoReceta int not null primary key auto_increment,
    descripcionReceta varchar(50) not null,
    codigoControlCita int not null unique, 
    foreign key(codigoControlCita) references ControlCitas(codigoControlCita)

);

#****************************ControlCitas**********************************

Delimiter $$
create procedure sp_AgregarControlCitas(p_fecha Date, p_horaInicio varchar(50), p_horaFin varchar(50), p_codigoMedico int, p_codigoPacientes int )
	begin 
		insert into ControlCitas(fecha, horaInicio, horaFin, codigoMedico, codigoPacientes  )
			values(p_fecha, p_horaInicio, p_horaFin, p_codigoMedico, p_codigoPacientes );
	end $$
   
call sp_AgregarControlCitas('2000-06-13', '8:00 AM', '6:00 PM', 1, 1);

drop procedure sp_AgregarControlCitas;

select * from ControlCitas;

Delimiter $$
create procedure sp_ModificarControlCitas(p_codigoControlCita int, p_fecha Date, p_horaInicio varchar(50), p_horaFin varchar(50), p_codigoMedico int, p_codigoPacientes int )
begin 
	update ControlCitas
    set fecha = p_fecha, horaInicio = p_horaInicio, horaFin = p_horaFin, codigoMedico = p_codigoMedico, codigoPacientes = p_codigoPacientes
    where codigoControlCita = p_codigoControlCita;
end $$

call sp_ModificarControlCitas(2, '2019-03-12', '7:00 AM' , '5:00 PM', 2,2);
    
Delimiter $$
create procedure sp_EliminarControlCitas(p_codigoControlCita int )
	begin
		delete  from ControlCitas where codigoControlCita = p_codigoControlCita;
	end $$
    
call sp_EliminarControlCitas(1);

Delimiter $$
create procedure sp_BuscaControlCitas(p_codigoControlCita int)	
	Begin
		select * from ControlCitas where  codigoControlCita = p_codigoControlCita;
	End $$
    
call sp_BuscaControlCitas(1);


delimiter $$
create procedure sp_ListaControlCitas()
	begin
		select codigoControlCita,fecha, horaInicio, horaFin, codigoMedico, codigoPacientes from ControlCitas;
	end $$
    
call sp_ListaControlCitas();

#**************************************Recetas********************************
Delimiter $$
create procedure sp_Recetas(p_descripcionReceta varchar(50), p_codigoControlCita int )
	begin 
		insert into Recetas(descripcionReceta, codigoControlCita)
			values(p_descripcionReceta, p_codigoControlCita);
	end $$

call sp_Recetas('tomas 2 ubuprofenos', 1);

Delimiter $$
create procedure sp_ModificarReceta(p_codigoReceta int, p_descripcionReceta varchar(50), p_codigoControlCita int  )
begin 
	update Recetas
    set descripcionReceta = p_descripcionReceta, codigoControlCita = p_codigoControlCita
    where codigoReceta = p_codigoReceta;
end $$

call sp_ModificarReceta(2,'3 aspirinas de 200ml al dia',2 );
   
select * from Recetas;

Delimiter $$
create procedure sp_EliminarReceta(p_codigoReceta int)
begin 
	delete from Recetas where codigoReceta = p_codigoReceta;
end $$ 
    
call sp_EliminarReceta(1);

Delimiter $$
create procedure sp_buscarReceta(p_codigoReceta int)
	begin 
		select * from Recetas where codigoReceta = p_codigoReceta;
	end $$

call sp_buscarReceta(2);

Delimiter $$
create procedure sp_ListarReceta()
	begin 
		select codigoReceta, descripcionReceta, codigoControlCita from Recetas;
	end $$

call sp_ListarReceta(); 



#****************************************Medicos**********************************
    
Delimiter $$
create procedure AgregarMedico(  p_licenciaMedica int ,p_nombres  varchar(30),p_apellidos varchar(30), p_horaEntrada datetime, p_horaSalidad datetime, p_sexo varchar(30))
	begin
		insert into Medicos(licenciaMedica, nombres, apellidos, horaEntrada, horaSalidad,  sexo)
			values(p_licenciaMedica,p_nombres,p_apellidos,p_horaEntrada,p_horaSalidad,p_sexo);
	end $$

call AgregarMedico(127456,'Ana  ','Sofia','2019-02-01 11:00:00','2019-02-01 06:00:07','Femenino');
call AgregarMedico(127456,'Oralia','Perez','2019-02-03 07:04:55 ','2019-02-04 08:45:05','Femenino');
call AgregarMedico(127456,'Miguel ','Perez','2019-02-06 05:00:00','2019-02-04 12:55:00 ','Masculino');
call AgregarMedico(127456,'jason ','Gatica','2019-02-06 03:00:00','2019-02-04 06:00:00','Femenino');

select * from Medicos;

Delimiter $$
create procedure ModificarMedico(p_codigoMedico int,p_licenciaMedica int ,p_nombres  varchar(30),p_apellidos varchar(30),p_horaEntrada datetime, p_horaSalidad datetime,  p_sexo varchar(30))
	begin
		update Medicos
		set licenciaMedica=p_licenciaMedica,nombres=p_nombres, apellidos=p_apellidos,horaEntrada=p_horaEntrada,horaSalidad=p_horaSalidad, sexo=p_sexo 
        where codigoMedico=p_codigoMedico;
	end $$

call ModificarMedico(1,908975,'Luis Fernado','Garcia Ramirez','2019-02-01 08:50:50','2019-02-01 08:50:50','Masculino');
call ModificarMedico(2,908975,'Billy ','Garcia Cardenas','2019-02-01 08:50:50','2019-02-01 08:50:50','Masculino');


Delimiter $$
create procedure EliminarMedicos(p_codigoMedico int )
	begin
		delete  from Medicos where codigoMedico=p_codigoMedico;
	end $$

call EliminarMedicos(1);
call EliminarMedicos(2);
call EliminarMedicos(3);
call EliminarMedicos(4);

Delimiter $$
create procedure sp_BuscaMedico	(p_codigoMedico int)	
	Begin
		select * from Medicos where  codigoMedico =p_codigoMedico;
	End $$
    
call sp_BuscaMedico(1);

delimiter $$
create procedure sp_ListaMedicos()
	begin
		select codigoMedico,licenciaMedica, nombres, apellidos, horaEntrada, horaSalidad, sexo from Medicos;
	end $$
    
call sp_ListaMedicos();



#***************************telefonosMedicos***************************

ALTER user 'root'@'localhost' Identified with mysql_native_password By 'admin';

Delimiter $$
create procedure Agregar_telefonosmedicos(p_telefonoPersonal varchar(15), p_telefonoTrabajo varchar(15), p_codigoMedico int(11))
	begin
	insert into telefonosmedicos(telefonoPersonal, telefonoTrabajo, codigoMedico)
		values(p_telefonoPersonal, p_telefonoTrabajo, p_codigoMedico);
	end $$

call Agregar_telefonosmedicos('2433-6363','4205-9932',1);
call Agregar_telefonosmedicos('2433-8746','4205-7890',2);

select * from telefonosMedicos;

Delimiter $$
create procedure modificar_telefonosmedicos(p_codigoTelefonoMedico int ,p_telefonoPersonal varchar(15),p_codigoMedico int(11))
	begin
		update telefonosmedicos
		set telefonoPersonal=p_telefonoPersonal,telefonoTrabajo = p_telefonoPersonal,codigoMedico=p_codigoMedico 
		where codigoTelefonoMedico = p_codigoTelefonoMedico;
	end $$

call modificar_telefonosmedicos(1,'1224-5675',1);
call modificar_telefonosmedicos(2,'2345-4567',2);

Delimiter $$
create procedure eliminar_telefonosmedicos(p_codigoTelefonoMedico int )
	begin
		delete from telefonosmedicos where codigoTelefonoMedico=p_codigoTelefonoMedico;
	end $$
    
call eliminar_telefonosmedicos(1);
call eliminar_telefonosmedicos(2);


Delimiter $$
create procedure sp_BuscartelefonosMedicos(p_codigoTelefonoMedico int)
	begin 
		select * from telefonosMedicos where codigoTelefonoMedico = p_codigoTelefonoMedico;
	end $$

call sp_BuscartelefonosMedicos(2);

delimiter $$
create procedure sp_Listartelefonosmedicos()
	begin
	select * from telefonosmedicos;
	end $$
    
call sp_telefonosmedicos(); 
    
    
#************************Horarios**************************************
Delimiter $$
create procedure Agregarhorarios(p_horarioInicio datetime, p_horarioSalida datetime, p_lunes tinyint, p_martes tinyint, p_miercoles tinyint,p_jueves tinyint, p_viernes tinyint)
	begin
	insert into horarios(horarioInicio, horarioSalida, lunes, martes, miercoles, jueves, viernes) 
	values (p_horarioInicio,p_horarioSalida,p_lunes,p_martes,p_miercoles,p_jueves,p_viernes);
	end $$

call Agregarhorarios('2019-02-01 09:01:01','2019-02-02 09:01:01',1,1,1,1,1);
call Agregarhorarios('2019-02-03 09:01:01','2019-02-04 09:01:01',1,1,1,1,1);

select * from Horarios;

/*Delimiter $$
create procedure Modificarhorarios(p_codigoHorario int,p_horarioInicio datetime, p_horarioSalida datetime)
	begin
		update horarios
		set horarioInicio=p_horarioInicio, horarioSalida=p_horarioSalida where codigoHorario=p_codigoHorario;
	end $$

call Modificarhorarios(1,'2019-03-02 08:05:09', '2019-03-02 08:02:05');
call Modificarhorarios(2,'2019-03-02 07:05:09', '2019-03-02 03:02:05');*/

DELIMITER $$
	create procedure sp_ModficarHorarios(p_codigoHorario int, p_horarioInicio datetime, p_horarioSalida datetime, p_lunes tinyint,p_martes tinyint, p_miercoles tinyint, p_jueves tinyint, p_viernes tinyint)
BEGIN
	update Horarios
		set horarioInicio = p_horarioInicio, horarioSalida = p_horarioSalida, lunes = p_lunes,martes = p_martes, miercoles = p_miercoles,jueves = p_jueves, viernes = p_viernes
		where codigoHorario = p_codigoHorario;
END $$



Delimiter $$
create procedure Eliminarhorarios(p_codigoHorario int)
	begin
		delete from horarios where codigoHorario=p_codigoHorario;
	end $$

call Eliminarhorarios(1);

Delimiter $$
create procedure sp_BuscarHorarios(p_codigoHorario int )
	begin 
		select * from Horarios where codigoHorario = p_codigoHorario;
	end $$

call sp_BuscarHorarios(2);


Delimiter $$
create procedure sp_Elistarhorarios()
	begin
		select * from Horarios;
	end $$
    
call sp_Elistarhorarios();

#*****************************Especialidades**************************

Delimiter $$
create procedure sp_Especialidades(p_nombreEspecialidad varchar (50))
	begin
		insert into Especialidades(nombreEspecialidad)
			values(p_nombreEspecialidad);
	end $$

call sp_Especialidades('cirujano');
call sp_Especialidades('Pediatra');


select * from Especialidades;

Delimiter $$
create procedure Modificar_especialidades(p_codigoEspecialidad int,p_nombreEspecialidad varchar(30))
	begin
		update especialidades
		set nombreEspecialidad=p_nombreEspecialidad where codigoEspecialidad=p_codigoEspecialidad;
	end $$

call Modificar_especialidades(1,'Neorulogia');
call Modificar_especialidades(2,'Cirujano');

Delimiter $$
create procedure Eliminar_especialidades(p_codigoEspecialidad int)
	begin
		delete from especialidades where codigoEspecialidad=p_codigoEspecialidad;
	end $$

call Eliminar_especialidades(1);
call Eliminar_especialidades(2);

Delimiter $$
create procedure sp_BuscarEspecialidades(p_codigoEspecialidad int)
	begin
		select * from Especialidades where codigoEspecialidad = p_codigoEspecialidad;
	end $$
    
call sp_BuscarEspecialidades(2);

Delimiter $$
create  procedure Elistar_especialidades()
	begin
		select * from Especialidades ;
	end$$

call Elistar_especialidades();

#*************************MedicoEspecialidad***************************

Delimiter $$
create procedure Agregarmedico_especialidad(p_codigoMedico int, p_codigoEspecialidad int , p_codigoHorario int)
	begin
	insert into medico_especialidad(codigoMedico, codigoEspecialidad, codigoHorario)
		values(p_codigoMedico,p_codigoEspecialidad,p_codigoHorario);
	end $$ 

call Agregarmedico_especialidad(1,1,1);
call Agregarmedico_especialidad(1,2,1);
call Agregarmedico_especialidad(2,4,1);

select * from Medico_Especialidad;

Delimiter $$
create procedure Modifcar_medico_especialidad(p_codigoMedicoEspecialidad int, p_codigoMedico int, p_codigoEspecialidad int, p_codigoHorario int)
	begin
	update medico_especialidad
	set codigoMedico=p_codigoMedico, codigoEspecialidad=p_codigoEspecialidad,codigoHorario=p_codigoHorario 
	where codigoMedicoEspecialidad=p_codigoMedicoEspecialidad;
	end $$

call Modifcar_medico_especialidad(1,1,3,1);
call Modifcar_medico_especialidad(2,1,5,1);

Delimiter $$
create procedure Eliminar_medico_especialidad(p_codigoMedicoEspecialidad int)
	begin
	delete from medico_especialidad where codigoMedicoEspecialidad=p_codigoMedicoEspecialidad;
	end $$

call Eliminar_medico_especialidad(1);
call Eliminar_medico_especialidad(2);

Delimiter $$
create procedure Elistar_medico_especialidad()
	begin
	select * from medico_especialidad;
	end $$

call Elistar_medico_especialidad();


Delimiter $$
create procedure sp_buscarMedicoEspecialida(p_codigoMedicoEspecialidad int )
begin
	select * from Medico_Especialidad where codigoMedicoEspecialidad = p_codigoMedicoEspecialidad;
end $$

call sp_buscarMedicoEspecialida(1);

#***************************pacientes **********************************


    
Delimiter $$  
	create procedure sp_Pacientes(p_DPI varchar (20), p_apellidos varchar(50), p_nombres varchar(50), p_fechaNacimiento date, p_edad int, p_direccion varchar(150), p_ocupacion varchar(50), p_sexo varchar(30)) 
		begin 
			insert into Pacientes(DPI, apellidos, nombres, fechaNacimiento, edad, direccion, ocupacion, sexo)
				values (p_DPI, p_apellidos, p_nombres, p_fechaNacimiento, p_edad, p_direccion, p_ocupacion, p_sexo);
		end $$

call sp_Pacientes('2745-11118-0101', 'Swift', 'Brandon javier', '2000-06-01' , 19 , '3ra Av.5-85 zona 16', 'Electricista', 'Masculino'  );
call sp_Pacientes('2771-11118-0101', 'rodas ', 'Fabiola', '2000-06-01' , 19 , '3ra Av.5-85 zona 16', 'Electricista', 'Femenino'  );
   

Delimiter $$
create procedure sp_ModificarPacientes(p_codigoPacientes int, p_DPI varchar (20), p_apellidos varchar(50), p_nombres varchar(50), p_fechaNacimiento date, p_edad int, p_direccion varchar(150), p_ocupacion varchar(50), p_sexo varchar(30)  )
	begin 
		update Pacientes
        set DPI = p_DPI, apellidos= p_apellidos, nombres = p_nombres, fechaNacimiento = p_fechaNacimiento, edad = p_edad , direccion = p_direccion, ocupacion = p_ocupacion, sexo = p_sexo
		where codigoPacientes = p_codigoPacientes;
	end $$

call sp_ModificarPacientes (1, '2745-11118-0101','Grande ', 'Taylor Wonderland', '1989-05-04' , 29 , '3ra Av.5-85 zona 18', 'Cantante', 'Femenino'  );  
call sp_ModificarPacientes (2, '2748-44158-0101','Carson ', 'Sofia', '1999-05-04' , 20 , '3ra Av.5-85 zona 19', 'Cantante', 'Femenino'  ); 

Delimiter $$ 
create procedure EliminarPacientes(p_codigoPacientes int)
	begin 
		delete from Pacientes 
		where codigoPacientes = p_codigoPacientes; 
	end $$

call EliminarPacientes();

Delimiter $$
create procedure sp_BuscarPacientes(p_codigoPacientes int)
	begin 
		select * from Pacientes where codigoPacientes = p_codigoPacientes;
	end $$

call sp_BuscarPacientes(2);

Delimiter $$ 
create procedure sp_ListaPacientes()
	begin 
		select  codigoPacientes, DPI, apellidos, nombres, fechaNacimiento, edad, direccion, ocupacion, sexo 
		from Pacientes;
end $$

call sp_ListaPacientes();


#*************************** ContactoUrgencia*************************


Delimiter $$ 
	create procedure sp_contactoUrgencia(p_nombres varchar(30),p_apellidos varchar(30), p_numeroContacto varchar(10),p_codigoPacientes int )
		begin
			insert into contactoUrgencia(nombres, apellidos,numeroContacto, codigoPacientes  ) 
				values( p_nombres,p_apellidos,p_numeroContacto, P_codigoPacientes);  
		end $$ 

call sp_contactoUrgencia('Sabrina ', 'Carpenter', '45878595', 1 ); 
call sp_contactoUrgencia('jen ', 'Selter', '45847122', 2 ); 


select * from contactoUrgencia; 

Delimiter $$ 
create procedure sp_ModificarContactoUrgencia(p_codigoContactoUrgencia int, p_nombres varchar(30),p_apellidos varchar(30), p_numeroContacto varchar(10),p_codigoPacientes int )
	begin 
		update contactoUrgencia
        set nombres = p_nombres, apellidos = p_apellidos, numeroContacto = numeroContacto, codigoPacientes = p_codigoPacientes
        where codigoContactoUrgencia = p_codigoContactoUrgencia;
	end $$

call sp_ModificarContactoUrgencia(2,'Sebastian', 'Martinez', '45872569', 2 );
call sp_ModificarContactoUrgencia(6,'Camila', 'Cabello', '36897825', 1 );

Delimiter $$ 
create procedure EliminarContactoUrgencia(p_codigoContactoUrgencia int)
	begin 
		delete from contactoUrgencia where codigoContactoUrgencia = p_codigoContactoUrgencia;
	end;

call EliminarContactoUrgencia(15);

Delimiter $$ 
create procedure sp_BuscarContactoUrgencia(p_codigoContactoUrgencia int )
	begin
		select * from contactoUrgencia where codigoContactoUrgencia = p_codigoContactoUrgencia;
	end $$

call sp_BuscarContactoUrgencia(1);


Delimiter $$ 
create procedure sp_ListacontactoUrgencia()
begin 
	select codigoContactoUrgencia, nombres, apellidos,numeroContacto, codigoPacientes
	from contactoUrgencia;
end $$

call sp_ListacontactoUrgencia();

#****************Areas********************
    
Delimiter $$
create procedure sp_Areas(p_nombreArea varchar(100))
	begin 
		insert into Areas(nombreArea) 
			values(p_nombreArea); 
end $$

call sp_Areas('Recursos Humanos');
call sp_Areas(' Contabilidad');

select * from Areas; 

Delimiter $$
create procedure sp_ModificarAreas(p_codigoArea int, p_nombreArea varchar (100))
	begin 
		update Areas
        set nombreArea = p_nombreArea
        where codigoArea = p_codigoArea;
	end $$
    
call sp_ModificarAreas(1,'Informatica' );
call sp_ModificarAreas(2,'Secretaria' );

Delimiter $$ 
create procedure sp_EliminarAreas(p_codigoArea int)
	begin 
		delete from Areas 
        where codigoArea =P_codigoArea;
	end $$

call sp_EliminarAreas(1); 

Delimiter $$
create procedure sp_BuscarAreas(p_codigoArea int)
	begin
		select * from Areas where codigoArea = p_codigoArea;
	end $$

call sp_BuscarAreas(2);


Delimiter $$
Create procedure sp_ListarAreas()
	begin 
		select codigoArea, nombreArea
		from Areas;
	end $$

call sp_ListarAreas();

#***********************************Cargos***************************

Delimiter $$
create procedure sp_Cargos(p_nombreCargo varchar (30))
	begin 
		insert into Cargos(nombreCargo)
			values (p_nombreCargo);
	end $$ 

call sp_Cargos('Jefe');
call sp_Cargos('Gerente');

select * from Cargos; 

Delimiter $$
create procedure sp_ModificarCargos(P_codigoCargo int, p_nombreCargo varchar (30))
	begin 
		update Cargos
        set nombreCargo = p_nombreCargo
        where codigoCargo = P_codigoCargo;
	end $$

call sp_ModificarCargos(1, 'Secretario');
call sp_ModificarCargos(2, 'Conserje');

Delimiter $$
create procedure sp_EliminarCargos(p_codigoCargo int)
	begin 
		delete from Cargos where codigoCargo = P_codigoCargo;
	end $$

call sp_EliminarCargos(1);

Delimiter $$ 
create procedure sp_BuscarCargos(P_codigoCargo int)
	begin
		select * from Cargos where codigoCargo = p_codigoCargo;
	end $$

call sp_BuscarCargos(2);

Delimiter $$
Create procedure sp_ListaCargos()
	begin 
		select codigoCargo, nombreCargo
		from Cargos;
	end $$

call sp_ListaCargos();

#*************************Responsableturno**************


Delimiter $$
create procedure sp_ResposableTurno(p_nombreResposable varchar(50), p_apellidoResposable varchar(50), p_telefonoResposable varchar(50), p_codigoArea int, p_codigoCargo int)
begin 
	insert into ResposableTurno(nombreResposable, apellidoResposable, telefonoResposable, codigoArea, codigoCargo)
		values (p_nombreResposable, p_apellidoResposable, p_telefonoResposable, p_codigoArea, p_codigoCargo);
	end $$

call sp_ResposableTurno('Kevin Rene', 'Gonzales Lau', '58789632', 2 , 1 );
call sp_ResposableTurno('Valerie', 'Gonzales', '78958745', 3 , 2);

select * from ResposableTurno;

Delimiter $$
create procedure sp_ModificarResposableTurno(p_codigoResposableTurno int, p_nombreResposable varchar (50), p_apellidoResposable varchar(50), p_telefonoResposable varchar(50), p_codigoArea int, p_codigoCargo int  )
	begin 
		update ResposableTurno
        set nombreResposable = p_nombreResposable, apellidoResposable = p_apellidoResposable, telefonoResposable = p_telefonoResposable, codigoArea = p_codigoArea, codigoCargo = p_codigoCargo
		where codigoResposableTurno = p_codigoResposableTurno; 
	end $$ 
    
call sp_ModificarResposableTurno(2, 'Oralia Izabel', 'Garcia Ramirez', '48057619', 2 , 2);
call sp_ModificarResposableTurno(1, 'Hailee', ' Stenfield', '25478902', 1 , 1);

Delimiter $$
create procedure sp_EliminarResposableTurno(p_codigoResposableTurno int)
	begin 
		delete from ResposableTurno where codigoResposableTurno = p_codigoResposableTurno;
	end $$

call sp_EliminarResposableTurno(1);

Delimiter $$ 
create procedure sp_BuscarResposableTurno(p_codigoResposableTurno int)
	begin
		select * from ResposableTurno where codigoResposableTurno = p_codigoResposableTurno;
	end $$

call sp_BuscarResposableTurno(2);


Delimiter $$
create procedure sp_ListaResposableTurno()
	begin
		select codigoResposableTurno, nombreResposable, apellidoResposable, telefonoResposable, codigoArea, codigoCargo
		from ResposableTurno;
	end $$

call sp_ListaResposableTurno();

#*******************Turno***************************
Delimiter $$
create procedure Agrega_turno(p_fechaTurno date, p_fechaCita date, p_valorCita decimal(10,2), p_codigoMedicoEspecialidad int, p_codigoResposableTurno int, p_codigoPacientes int)
	begin
		insert into  turno(fechaTurno, fechaCita, valorCita, codigoMedicoEspecialidad, codigoResposableTurno, codigoPacientes)
			values(p_fechaTurno, p_fechaCita, p_valorCita, p_codigoMedicoEspecialidad, p_codigoResposableTurno, p_codigoPacientes);
	end $$

call Agrega_turno('2019-06-07', '2019-06-7', 100.2, 1, 1, 1 );

select * from Turno;
 
Delimiter $$
create procedure Modicar_turno(p_codigoTurno int,p_fechaTurno date, p_fechaCita date, p_valorCita decimal(10,2), p_codigoMedicoEspecialidad int, p_codigoResposableTurno int, p_codigoPacientes int)
	begin
	update turno
	set fechaTurno=p_fechaTurno,fechaCita=p_fechaCita,valorCita=p_valorCita,codigoMedicoEspecialidad=p_codigoMedicoEspecialidad, codigoResposableTurno=p_codigoResposableTurno , codigoPacientes=p_codigoPacientes  where codigoTurno=p_codigoTurno;
	end $$

call Modicar_turno();
 
Delimiter $$
create procedure Eliminar_Turno(p_codigoTurno int)
	begin
	delete from turno  where codigoTurno=p_codigoTurno;
	end $$
    
call Eliminar_Turno(1)    

Delimiter $$
create procedure sp_BuscarTurno(p_codigoTurno int)
	begin 
		select * from Turno where codigoTurno = p_codigoTurno;
	end $$

call sp_BuscarTurno(1);
 
Delimiter $$
create procedure Elistar_Turno()
	begin
	select * from turno;
	end $$

call Elistar_Turno();







