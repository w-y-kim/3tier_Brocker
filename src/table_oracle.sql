create table customer
       (ssn varchar(15) primary key,
       cust_name varchar(40) not null,
       address varchar(100) not null ) ;
 
 
create table stock
       (symbol varchar(8) primary key,
       price number(10,2) not null) ;
 
 
create table shares
       (ssn varchar(15) not null,
	constraint shares_ssn_fk foreign key (ssn) references customer(ssn) on delete cascade,
       symbol varchar(8) not null,
	constraint shares_symbol_fk foreign key (symbol) references stock(symbol) on delete cascade,
       quantity number not null) ;
 
drop table shares
       
create table shares
       (ssn varchar(15) not null,
	constraint shares_ssn_fk foreign key (ssn) references customer(ssn),
       symbol varchar(8) not null,
	constraint shares_symbol_fk foreign key (symbol) references stock(symbol),
       quantity number not null) ;

       
       
insert into stock ( symbol,price) values( 'SUNW', 68.75);
insert into stock ( symbol,price) values( 'CyAs', 22.675);
insert into stock ( symbol,price) values( 'DUKE', 6.25);
insert into stock ( symbol,price) values( 'ABStk', 18.5);
insert into stock ( symbol,price) values( 'JSVco', 9.125);
insert into stock ( symbol,price) values( 'TMAs', 82.375);
insert into stock ( symbol,price) values( 'BWInc', 11.375);
insert into stock ( symbol,price) values( 'GMEnt', 44.625);
insert into stock ( symbol,price) values( 'PMLtd', 203.375);
insert into stock ( symbol,price) values( 'JDK', 33.5);
insert into customer values( '111-111', 'Yufirst', 'Seoul');
insert into customer values( '111-112', 'Yu', 'Seoul');
insert into customer values( '111-113', 'Leesun', 'Seoul');
insert into customer values( '111-114', 'Sehe4', 'Seoul');
insert into customer values( '111-115', 'yufirst', 'JeonJu');
insert into customer values( '111-116', 'Yufirst6', 'Seoul');
insert into customer values( '111-117', 'Yufirst7', 'Seoul');
insert into customer values( '111-118', 'Yufirst8', 'Seoul');
insert into customer values( '111-119', 'Yufirst9', 'Seoul');


UPDATE CUSTOMER set cus_name = '김사랑' where id ='111-111'

commit

select * from shares
select * from Customer

Select ssn from customer where ssn = '111-111'


insert into shares values( '111-111', 'SUNW', 100);
insert into shares values( '111-112', 'CyAs', 200);
insert into shares values( '111-113', 'ABStk',300);
insert into shares values( '111-114', 'BWInc',400);
insert into shares values( '111-119', 'JDK', 500);


commit

drop table customer 
drop table shares
drop table stock