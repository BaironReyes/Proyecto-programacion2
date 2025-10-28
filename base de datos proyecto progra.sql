CREATE database proyecto_hotel;
use proyecto_hotel;
create table clientes (
id int auto_increment primary key,
nombre varchar(100) not null,
apellido varchar(100) not null,
email varchar(120) not null UNIQUE,
telefono varchar (20) not null,
identificacion varchar (50) not null UNIQUE,
tipo_identificacion enum ('DUI', 'ID','Pasaporte', 'Licencia') not null, 
Fecha_creacion timestamp default current_timestamp 
);
create table empleados(
id int auto_increment primary key,
username varchar(100) not null unique,
password_hash varchar(255) not null,
puesto enum ('Recepcion','Admin') not null,
fecha_creacion timestamp default current_timestamp);

create table clientes_login(
id int auto_increment primary key,
username varchar(100) not null UNIQUE,
password_hash varchar(255) not null,
cliente_id int not null UNIQUE,
fecha_creacion timestamp default current_timestamp,
foreign key(cliente_id) references clientes (id) on delete restrict);

create table habitaciones(
id int auto_increment primary key,
numero_habitacion varchar (100) not null UNIQUE,
tipo_habitacion varchar(75) not null,
precio decimal(10,2) not null,
estado enum('Disponible','Mantenimiento','Reservado') not null);

create table reservas (
id int auto_increment primary key,
id_cliente int not null,
id_habitacion int not null, 
check_in date not null, 
check_out date not null,
estado enum('Confirmada','Cancelada','Finalizada')not null default 'Confirmada',
reserva_creada timestamp default current_timestamp,
foreign key(id_cliente) references clientes (id) on delete restrict,
foreign key(id_habitacion) references habitaciones(id) on delete restrict,
check (check_in < check_out));

create table facturas (
id int auto_increment primary key,
id_reserva int not null,
total decimal(10,2) not null,
fecha date default (current_date),
pagado boolean not null default false,
foreign key (id_reserva) references reservas(id) on delete restrict);

create index reserva_habitacion on reservas(id_habitacion, check_in, check_out);
create index reserva_cliente on reservas(id_cliente);



