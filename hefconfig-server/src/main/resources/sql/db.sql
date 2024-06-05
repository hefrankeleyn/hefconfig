create table if not exists configs (
   capp varchar(128) not null,
   cenv varchar(128) not null,
   cnamespace varchar(128) not null,
    ckey varchar(128) not null,
    cvalue varchar(255)
);

insert into configs(capp, cenv, cnamespace, ckey, cvalue) values('app01', 'dev', 'application', 'a', '001');
insert into configs(capp, cenv, cnamespace, ckey, cvalue) values('app01', 'dev', 'application', 'b', 'bj');
insert into configs(capp, cenv, cnamespace, ckey, cvalue) values('app01', 'dev', 'application', 'c', 'http://localhost:9129');


create table if not exists configs_version (
   capp varchar(128) not null,
   cenv varchar(128) not null,
   cnamespace varchar(128) not null,
   cversion bigint default -1
);


create table if not exists locks (
   lid int(11) primary key not null,
   lname varchar(255) not null
);

insert into locks(lid, lname) values(1, 'hefconfig-server');



