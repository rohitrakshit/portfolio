create database equity;
connect equity;
create table stock(
	id varchar(50),
	name varchar(50),
	symbol varchar(50),
	symbol_id varchar(30),
	exchange varchar(10),
	elev_avrg_price double(9,2),
	last_price double(9,2),
	sgroup varchar(2),
	sector varchar(100),
	industry varchar(100),
	lt_date timestamp,
	orders varchar(2000),
	notes varchar(1000),
primary key(symbol,exchange)
);

create table users(
username varchar(30),
password varchar(30),
permission varchar(20)
);
INSERT INTO Users values('rohit','maw4$Twgtt','WRITE');


create table symbol(
nse varchar(30),
bse varchar(30),
name varchar(50),
primary key(nse)
);

INSERT INTO symbol values('TATASTEEL','500470','Tata Steel');
INSERT INTO symbol values('IDEA','532822','Idea Cellular');